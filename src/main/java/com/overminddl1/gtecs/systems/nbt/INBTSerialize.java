package com.overminddl1.gtecs.systems.nbt;

import net.minecraft.nbt.NBTBase;

public interface INBTSerialize {
	public NBTBase toNBT();

	public void fromNBT(Object self, NBTBase nbt);
}
