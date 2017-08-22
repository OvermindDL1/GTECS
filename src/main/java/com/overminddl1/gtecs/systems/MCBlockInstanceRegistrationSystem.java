package com.overminddl1.gtecs.systems;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.artemis.utils.IntBag;
import com.overminddl1.gtecs.EBlock;
import com.overminddl1.gtecs.EBlockInst;
import com.overminddl1.gtecs.components.MCBlockInstance;
import com.overminddl1.gtecs.systems.states.IInit;

import net.minecraft.block.Block;

public class MCBlockInstanceRegistrationSystem extends BaseEntitySystem implements IInit {
	private ComponentMapper<MCBlockInstance> mcBlockInstanceMapper;

	public MCBlockInstanceRegistrationSystem() {
		super(Aspect.all(MCBlockInstance.class));
	}

	@Override
	protected void processSystem() { // TODO: Function is not complete, need to gen each block as necessary, maybe in
										// the main system...
		final Block air = (Block) Block.blockRegistry.getObject("minecraft:air");
		final IntBag actives = subscription.getEntities();
		for (int i = 0, s = actives.size(); s > i; i++) {
			final int e = actives.get(i);
			final MCBlockInstance instDef = mcBlockInstanceMapper.get(e);
			final String blockName = "E-" + instDef.name;
			final Block block_ = (Block) Block.blockRegistry.getObject("E-" + blockName);
			if (block_ instanceof EBlockInst) {
				return;
			}
			if (!(block_ instanceof EBlockInst)) {
				return;
			}
			final EBlock block = (EBlock) block_;
		}
	}

}
