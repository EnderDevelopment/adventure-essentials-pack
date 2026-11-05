package com.enderdevelopment.enderessentials.screen;

import com.enderdevelopment.enderessentials.EnderEssentials;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerType;

/**
 * Registers screen handlers for mod GUIs.
 */
public class ModScreenHandlers {
	public static final ScreenHandlerType<WaypointScreenHandler> WAYPOINT_SCREEN_HANDLER =
			ScreenHandlerRegistry.registerSimple(EnderEssentials.id("waypoint"),
					WaypointScreenHandler::create);

	public static void register() {
		// The screen handler is registered lazily by the ScreenHandlerRegistry call above.
		EnderEssentials.LOGGER.info("Registered screen handlers");
	}
}
