package com.overminddl1.gtecs;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * @author OvermindDL1
 *
 *         TickHandler Information:
 *         https://github.com/coolAlias/Forge_Tutorials/blob/master/EventHandlerTutorial.java#L378-L384
 */
@SideOnly(Side.SERVER)
public final class ProxyServer extends ProxyBase {
	@SubscribeEvent
	public void onServerTick(final TickEvent.ServerTickEvent event) {
		onTick(event);
	}
}