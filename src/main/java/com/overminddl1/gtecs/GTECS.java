package com.overminddl1.gtecs;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import gregapi.api.Abstract_Mod;
import gregapi.api.Abstract_Proxy;

/**
 * @author OvermindDL1
 *
 *         Note: it is important to load after "gregapi_post".
 */
@Mod(modid = GTECS.MOD_ID, name = GTECS.MOD_NAME, version = GTECS.VERSION, dependencies = "required-after:gregapi_post")
public final class GTECS extends Abstract_Mod {
	public static final String MOD_ID = "gtecs";
	public static final String MOD_NAME = "GTECS";
	public static final String MOD_LOG_NAME = "GTECS";
	public static final String VERSION = "0.0.1";

	@SidedProxy(modId = GTECS.MOD_ID, clientSide = "com.overminddl1.gtecs.ProxyClient", serverSide = "com.overminddl1.gtecs.ProxyServer")
	public static Abstract_Proxy PROXY;

	@Override
	public String getModID() {
		return GTECS.MOD_ID;
	}

	@Override
	public String getModName() {
		return GTECS.MOD_NAME;
	}

	@Override
	public String getModNameForLog() {
		return GTECS.MOD_LOG_NAME;
	}

	@Override
	public Abstract_Proxy getProxy() {
		return GTECS.PROXY;
	}

	// Main Class Start

	@Mod.EventHandler
	public final void onLoad(final FMLInitializationEvent aEvent) {
		onModInit(aEvent);
	}

	@Override
	public void onModInit2(final FMLInitializationEvent aEvent) {
	}

	@Override
	public void onModPostInit2(final FMLPostInitializationEvent aEvent) {
		// Insert your PostInit Code here and not above
	}

	@Override
	public void onModPreInit2(final FMLPreInitializationEvent aEvent) {
	}

	@Override
	public void onModServerStarted2(final FMLServerStartedEvent aEvent) {
		// Insert your ServerStarted Code here and not above
	}

	@Override
	public void onModServerStarting2(final FMLServerStartingEvent aEvent) {
		// Insert your ServerStarting Code here and not above
	}

	@Override
	public void onModServerStopped2(final FMLServerStoppedEvent aEvent) {
		// Insert your ServerStopped Code here and not above
	}

	@Override
	public void onModServerStopping2(final FMLServerStoppingEvent aEvent) {
		// Insert your ServerStopping Code here and not above
	}

	// Mod event overrides
	// Do not change these 7 Functions. Just keep them this way.

	@Mod.EventHandler
	public final void onPostLoad(final FMLPostInitializationEvent aEvent) {
		onModPostInit(aEvent);
	}

	@Mod.EventHandler
	public final void onPreLoad(final FMLPreInitializationEvent aEvent) {
		onModPreInit(aEvent);
	}

	@Mod.EventHandler
	public final void onServerStarted(final FMLServerStartedEvent aEvent) {
		onModServerStarted(aEvent);
	}

	@Mod.EventHandler
	public final void onServerStarting(final FMLServerStartingEvent aEvent) {
		onModServerStarting(aEvent);
	}

	@Mod.EventHandler
	public final void onServerStopped(final FMLServerStoppedEvent aEvent) {
		onModServerStopped(aEvent);
	}

	@Mod.EventHandler
	public final void onServerStopping(final FMLServerStoppingEvent aEvent) {
		onModServerStopping(aEvent);
	}
}
