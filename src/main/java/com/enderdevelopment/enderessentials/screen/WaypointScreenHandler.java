package com.enderdevelopment.enderessentials.screen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;

/**
 * Waypoint GUI screen handler - a simple container for listing waypoints.
 * Implements NamedScreenHandlerFactory so it can be opened via commands.
 */
public class WaypointScreenHandler extends ScreenHandler implements NamedScreenHandlerFactory {

	protected WaypointScreenHandler(int syncId, PlayerInventory playerInventory) {
		super(null, syncId);
	}

	public static WaypointScreenHandler create(int syncId, PlayerInventory playerInventory) {
		return new WaypointScreenHandler(syncId, playerInventory);
	}

	@Override
	public boolean canUse(PlayerEntity player) {
		return true;
	}

	@Override
	public Text getDisplayName() {
		return Text.translatable("screen.ender_essentials.waypoint");
	}

	@Override
	public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
		return new WaypointScreenHandler(syncId, inv);
	}
}
