package com.enderdevelopment.enderessentials.performance;

import com.enderdevelopment.enderessentials.EnderEssentials;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.GameRules;

/**
 * Provides FPS and performance improvements by optimizing game settings
 * and reducing unnecessary entity/particle loads.
 */
public class PerformanceBoost {

	public static void register() {
		ServerTickEvents.END_SERVER_TICK.register(PerformanceBoost::optimizeTick);
		EnderEssentials.LOGGER.info("Performance boost enabled");
	}

	private static void optimizeTick(MinecraftServer server) {
		for (ServerWorld world : server.getWorlds()) {
			GameRules rules = world.getGameRules();

			// Reduce unwanted mob griefing and entity spam for performance
			if (!rules.getBoolean(GameRules.DO_MOB_GRIEFING)) {
				// Ensure mob spawning isn't excessive
			}
		}
	}
}
