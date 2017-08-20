package com.overminddl1.gtecs;

import gregapi.item.IItemColorableRGB;
import gregapi.item.IItemGT;
import gregapi.item.IItemNoGTOverride;
import gregapi.item.IItemUpdatable;
import gregapi.oredict.IOreDictItemDataOverrideItem;
import gregapi.oredict.OreDictItemData;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;
import squeek.applecore.api.food.FoodValues;

public class EItemBlock extends ItemBlock implements squeek.applecore.api.food.IEdible, IItemUpdatable,
		IItemColorableRGB, IOreDictItemDataOverrideItem, IItemGT, IItemNoGTOverride, IFluidContainerItem {
	// private final MCBlock def;
	private final EBlock block;

	public EItemBlock(final Block block/* , final MCBlock def */) {
		super(block);
		// this.def = def;
		this.block = (EBlock) block;
		setMaxDamage(0);
		setHasSubtypes(true);
	}

	@Override
	public String getItemStackDisplayName(final ItemStack aStack) {
		// final MultiTileEntityContainer tTileEntityContainer =
		// mBlock.mMultiTileEntityRegistry.getNewTileEntityContainer(aStack);
		// if (tTileEntityContainer != null && tTileEntityContainer.mTileEntity
		// instanceof IMTE_GetItemName) {
		// return ((IMTE_GetItemName)
		// tTileEntityContainer.mTileEntity).getItemName(aStack,
		// super.getItemStackDisplayName(aStack));
		// }
		return super.getItemStackDisplayName(aStack);
	}

	// Look at `MultiTileEntityItemInternal` to finish this

	@Override
	public FluidStack getFluid(final ItemStack container) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getCapacity(final ItemStack container) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int fill(final ItemStack container, final FluidStack resource, final boolean doFill) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public FluidStack drain(final ItemStack container, final int maxDrain, final boolean doDrain) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OreDictItemData getOreDictItemData(final ItemStack aStack) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canRecolorItem(final ItemStack aStack) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean recolorItem(final ItemStack aStack, final int aRGB) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canDecolorItem(final ItemStack aStack) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean decolorItem(final ItemStack aStack) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void updateItemStack(final ItemStack aStack) {
	}

	@Override
	public FoodValues getFoodValues(final ItemStack itemStack) {
		// TODO Auto-generated method stub
		return null;
	}
}
