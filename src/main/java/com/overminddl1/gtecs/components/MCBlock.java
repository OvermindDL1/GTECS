package com.overminddl1.gtecs.components;

import com.artemis.Component;

public class MCBlock extends Component {
	public String name;
	public boolean instanceable = false;

	public boolean material_isLiquid = false;
	public boolean material_isSolid = true;
	public boolean material_blocksGrass = true;
	public boolean material_blocksMovement = true;
	public boolean material_burnable = false;
	public boolean material_replaceable = false;
	public boolean material_opaque = true;
	public boolean material_handHarvestable = false;
	public int material_mobilityFlag = 0;
	public boolean material_isAdventureModeExempt = false;
	public int material_mapColorIdx = 0;

	public String sound_name = "stone";
	public float sound_volume = 1.0F;
	public float sound_frequency = 1.0F;

	public String metaEncoder;
}
