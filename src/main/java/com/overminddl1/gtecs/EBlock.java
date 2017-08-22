package com.overminddl1.gtecs;

import java.util.Random;

import com.overminddl1.gtecs.components.MCBlock;
import com.overminddl1.gtecs.systems.IconLoaderSystem;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregapi.data.CS;
import gregapi.data.LH;
import gregapi.util.ST;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * @author OvermindDL1
 */
public class EBlock extends Block {
	public final com.artemis.World ecs;
	public final int entityID;
	public final MCBlock def;
	public IIcon[] textures;
	// Bloody hell this would have been easier, why is the constructor not public?!?
	// public final FMLControlledNamespacedRegistry<Archetype>
	// instanceBuilderRegistry;
	// public final Hashtable<String, Archetype> instanceBuilderRegistry;

	protected IconLoaderSystem iconSystem;

	public EBlock(final com.artemis.World ecs, final int entityID, final MCBlock def, final String name) {
		super(EBlock.getNamedMaterial(def));
		ecs.inject(this);
		this.ecs = ecs;
		this.entityID = entityID;
		this.def = def;
		setStepSound(new SoundType(def.sound_name, def.sound_volume, def.sound_frequency));
		setBlockName(name);
		GameRegistry.registerBlock(this, EItemBlock.class, getUnlocalizedName());
		LH.add(getUnlocalizedName() + "." + CS.W + ".name", "Any Sub-Block of this one");
		textures = new IIcon[def.textures.length >= 1 ? def.textures.length : 1];
		// if (def.instanceable) {
		// instanceBuilderRegistry = new Hashtable<String, Archetype>();
		// }
	}

	public static Material getNamedMaterial(final MCBlock def) {
		switch (def.material) {
		case "":
			return new EMaterial(def);
		case "air":
			return Material.air;
		case "grass":
			return Material.grass;
		case "ground":
			return Material.ground;
		case "wood":
			return Material.wood;
		case "rock":
			return Material.rock;
		case "iron":
			return Material.iron;
		case "anvil":
			return Material.anvil;
		case "water":
			return Material.water;
		case "lava":
			return Material.lava;
		case "leaves":
			return Material.leaves;
		case "plants":
			return Material.plants;
		case "vine":
			return Material.vine;
		case "sponge":
			return Material.sponge;
		case "cloth":
			return Material.cloth;
		case "fire":
			return Material.fire;
		case "sand":
			return Material.sand;
		case "circuits":
			return Material.circuits;
		case "carpet":
			return Material.carpet;
		case "glass":
			return Material.glass;
		case "restoneLight":
			return Material.redstoneLight;
		case "tnt":
			return Material.tnt;
		case "coral":
			return Material.coral;
		case "ice":
			return Material.ice;
		case "packedIce":
			return Material.packedIce;
		case "snow":
			return Material.snow;
		case "craftdSnow":
			return Material.craftedSnow;
		case "cactus":
			return Material.cactus;
		case "clay":
			return Material.clay;
		case "gourd":
			return Material.gourd;
		case "dragonEgg":
			return Material.dragonEgg;
		case "portal":
			return Material.portal;
		case "cae":
			return Material.cake;
		case "web":
			return Material.web;
		default:
			throw new NullPointerException("Material name does not exist: " + def.material);
		}
	}

	public static float getSidedArrayReturn(final float[] arr, final int side, final float def) {
		if (arr == null) {
			return def;
		}
		switch (arr.length) {
		case 0:
			return def; // No entries, return default
		case 1:
			return arr[0]; // 1 entry, return it always
		case 2:
		case 3:
		case 4:
			return arr[side <= 1 ? 0 : 1]; // 2/3/4 entries, return first if up/down, second otherwise
		case 5:
			return arr[side <= 1 ? 0 : side - 1]; // 5 entries, first is top/bottom, rest are each side
		case 6:
		default:
			return arr[side]; // 6 or more entries
		}
	}

	public static boolean getSidedArrayReturn(final boolean[] arr, final int side, final boolean def) {
		if (arr == null) {
			return def;
		}
		switch (arr.length) {
		case 0:
			return def; // No entries, return default
		case 1:
			return arr[0]; // 1 entry, return it always
		case 2:
		case 3:
		case 4:
			return arr[side <= 1 ? 0 : 1]; // 2/3/4 entries, return first if up/down, second otherwise
		case 5:
			return arr[side <= 1 ? 0 : side - 1]; // 5 entries, first is top/bottom, rest are each side
		case 6:
		default:
			return arr[side]; // 6 or more entries
		}
	}

	public static int getSidedArrayReturn(final int[] arr, final int side, final int def) {
		if (arr == null) {
			return def;
		}
		switch (arr.length) {
		case 0:
			return def; // No entries, return default
		case 1:
			return arr[0]; // 1 entry, return it always
		case 2:
		case 3:
		case 4:
			return arr[side <= 1 ? 0 : 1]; // 2/3/4 entries, return first if up/down, second otherwise
		case 5:
			return arr[side <= 1 ? 0 : side - 1]; // 5 entries, first is top/bottom, rest are each side
		case 6:
		default:
			return arr[side]; // 6 or more entries
		}
	}

