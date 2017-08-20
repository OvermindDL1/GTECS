package com.overminddl1.gtecs;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * @author OvermindDL1
 */
@SideOnly(Side.CLIENT)
public final class ProxyClient extends ProxyBase {
	@SubscribeEvent
	public void onClientTick(final TickEvent.ClientTickEvent event) {
		onTick(event);
	}
}