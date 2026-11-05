package com.enderdevelopment.enderessentials;

import com.enderdevelopment.enderessentials.command.ModCommands;
import com.enderdevelopment.enderessentials.item.ModItems;
import com.enderdevelopment.enderessentials.mob.ModMobs;
import com.enderdevelopment.enderessentials.network.WaypointNetwork;
import com.enderdevelopment.enderessentials.performance.PerformanceBoost;
import com.enderdevelopment.enderessentials.screen.ModScreenHandlers;
import com.enderdevelopment.enderessentials.structure.ModStructures;
import com.enderdevelopment.enderessentials.waypoint.WaypointManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnderEssentials implements ModInitializer {
	public static final String MOD_ID = "ender_essentials";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Ender Essentials initializing...");

		ModMobs.register();
		ModItems.register();
		ModStructures.register();
		ModScreenHandlers.register();
		PerformanceBoost.register();
		WaypointNetwork.registerPayloadTypes();
		WaypointNetwork.registerServerReceivers();
		WaypointManager.registerCommands();

		// Register game rules for mod features
		registerGameRules();

		LOGGER.info("Ender Essentials loaded successfully!");
	}

	private void registerGameRules() {
		// Convenience game rules are set through commands
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			ModCommands.register(dispatcher);
			WaypointManager.registerWaypointCommands(dispatcher);
		});
	}

	public static Identifier id(String path) {
		return new Identifier(MOD_ID, path);
	}
}
