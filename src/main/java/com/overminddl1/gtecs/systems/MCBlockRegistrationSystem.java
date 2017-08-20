package com.overminddl1.gtecs.systems;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.artemis.utils.IntBag;
import com.overminddl1.gtecs.EBlock;
import com.overminddl1.gtecs.components.MCBlock;
import com.overminddl1.gtecs.systems.states.IInit;

import net.minecraft.block.Block;

public class MCBlockRegistrationSystem extends BaseEntitySystem implements IInit {
	private ComponentMapper<MCBlock> mcBlockMapper;

	public MCBlockRegistrationSystem() {
		super(Aspect.all(MCBlock.class));
	}

	@Override
	protected final void processSystem() {
		final Block air = (Block) Block.blockRegistry.getObject("minecraft:air");
		final IntBag actives = subscription.getEntities();
		debugtest(actives.get(0));
		for (int i = 0, s = actives.size(); s > i; i++) {
			final int e = actives.get(i);
			final MCBlock def = mcBlockMapper.get(e);
			final String name = def.instanceable ? "E-" + def.name : def.name;
			if (def.name.length() >= 3 && Block.blockRegistry.getObject(name) == air) {
				final EBlock block = new EBlock(getWorld(), e, def, name);
			}
		}
	}

	private void debugtest(final int e) {
		final MCBlock def = mcBlockMapper.get(e);
		def.name = "blah";
	}
}