	public static <T> T getSidedArrayReturn(final T[] arr, final int side, final T def) {
		if (arr == null) {
			return def;
		}
		switch (arr.length) {
		case 0:
			return def; // No entries, return default
		case 1:
			return arr[0]; // 1 entry, return it always
		case 2:
		case 3:
		case 4:
			return arr[side <= 1 ? 0 : 1]; // 2/3/4 entries, return first if up/down, second otherwise
		case 5:
			return arr[side <= 1 ? 0 : side - 1]; // 5 entries, first is top/bottom, rest are each side
		case 6:
		default:
			return arr[side]; // 6 or more entries
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(final IBlockAccess worldAccess, final int x, final int y, final int z, final int side) {
		return EBlock.getSidedArrayReturn(textures, side, null);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(final int side, final int meta) {
		return EBlock.getSidedArrayReturn(textures, side, null);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(final IIconRegister iconRegistry) {
		if (def.textures.length == 0) {
			textures[0] = iconRegistry
					.registerIcon("MISSING_ICON_BLOCK_" + Block.getIdFromBlock(this) + "_" + getUnlocalizedName());
			return;
		}
		iconSystem.registerBlockIcons(iconRegistry);
		for (int i = 0, size = def.textures.length; i < size; ++i) {
			textures[i] = iconSystem.getIcon(iconRegistry, def.textures[i]);
		}
	}

	@Override
	public boolean hasTileEntity(final int metadata) {
		return false; // TODO
	}

	@Override
	public TileEntity createTileEntity(final World world, final int metadata) {
		return null; // TODO
	}

	@Override
	public boolean canBeReplacedByLeaves(final IBlockAccess aWorld, final int aX, final int aY, final int aZ) {
		return def.triviallyReplaceable;
	}

	@Override
	public boolean canCreatureSpawn(final EnumCreatureType type, final IBlockAccess aWorld, final int aX, final int aY,
			final int aZ) {
		return def.canBeSpawnedOn;
	}

	@Override
	public boolean canSilkHarvest() {
		return def.silkHarvestable;
	}

	@Override
	public ItemStack createStackedBlock(final int aMeta) {
		return ST.make(this, 1, damageDropped(aMeta));
	}

	@Override
	public int damageDropped(final int aMeta) {
		return aMeta; // TODO: Maybe return 0, move this into the subclasses?
	}

	@Override
	public int getDamageValue(final World aWorld, final int aX, final int aY, final int aZ) {
		return aWorld.getBlockMetadata(aX, aY, aZ); // TODO: Maybe return 0, move this into the subclasses?
	}

	@Override
	public float getExplosionResistance(final Entity aEntity) {
		return getExplosionResistance(0);
	}

	@Override
	public float getExplosionResistance(final Entity aEntity, final World aWorld, final int aX, final int aY,
			final int aZ, final double eX, final double eY, final double eZ) {
		return getExplosionResistance(aWorld.getBlockMetadata(aX, aY, aZ));
	}

	public float getExplosionResistance(final int aMeta) {
		return def.explosionResistance;
	}

	@Override
	public int getFireSpreadSpeed(final IBlockAccess aWorld, final int aX, final int aY, final int aZ,
			final ForgeDirection aSide) {
		return EBlock.getSidedArrayReturn(def.fireSpreadSpeed, aSide.ordinal(), 0);
	}

	@Override
	public int getFlammability(final IBlockAccess aWorld, final int aX, final int aY, final int aZ,
			final ForgeDirection aSide) {
		return EBlock.getSidedArrayReturn(def.flammability, aSide.ordinal(), 0);
	}

	@Override
	public int getHarvestLevel(final int aMeta) {
		return def.harvestLevel;
	}

	@Override
	public String getHarvestTool(final int aMeta) {
		return def.harvestTools.length > 0 ? def.harvestTools[0] : "pickaxe";
	}

	@Override
	public Item getItem(final World aWorld, final int aX, final int aY, final int aZ) {
		return Item.getItemFromBlock(this);
	}

	@Override
	public Item getItemDropped(final int aMeta, final Random aRandom, final int aFortune) {
		return Item.getItemFromBlock(this);
	}

	@Override
	public int getLightOpacity() {
		return def.opacity;
	}

	@Override
	public String getLocalizedName() {
		return StatCollector.translateToLocal(getUnlocalizedName() + ".name");
	}

	@Override
	public String getUnlocalizedName() {
		return def.name;
	}

	@Override
	public boolean isFireSource(final World aWorld, final int aX, final int aY, final int aZ,
			final ForgeDirection aSide) {
		return EBlock.getSidedArrayReturn(def.fireSource, aSide.ordinal(), false);
	}

	@Override
	public boolean isFlammable(final IBlockAccess aWorld, final int aX, final int aY, final int aZ,
			final ForgeDirection aSide) {
		return EBlock.getSidedArrayReturn(def.flammable, aSide.ordinal(),
				getFlammability(aWorld, aX, aY, aZ, aSide) > 0);
	}

	@Override
	public boolean isNormalCube(final IBlockAccess aWorld, final int aX, final int aY, final int aZ) {
		return def.normalCube;
	}

	@Override
	public boolean isOpaqueCube() {
		return def.opaqueCube;
	}

	@Override
	public boolean isSideSolid(final IBlockAccess aWorld, final int aX, final int aY, final int aZ,
			final ForgeDirection aSide) {
		return EBlock.getSidedArrayReturn(def.sideSolid, aSide.ordinal(), true);
	}

	@Override
	public boolean isToolEffective(final String aType, final int aMeta) {
		for (final String type : def.harvestTools) {
			if (type.equals(aType)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int quantityDropped(final Random par1Random) {
		final int amt = def.dropped_base + (def.dropped_randomMax <= 0 ? 0 : par1Random.nextInt(def.dropped_randomMax));
		return amt <= 0 ? 0 : amt;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return def.renderAsNormalBlock; // TODO: Change this to determine what renderer is used
	}

	@Override
	public boolean isAir(final IBlockAccess world, final int x, final int y, final int z) {
		return def.isAir;
	}
}