package com.overminddl1.gtecs.systems;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.security.AccessControlException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.artemis.BaseSystem;
import com.artemis.Component;
import com.artemis.ComponentManager;
import com.artemis.ComponentMapper;
import com.artemis.ComponentType;
import com.artemis.Entity;
import com.artemis.annotations.Transient;
import com.artemis.annotations.Wire;
import com.artemis.components.SerializationTag;
import com.artemis.io.DefaultObjectStore;
import com.artemis.managers.GroupManager;
import com.artemis.managers.TagManager;
import com.artemis.utils.Bag;
import com.artemis.utils.ImmutableBag;
import com.artemis.utils.IntBag;
import com.artemis.utils.reflect.ClassReflection;
import com.esotericsoftware.jsonbeans.ObjectMap;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.overminddl1.gtecs.systems.nbt.INBTPOJO;
import com.overminddl1.gtecs.systems.nbt.INBTSerialize;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.nbt.NBTTagString;

@Wire(failOnNull = false)
public class NBTSerializationManager extends BaseSystem {
	private final Bag<Component> components = new Bag<Component>();
	private boolean isSerializing = false;
	private GroupManager groupManager;
	private TagManager tagManager;
	private Collection<String> registeredTags;
	private DefaultObjectStore defaultValues;
	private IntBag entityBag;

	private ComponentMapper<SerializationTag> saveTagMapper;

	private final ObjectMap<Class, ObjectMap<String, FieldMetadata>> typeToFields = new ObjectMap();
	private final ObjectMap<Class, Object[]> classToDefaultValues = new ObjectMap();
	private final BiMap<String, Class<? extends Component>> nameComponentMap = HashBiMap.create();
	private BiMap<Class<? extends Component>, String> componentNameMap;
	Set<Class<? extends Component>> transientComponents = new HashSet<Class<? extends Component>>();

	@Override
	protected void initialize() {
		registeredTags = (tagManager != null) ? tagManager.getRegisteredTags() : Collections.<String>emptyList();
		entityBag = new IntBag();
		// Setup component name -> type mapping
		final ComponentManager cm = world.getComponentManager();
		final ImmutableBag<ComponentType> types = cm.getComponentTypes();
		for (int i = 0, count = types.size(); i < count; ++i) {
			final ComponentType type = types.get(i);
			final Class<? extends Component> componentKlass = type.getType();
			if (ClassReflection.getDeclaredAnnotation(componentKlass, Transient.class) == null) {
				final String componentIdentifier = componentKlass.getSimpleName();
				if (nameComponentMap.containsKey(componentIdentifier)) {
					throw new RuntimeException("Duplicate ECS Component Names of: " + componentIdentifier);
				}
				nameComponentMap.put(componentIdentifier, componentKlass);
			} else {
				transientComponents.add(componentKlass);
			}
		}
		componentNameMap = nameComponentMap.inverse();
	}

	@Override
	protected void processSystem() {
	}

	// Saving

	public NBTBase entityIdToNBT(final int entityId) {
		return entityToNBT(world.getEntity(entityId));
	}

	public NBTBase entityToNBT(final Entity e) {
		if (isSerializing) {
			return new NBTTagInt(e.getId());
		}
		isSerializing = true;
		final NBTTagCompound nbt = new NBTTagCompound();
		final int entityId = e.getId();
		entityBag.clear();
		entityBag.add(entityId);

		// Archetype
		// nbt.setInteger("a", world.getComponentManager().getIdentity(entityID));
		final int archetypeID = world.getComponentManager().getIdentity(entityId);
		// Tag
		for (final String tag : registeredTags) {
			if (tagManager.getEntityId(tag) != entityId) {
				continue;
			}

			nbt.setString("tag", tag);
			break;
		}
		// Key'd tag
		if (saveTagMapper.has(entityId)) {
			final String key = saveTagMapper.get(entityId).tag;
			if (key != null) {
				nbt.setString("key", key);
			}
		}
		// Groups
		if (groupManager != null) {
			final ImmutableBag<String> groups = groupManager.getGroups(e);
			if (groups.size() > 0) {
				final NBTTagList list = new NBTTagList();
				for (final String group : groups) {
					list.appendTag(new NBTTagString(group));
				}
				nbt.setTag("groups", list);
			}
		}
		// Components
		final NBTTagCompound nbtComponents = new NBTTagCompound();
		world.getComponentManager().getComponentsFor(entityId, components);
		for (int i = 0, s = components.size(); s > i; i++) {
			final Component c = components.get(i);
			final Class<? extends Component> componentKlass = c.getClass();
			if (transientComponents.contains(componentKlass)) {
				continue;
			}

			final String componentIdentifier = componentNameMap.get(componentKlass);
			final NBTBase nbtComponent = writeFieldsOfComponent(c);
			nbtComponents.setTag(componentIdentifier, nbtComponent);
		}
		nbt.setTag("c", nbtComponents);

		components.clear();
		isSerializing = false;
		return nbt;
	}

