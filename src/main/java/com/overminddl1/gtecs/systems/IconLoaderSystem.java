package com.overminddl1.gtecs.systems;

import java.util.HashMap;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.artemis.utils.IntBag;
import com.overminddl1.gtecs.components.Icons;
import com.overminddl1.gtecs.systems.states.INever;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

public class IconLoaderSystem extends BaseEntitySystem implements INever {
	private ComponentMapper<Icons> iconsMapper;
	private HashMap<String, IIcon> iconsMap;

	public IconLoaderSystem() {
		super(Aspect.all(Icons.class));
	}

	@Override
	protected void processSystem() {
	}

	public void registerBlockIcons(final IIconRegister iconRegistry) {
		if (iconsMap != null) {
			return; // Already done
		}

		iconsMap = new HashMap<>();

		final IntBag actives = subscription.getEntities();
		for (int i = 0, s = actives.size(); s > i; i++) {
			final int entityId = actives.get(i);
			final Icons icons = iconsMapper.get(entityId);
			for (final String icon : icons.icons) {
				if (!iconsMap.containsKey(icon)) {
					iconsMap.put(icon, iconRegistry.registerIcon(icon));
				}
			}
			iconsMapper.set(entityId, false);
		}
	}

	public IIcon getIcon(final String id) {
		return iconsMap.get(id);
	}

	public IIcon getIcon(final IIconRegister iconRegistry, final String id) {
		IIcon icon = getIcon(id);
		if (icon != null) {
			return icon;
		}
		icon = iconRegistry.registerIcon(id);
		iconsMap.put(id, icon);
		return icon;
	}

}
