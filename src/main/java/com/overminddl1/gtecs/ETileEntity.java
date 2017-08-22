package com.overminddl1.gtecs;

import net.minecraft.tileentity.TileEntity;

public class ETileEntity extends TileEntity {// implements ITileEntity, ITileEntityGUI {
	private int entityId = -1;

	public void setEntityId(final int entityId) {
		this.entityId = entityId;
	}

	public int getEntityId() {
		return entityId;
	}
}