	// Loading

	public int nbtToEntity(final NBTBase nbtBase) {
		if (!(nbtBase instanceof NBTTagCompound)) {
			return -1;
		}
		final NBTTagCompound nbt = (NBTTagCompound) nbtBase;

		final int entityId = world.create();

		// Tag
		if (tagManager != null && nbt.hasKey("t")) {
			tagManager.register(nbt.getString("t"), entityId);
		}
		// Key
		if (nbt.hasKey("k")) {
			saveTagMapper.create(entityId).tag = nbt.getString("k");
		}
		// Groups
		if (groupManager != null && nbt.hasKey("g")) {
			final NBTTagList list = nbt.getTagList("g", 8);
			for (int i = 0, count = list.tagCount(); i < count; ++i) {
				final String group = list.getStringTagAt(i);
				groupManager.add(world.getEntity(entityId), group);
			}
		}
		// Components
		if (nbt.hasKey("c")) {
			final NBTTagCompound nbtComponents = nbt.getCompoundTag("c");
			final Set<String> keys = nbtComponents.func_150296_c();
			for (final String componentIdentifier : keys) {
				final Class<? extends Component> componentKlazz = nameComponentMap.get(componentIdentifier);
				final Component c = world.edit(entityId).create(componentKlazz);
				final NBTBase nbtComponent = nbtComponents.getTag(componentIdentifier);
				writeObjectFromNBT(c, nbtComponent);
			}
		}

		return entityId;
	}

	// Helpers

	private NBTBase writeFieldsOfComponent(final Object object) {
		if (object instanceof INBTSerialize) {
			return ((INBTSerialize) object).toNBT();
		}

		final NBTTagCompound nbt = new NBTTagCompound();

		final Class type = object.getClass();

		final Object[] defaultValues = getDefaultValues(type);

		ObjectMap<String, FieldMetadata> fields = typeToFields.get(type);
		if (fields == null) {
			fields = cacheFields(type);
		}
		int i = 0;
		for (final FieldMetadata metadata : fields.values()) {
			final Field field = metadata.field;
			try {
				final Object value = field.get(object);
				if (defaultValues != null) {
					final Object defaultValue = defaultValues[i++];
					if (value == null && defaultValue == null) {
						continue;
					}
					if (value != null && defaultValue != null && value.equals(defaultValue)) {
						continue;
					}
				}

				nbt.setTag(field.getName(), valueToNBT(value, field.getType(), metadata.elementType));
			} catch (final IllegalAccessException ex) {
				throw new RuntimeException("Error accessing field: " + field.getName() + " (" + type.getName() + ")",
						ex);
			}
		}

		return nbt;
	}

	private void writeObjectFromNBT(final Object object, final NBTBase nbtBase) {
		if (object instanceof INBTSerialize) {
			((INBTSerialize) object).fromNBT(object, nbtBase);
			return;
		}

		final Class type = object.getClass();

		ObjectMap<String, FieldMetadata> fields = typeToFields.get(type);
		if (fields == null) {
			fields = cacheFields(type);
		}

		final NBTTagCompound nbt = (NBTTagCompound) nbtBase;
		final Set<String> keys = nbt.func_150296_c();
		for (final String fieldName : keys) {
			final NBTBase nbtField = nbt.getTag(fieldName);
			final FieldMetadata field = fields.get(fieldName);
			nbtToField(nbtField, object, field.field, field.elementType);
		}
	}

