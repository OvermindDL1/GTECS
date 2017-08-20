package com.overminddl1.gtecs;

import com.overminddl1.gtecs.components.MCBlock;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class EMaterial extends Material {
	private final MCBlock def;

	public EMaterial(final MCBlock def) {
		super(MapColor.mapColorArray[def.material_mapColorIdx]);
		this.def = def;
	}

	@Override
	public boolean blocksMovement() {
		return def.material_blocksMovement;
	}

	@Override
	public boolean getCanBlockGrass() {
		return def.material_blocksGrass;
	}

	@Override
	public boolean getCanBurn() {
		return def.material_burnable;
	}

	@Override
	public boolean isLiquid() {
		return def.material_isLiquid;
	}

	@Override
	public boolean isOpaque() {
		return def.material_opaque;
	}

	@Override
	public boolean isReplaceable() {
		return def.material_replaceable;
	}

	@Override
	public boolean isSolid() {
		return def.material_isSolid;
	}

	@Override
	public boolean isToolNotRequired() {
		return def.material_handHarvestable;
	}

	@Override
	public int getMaterialMobility() {
		return def.material_mobilityFlag;
	}

	@Override
	public boolean isAdventureModeExempt() {
		return def.material_isAdventureModeExempt;
	}

}
