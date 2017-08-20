package com.overminddl1.gtecs;

import java.util.List;
import java.util.Random;

import com.overminddl1.gtecs.components.MCBlock;

import cpw.mods.fml.common.registry.GameRegistry;
import gregapi.block.IBlockOnWalkOver;
import gregapi.data.CS;
import gregapi.data.LH;
import gregapi.data.OP;
import gregapi.util.ST;
import gregapi.util.UT;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStaticLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
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
	// Bloody hell this would have been easier, why is the constructor not public?!?
	// public final FMLControlledNamespacedRegistry<Archetype>
	// instanceBuilderRegistry;
	// public final Hashtable<String, Archetype> instanceBuilderRegistry;

	public EBlock(final com.artemis.World ecs, final int entityID, final MCBlock def, final String name) {
		super(new EMaterial(def));
		this.ecs = ecs;
		this.entityID = entityID;
		this.def = def;
		setStepSound(new SoundType(def.sound_name, def.sound_volume, def.sound_frequency));
		setBlockName(name);
		GameRegistry.registerBlock(this, EItemBlock.class, getUnlocalizedName());
		LH.add(getUnlocalizedName() + "." + CS.W + ".name", "Any Sub-Block of this one");
		// if (def.instanceable) {
		// instanceBuilderRegistry = new Hashtable<String, Archetype>();
		// }
	}

	public void addInformation(final ItemStack aStack, final int aMeta, final EntityPlayer aPlayer, final List aList,
			final boolean aF3_H) {
		/**/}

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
		return CS.F;
	}

	@Override
	public boolean canCreatureSpawn(final EnumCreatureType type, final IBlockAccess aWorld, final int aX, final int aY,
			final int aZ) {
		return canCreatureSpawn(aWorld.getBlockMetadata(aX, aY, aZ));
	}

	public boolean canCreatureSpawn(final int aMeta) {
		return CS.F;
	}

	@Override
	public boolean canSilkHarvest() {
		return CS.T;
	}

	@Override
	public ItemStack createStackedBlock(final int aMeta) {
		return ST.make(this, 1, damageDropped(aMeta));
	}

	@Override
	public int damageDropped(final int aMeta) {
		return aMeta;
	}

	public boolean doesWalkSpeed(final short aMeta) {
		return this instanceof IBlockOnWalkOver;
	}

	@Override
	public int getDamageValue(final World aWorld, final int aX, final int aY, final int aZ) {
		return aWorld.getBlockMetadata(aX, aY, aZ);
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
		return 10.0F;
	}

	@Override
	public int getFireSpreadSpeed(final IBlockAccess aWorld, final int aX, final int aY, final int aZ,
			final ForgeDirection aSide) {
		return 0;
	}

	@Override
	public int getFlammability(final IBlockAccess aWorld, final int aX, final int aY, final int aZ,
			final ForgeDirection aSide) {
		return 0;
	}

	@Override
	public int getHarvestLevel(final int aMeta) {
		return 0;
	}

	@Override
	public String getHarvestTool(final int aMeta) {
		return CS.TOOL_pickaxe;
	}

	@Override
	public Item getItem(final World aWorld, final int aX, final int aY, final int aZ) {
		return Item.getItemFromBlock(this);
	}

	@Override
	public Item getItemDropped(final int aMeta, final Random aRandom, final int aFortune) {
		return Item.getItemFromBlock(this);
	}

	public int getItemStackLimit(final ItemStack aStack) {
		return UT.Code.bindStack(OP.block.mDefaultStackSize);
	}

	@Override
	public int getLightOpacity() {
		return CS.LIGHT_OPACITY_MAX;
	}

	@Override
	public String getLocalizedName() {
		return StatCollector.translateToLocal(getUnlocalizedName() + ".name");
	}

	@Override
	public Material getMaterial() {
		return blockMaterial.getCanBurn() && !isFlammable(null, 0, 0, 0, CS.FORGE_DIR[CS.SIDE_ANY])
				&& (new Throwable().getStackTrace()[1].getClassName()) == BlockStaticLiquid.class.getName()
						? Material.iron
						: blockMaterial;
	}

	@Override
	public final String getUnlocalizedName() {
		return def.name;
	}

	@Override
	public boolean isFireSource(final World aWorld, final int aX, final int aY, final int aZ,
			final ForgeDirection aSide) {
		return CS.F;
	}

	@Override
	public boolean isFlammable(final IBlockAccess aWorld, final int aX, final int aY, final int aZ,
			final ForgeDirection aSide) {
		return CS.F;
	}

	@Override
	public boolean isNormalCube(final IBlockAccess aWorld, final int aX, final int aY, final int aZ) {
		return CS.T;
	}

	@Override
	public boolean isOpaqueCube() {
		return CS.T;
	}

	@Override
	public boolean isSideSolid(final IBlockAccess aWorld, final int aX, final int aY, final int aZ,
			final ForgeDirection aDirection) {
		return CS.T;
	}

	@Override
	public boolean isToolEffective(final String aType, final int aMeta) {
		return getHarvestTool(aMeta).equals(aType);
	}

	public ItemStack onItemRightClick(final ItemStack aStack, final World aWorld, final EntityPlayer aPlayer) {
		return aStack;
	}

	@Override
	public int quantityDropped(final Random par1Random) {
		return 1;
	}

	@Override
	public void registerBlockIcons(final IIconRegister aIconRegister) {
		/**/}

	@Override
	public boolean renderAsNormalBlock() {
		return CS.T;
	}
}