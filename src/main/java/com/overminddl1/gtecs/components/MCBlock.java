package com.overminddl1.gtecs.components;

import com.artemis.Component;

public class MCBlock extends Component {
	public String name;
	public boolean instanceable = false;
	/*
	 * 1 = all sides, 2 = top/bottom, sides, 6 = all sides in integral format,
	 */
	public String[] textures;
	public boolean normalCube = true;
	public boolean opaqueCube = true;
	public boolean[] sideSolid = null;
	public boolean renderAsNormalBlock = true;

	public String material = ""; // If this is specified then the below is ignored
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

	public boolean silkHarvestable = true;
	public boolean canBeSpawnedOn = true;
	public boolean triviallyReplaceable = false;
	public float explosionResistance = 1.0f;
	public int[] fireSpreadSpeed = null;
	public int[] flammability = null;
	public boolean[] fireSource = null;
	public boolean[] flammable = null;
	public int harvestLevel = 0;
	public String[] harvestTools = new String[] { "pickaxe" };
	public int opacity = 255;
	public int dropped_base = 1;
	public int dropped_randomMax = 0;

	// Generally never touched, useful for logic/invisible blocks...
	public boolean isAir = false;
}
