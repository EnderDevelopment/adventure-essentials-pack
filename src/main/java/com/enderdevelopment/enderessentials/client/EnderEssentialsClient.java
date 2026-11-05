package com.enderdevelopment.enderessentials.client;

import com.enderdevelopment.enderessentials.EnderEssentials;
import com.enderdevelopment.enderessentials.mob.ModMobs;
import com.enderdevelopment.enderessentials.mob.PetWolfRenderer;
import com.enderdevelopment.enderessentials.mob.WarriorBeastRenderer;
import com.enderdevelopment.enderessentials.network.WaypointNetwork;
import com.enderdevelopment.enderessentials.screen.ModScreenHandlers;
import com.enderdevelopment.enderessentials.screen.WaypointScreen;
import com.enderdevelopment.enderessentials.waypoint.WaypointRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class EnderEssentialsClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// Register entity renderers
		EntityRendererRegistry.register(ModMobs.WARRIOR_BEAST, WarriorBeastRenderer::new);
		EntityRendererRegistry.register(ModMobs.PET_WOLF, PetWolfRenderer::new);

		// Register waypoint networking receivers
		WaypointNetwork.registerClientReceivers();

		// Register screen handlers
		HandledScreens.register(ModScreenHandlers.WAYPOINT_SCREEN_HANDLER,
				WaypointScreen::new);

		// Register waypoint rendering
		WaypointRenderer.register();
	}
}
