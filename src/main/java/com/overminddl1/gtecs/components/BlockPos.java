package com.overminddl1.gtecs.components;

import com.artemis.Component;

import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class BlockPos extends Component {
	public int dimensionId; // world.provider.dimensionId / DimensionManager.getWorld(dimensionId)
	public int x;
	public int y;
	public int z;

	void setWorld(final World world) {
		dimensionId = world.provider.dimensionId;
	}

	World getWorld() {
		return DimensionManager.getWorld(dimensionId);
	}
}
