package com.overminddl1.gtecs.systems;

import org.apache.commons.io.output.ByteArrayOutputStream;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.artemis.io.SaveFileFormat;
import com.artemis.managers.WorldSerializationManager;
import com.artemis.utils.IntBag;
import com.overminddl1.gtecs.components.PrintDebug;

import net.minecraft.nbt.NBTBase;

public class PrintDebugSystem extends BaseEntitySystem {
	private ComponentMapper<PrintDebug> printDebugMapper;

	private final WorldSerializationManager jsonSerializationManager;
	private NBTSerializationManager nbtSerializationManager;

	public PrintDebugSystem(final WorldSerializationManager jsonSerializationManager) {
		super(Aspect.all(PrintDebug.class));
		this.jsonSerializationManager = jsonSerializationManager;
	}

	@Override
	protected final void processSystem() {
		// System.out.println("To JSON!:");
		final IntBag actives = subscription.getEntities();
		if (actives.isEmpty()) {
			return;
		}
		printDebugMapper.get(actives.get(0)).printTimes = 42;
		final ByteArrayOutputStream writer = new ByteArrayOutputStream();
		jsonSerializationManager.save(writer, new SaveFileFormat(actives));
		final String json = writer.toString();

		final NBTBase nbt = nbtSerializationManager.entityIdToNBT(actives.get(0));

		System.out.println("DEBUGPRINT-JSON: " + json);
		System.out.println("DEBUGPRINT-NBT: " + nbt.toString());

		final int entityId = nbtSerializationManager.nbtToEntity(nbt);
		world.delete(entityId);

		for (int i = 0, s = actives.size(); s > i; i++) {
			final int e = actives.get(i);
			printDebugMapper.remove(e);
		}
	}
}