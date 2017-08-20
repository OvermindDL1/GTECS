package com.overminddl1.gtecs;

import java.io.InputStream;

import com.artemis.Archetype;
import com.artemis.ArchetypeBuilder;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.artemis.WorldConfigurationBuilder.Priority;
import com.artemis.io.JsonArtemisSerializer;
import com.artemis.io.SaveFileFormat;
import com.artemis.managers.WorldSerializationManager;
import com.overminddl1.gtecs.components.MCBlock;
import com.overminddl1.gtecs.components.PrintDebug;
import com.overminddl1.gtecs.systems.MCBlockRegistrationSystem;
import com.overminddl1.gtecs.systems.NBTSerializationManager;
import com.overminddl1.gtecs.systems.PrintDebugSystem;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import gregapi.api.Abstract_Mod;
import gregapi.api.Abstract_Proxy;

public class ProxyBase extends Abstract_Proxy {
	public WorldConfigurationBuilder ecsBuilder;
	public com.artemis.World ecs;
	public SystemInvoker strategy;
	public WorldSerializationManager jsonSerializationManager;
	public NBTSerializationManager nbtSerializationManager;
	public MCBlockRegistrationSystem blockRegistrationSystem;

	// Helpers
	public Archetype blueprintBlockBasic;

	public void loadEntitiesFromResource(final String resource_path) {
	}

	@Override
	public void onProxyAfterInit(final Abstract_Mod aMod, final FMLInitializationEvent aEvent) {
		// final int blockTest = ecs.create(blueprintBlockBasic);
		final InputStream is = this.getClass().getResourceAsStream("/gcecs/blocks.json");
		jsonSerializationManager.load(is, SaveFileFormat.class);

		processECS(SystemInvoker.STATE_INIT, 0.0f);
	}

	@Override
	public void onProxyBeforeInit(final Abstract_Mod aMod, final FMLInitializationEvent aEvent) {
		blockRegistrationSystem = new MCBlockRegistrationSystem();
		ecsBuilder.with(Priority.LOW, blockRegistrationSystem);
		final WorldConfiguration config = ecsBuilder.build();

		ecs = new com.artemis.World(config);
		jsonSerializationManager.setSerializer(new JsonArtemisSerializer(ecs));

		// Setup Archetypes/Blueprints
		blueprintBlockBasic = new ArchetypeBuilder().add(PrintDebug.class).add(MCBlock.class).build(ecs);
	}

	@Override
	public void onProxyBeforePreInit(final Abstract_Mod aMod, final FMLPreInitializationEvent aEvent) {
		FMLCommonHandler.instance().bus().register(this);

		ecsBuilder = new WorldConfigurationBuilder();

		strategy = new SystemInvoker();
		ecsBuilder.register(strategy);

		jsonSerializationManager = new WorldSerializationManager();
		ecsBuilder.with(jsonSerializationManager);

		nbtSerializationManager = new NBTSerializationManager();
		ecsBuilder.with(nbtSerializationManager);

		// Systems: Low
		ecsBuilder.with(Priority.LOWEST, new PrintDebugSystem(jsonSerializationManager));
	}

	final public void onTick(final TickEvent event) {
		processECS(SystemInvoker.STATE_TICK, 0.05f); // Since Minecraft operates at 20 ticks per second, or 0.05, we
														// don't really use this though regardless...
	}

	final public void processECS(final int state, final float delta) {
		strategy.state = state;
		ecs.setDelta(delta);
		ecs.process();
	}
}
