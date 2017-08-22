package com.overminddl1.gtecs.systems;

import com.artemis.Entity;
import com.overminddl1.gtecs.components.Icons;
import com.overminddl1.gtecs.components.IconsCache;

import net.minecraft.util.IIcon;
import net.mostlyoriginal.api.manager.AssetManager;

public class IconsCacheSystem extends AssetManager<Icons, IconsCache> {
	protected IconLoaderSystem iconSystem;

	public IconsCacheSystem() {
		super(Icons.class, IconsCache.class);
	}

	@Override
	protected void setup(final Entity entity, final Icons req, final IconsCache cache) {
		if (req.icons == null) {
			return;
		}
		if (cache.icons == null || req.icons.length != cache.icons.length) {
			cache.icons = new IIcon[req.icons.length];
		}
		for (int i = 0; i < req.icons.length; ++i) {
			cache.icons[i] = iconSystem.getIcon(req.icons[i]);
		}
	}

}