	private void nbtToField(final NBTBase nbt, final Object object, final Field field, final Class elementType) {
		try {
			switch (nbt.getId()) {
			case 0:
				throw new RuntimeException("Invalid NBT type in field deserialization: 0");
			case 1:
				field.setByte(object, ((NBTTagByte) nbt).func_150290_f());
				return;
			case 2:
				field.setShort(object, ((NBTTagShort) nbt).func_150289_e());
				return;
			case 3:
				field.setInt(object, ((NBTTagInt) nbt).func_150287_d());
				return;
			case 4:
				field.setLong(object, ((NBTTagLong) nbt).func_150291_c());
				return;
			case 5:
				field.setFloat(object, ((NBTTagFloat) nbt).func_150288_h());
				return;
			case 6:
				field.setDouble(object, ((NBTTagDouble) nbt).func_150286_g());
				return;
			case 7:
				if (field.getType().isArray() && elementType == Byte.TYPE) {
					final NBTTagByteArray nbtArr = (NBTTagByteArray) nbt;
					final byte[] arr = nbtArr.func_150292_c().clone();
					field.set(object, arr);
				} else {
					throw new RuntimeException("Attempted to deserialize a bytearray into a non-bytearray");
				}
				return;
			case 8:
				field.set(object, ((NBTTagString) nbt).func_150285_a_());
				return;
			case 9:
				if (field.getType().isArray()) {
					final NBTTagList nbtArr = (NBTTagList) nbt.copy(); // Yes, copy, because the `getTag` method
					// vanished...
					final int count = nbtArr.tagCount();
					if (count == 0) {
						return;
					}
					if (elementType == String.class) {
						final String[] arr = new String[count];
						for (int i = 0; i < count; ++i) {
							arr[i] = nbtArr.getStringTagAt(i);
						}
						field.set(object, arr);
						return;
					}
					final Object[] arr = new Object[count];
					int idx = count - 1;
					while (idx >= 0) {
						final NBTBase nbtElem = nbtArr.removeTag(idx);
						final Object obj = newInstance(elementType);
						writeObjectFromNBT(obj, nbtElem);
						arr[idx] = obj;
						idx = idx + 1;
					}
					field.set(object, arr);
				}
				return;
			case 10:
				final NBTTagCompound nbtCom = (NBTTagCompound) nbt;
				final Class<?> type = field.getType();
				// All field classes need to be default constructed first
				if (Map.class.isAssignableFrom(type)) {
					final Map map = (Map) field.get(object);
					ObjectMap<String, FieldMetadata> fields = typeToFields.get(type);
					if (fields == null) {
						fields = cacheFields(type);
					}
					final Set<String> keys = nbtCom.func_150296_c();
					for (final String key : keys) {
						final NBTBase nbtValue = nbtCom.getTag(key);
						// nbtToField(nbtValue, map, field, null);
					}
					throw new RuntimeException("Deserializing Map's directly is not supported yet...");
				} else if (Enum.class.isAssignableFrom(type)) {
					throw new RuntimeException("Deserializing Enum's directly is not supported yet...");
				} else {
					// Fallback to POJO-like storage for now...
					writeObjectFromNBT(field.get(object), nbt);
				}
				return;
			case 11:
				if (field.getType().isArray() && elementType == Integer.TYPE) {
					final NBTTagIntArray nbtArr = (NBTTagIntArray) nbt;
					final int[] arr = nbtArr.func_150302_c().clone();
					field.set(object, arr);
				} else {
					throw new RuntimeException("Attempted to deserialize a bytearray into a non-bytearray");
				}
				return;
			default:
				throw new RuntimeException("Unknown NBT tag type: " + nbt.getId());
			}
		} catch (final IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (final IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	private Object[] getDefaultValues(final Class type) {
		if (classToDefaultValues.containsKey(type)) {
			return classToDefaultValues.get(type);
		}
		Object object;
		try {
			object = newInstance(type);
		} catch (final Exception ex) {
			classToDefaultValues.put(type, null);
			return null;
		}

		ObjectMap<String, FieldMetadata> fields = typeToFields.get(type);
		if (fields == null) {
			fields = cacheFields(type);
		}

		final Object[] values = new Object[fields.size];
		classToDefaultValues.put(type, values);

		int i = 0;
		for (final FieldMetadata metadata : fields.values()) {
			final Field field = metadata.field;
			try {
				values[i++] = field.get(object);
			} catch (final IllegalAccessException ex) {
				throw new RuntimeException("Error accessing field: " + field.getName() + " (" + type.getName() + ")",
						ex);
			}
		}
		return values;
	}

	protected Object newInstance(Class type) {
		try {
			return type.newInstance();
		} catch (Exception ex) {
			try {
				// Try a private constructor.
				final Constructor constructor = type.getDeclaredConstructor();
				constructor.setAccessible(true);
				return constructor.newInstance();
			} catch (final SecurityException ignored) {
			} catch (final IllegalAccessException ignored) {
				if (Enum.class.isAssignableFrom(type)) {
					if (type.getEnumConstants() == null) {
						type = type.getSuperclass();
					}
					return type.getEnumConstants()[0];
				}

				if (type.isArray()) {
					throw new RuntimeException("Encountered object when expected array of type: " + type.getName(), ex);
				} else if (type.isMemberClass() && !Modifier.isStatic(type.getModifiers())) {
					throw new RuntimeException("Class cannot be created (non-static member class): " + type.getName(),
							ex);
				} else {
					throw new RuntimeException(
							"Class cannot be created (missing no-arg constructor): " + type.getName(), ex);
				}
			} catch (final Exception privateConstructorException) {
				ex = privateConstructorException;
			}
			throw new RuntimeException("Error constructing instance of class: " + type.getName(), ex);
		}
	}

	private ObjectMap<String, FieldMetadata> cacheFields(final Class type) {
		final ArrayList<Field> allFields = new ArrayList();
		Class nextClass = type;
		while (nextClass != Object.class) {
			Collections.addAll(allFields, nextClass.getDeclaredFields());
			nextClass = nextClass.getSuperclass();
		}

		final ObjectMap<String, FieldMetadata> nameToField = new ObjectMap();
		for (int i = 0, n = allFields.size(); i < n; i++) {
			final Field field = allFields.get(i);

			final int modifiers = field.getModifiers();
			if (Modifier.isTransient(modifiers)) {
				continue;
			}
			if (Modifier.isStatic(modifiers)) {
				continue;
			}
			if (field.isSynthetic()) {
				continue;
			}

			if (!field.isAccessible()) {
				try {
					field.setAccessible(true);
				} catch (final AccessControlException ex) {
					continue;
				}
			}

			nameToField.put(field.getName(), new FieldMetadata(field));
		}
		typeToFields.put(type, nameToField);
		return nameToField;
	}

	public NBTBase valueToNBT(final Object value, Class knownType, Class elementType) {
		if (value == null) {
			return new NBTTagCompound();
		}

		final Class actualType = value.getClass();

		if (knownType == null || knownType.isPrimitive()) {
			knownType = actualType;
		}

		if (knownType == String.class) {
			return new NBTTagString((String) value);
		}

		if (knownType == Integer.class) {
			return new NBTTagInt(((Integer) value).intValue());
		}

		if (knownType == Long.class) {
			return new NBTTagLong(((Long) value).longValue());
		}

		if (knownType == Short.class) {
			return new NBTTagShort(((Short) value).shortValue());
		}

		if (knownType == Byte.class) {
			return new NBTTagByte(((Byte) value).byteValue());
		}

		if (knownType == Character.class) {
			return new NBTTagByte((byte) ((Character) value).charValue());
		}

		if (knownType == Double.class) {
			return new NBTTagDouble(((Double) value).doubleValue());
		}

		if (knownType == Float.class) {
			return new NBTTagFloat(((Float) value).floatValue());
		}

		if (knownType == Boolean.class) {
			return new NBTTagByte((byte) (((Boolean) value).booleanValue() ? 1 : 0));
		}

		if (value instanceof INBTPOJO) {
			return writeFieldsOfComponent(value);
		}

		if (value instanceof INBTSerialize) {
			return ((INBTSerialize) value).toNBT();
		}

		if (knownType.isArray()) {
			if (elementType == null) {
				elementType = actualType.getComponentType();
			}
			final int length = Array.getLength(value);
			if (elementType == Byte.TYPE) {
				final byte[] arr = new byte[length];
				for (int i = 0; i < length; i++) {
					arr[i] = Array.getByte(value, i);
				}
				return new NBTTagByteArray(arr);
			}
			if (elementType == Integer.TYPE) {
				final int[] arr = new int[length];
				for (int i = 0; i < length; i++) {
					arr[i] = Array.getInt(value, i);
				}
				return new NBTTagIntArray(arr);
			}
			// if (elementType == String.class) {
			// final NBTTagList arr = new NBTTagList();
			// for (int i = 0; i < length; i++) {
			// arr.appendTag(valueToNBT(Array.get(value, i), elementType, null));
			// }
			// return arr;
			// }
			final NBTTagList arr = new NBTTagList();
			for (int i = 0; i < length; i++) {
				arr.appendTag(valueToNBT(Array.get(value, i), elementType, null));
			}
			return arr;

			// throw new RuntimeException(
			// "Serialization error of unsupported array type with elements of: " +
			// elementType.getName());
		}

		if (value instanceof Map) {
			final NBTTagCompound nbt = new NBTTagCompound();
			for (final Map.Entry entry : ((Map<?, ?>) value).entrySet()) {
				final String name = convertToString(entry.getKey());
				nbt.setTag(name, valueToNBT(entry.getValue(), elementType, null));
			}
			return nbt;
		}

		if (Enum.class.isAssignableFrom(actualType)) {
			final NBTTagCompound nbt = new NBTTagCompound();
			nbt.setString("enum", ((Enum) value).name());
			return nbt;
		}

		// Fallback to POJO-like storage for now...
		return writeFieldsOfComponent(value);
	}

	private String convertToString(final Object object) {
		if (object instanceof Class) {
			return ((Class) object).getName();
		}
		return String.valueOf(object);
	}

	static private class FieldMetadata {
		Field field;
		Class elementType;

		public FieldMetadata(final Field field) {
			this.field = field;

			final Type genericType = field.getGenericType();
			if (genericType instanceof ParameterizedType) {
				final Type[] actualTypes = ((ParameterizedType) genericType).getActualTypeArguments();
				if (actualTypes.length == 1) {
					final Type actualType = actualTypes[0];
					if (actualType instanceof Class) {
						elementType = (Class) actualType;
					} else if (actualType instanceof ParameterizedType) {
						elementType = (Class) ((ParameterizedType) actualType).getRawType();
					}
				}
			}
		}
	}

}
