package com.overminddl1.gtecs;

import java.util.Random;

import com.artemis.ComponentMapper;
import com.overminddl1.gtecs.components.MCBlock;
import com.overminddl1.gtecs.components.MCBlockInstance;
import com.overminddl1.gtecs.systems.IconLoaderSystem;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * @author OvermindDL1
 */
public class EBlockInst extends EBlock {
	// MCBlock fields that cannot be acquired from the TE:
	// name
	// opacity
	// silkHarvestable
	// harvestLevel
	// harvestTool
	// opaqueCube
	// harvestTools
	// dropped_base
	// dropped_randomMax
	// renderAsNormalBlock
	// MCBlock fields that are sometimes accessed:
	// explosionResistance

	protected ComponentMapper<MCBlockInstance> blockInstances;
	protected IconLoaderSystem iconSystem;

	public EBlockInst(final com.artemis.World ecs, final int entityID, final MCBlock def, final String name) {
		super(ecs, entityID, def, name);
		// if (def.instanceable) {
		// instanceBuilderRegistry = new Hashtable<String, Archetype>();
		// }
	}

	public static ETileEntity getETileEntityAt(final IBlockAccess worldAccess, final int x, final int y, final int z) {
		return (ETileEntity) worldAccess.getTileEntity(x, y, z);
	}

	public static int getBlockEntity(final IBlockAccess worldAccess, final int x, final int y, final int z) {
		final TileEntity te = worldAccess.getTileEntity(x, y, z);
		if (te instanceof ETileEntity) {
			return ((ETileEntity) te).getEntityId();
		}
		// TODO: Else check the Octree system or so...
		return -1;
	}

	public MCBlockInstance getBlockInstanceComponent(final IBlockAccess worldAccess, final int x, final int y,
			final int z) {
		final int entityId = EBlockInst.getBlockEntity(worldAccess, x, y, z);
		if (entityId < 0) {
			return null;
		}
		return blockInstances.get(entityId);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(final IBlockAccess worldAccess, final int x, final int y, final int z, final int side) {
		final MCBlockInstance inst = getBlockInstanceComponent(worldAccess, x, y, z);
		if (inst == null) {
			return super.getIcon(worldAccess, x, y, z, side);
		}
		final String iconName = EBlock.getSidedArrayReturn(inst.textures, side, null);
		return iconSystem.getIcon(iconName); // TODO: Cache this somewhere so a hashmap lookup is not needed every
												// single time... Look at AssetManager...
	}

	@Override
	public boolean hasTileEntity(final int metadata) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(final World world, final int metadata) {
		return new ETileEntity();
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
	public int getDamageValue(final World aWorld, final int aX, final int aY, final int aZ) {
		return aWorld.getBlockMetadata(aX, aY, aZ); // TODO: Maybe return 0, move this into the subclasses?
	}

	@Override
	public float getExplosionResistance(final Entity aEntity, final World aWorld, final int aX, final int aY,
			final int aZ, final double eX, final double eY, final double eZ) {
		return getExplosionResistance(aWorld.getBlockMetadata(aX, aY, aZ));
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
	public Item getItem(final World aWorld, final int aX, final int aY, final int aZ) {
		return Item.getItemFromBlock(this); // Get item based on component data
	}

	@Override
	public Item getItemDropped(final int aMeta, final Random aRandom, final int aFortune) {
		return null;
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
	public boolean isSideSolid(final IBlockAccess aWorld, final int aX, final int aY, final int aZ,
			final ForgeDirection aSide) {
		return EBlock.getSidedArrayReturn(def.sideSolid, aSide.ordinal(), true);
	}

	@Override
	public boolean isAir(final IBlockAccess world, final int x, final int y, final int z) {
		return def.isAir;
	}
}