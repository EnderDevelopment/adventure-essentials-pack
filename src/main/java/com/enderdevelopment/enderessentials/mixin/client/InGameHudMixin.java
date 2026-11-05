package com.enderdevelopment.enderessentials.mixin.client;

import com.enderdevelopment.enderessentials.network.ClientWaypointData;
import com.enderdevelopment.enderessentials.waypoint.Waypoint;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

/**
 * Renders waypoint indicators on the HUD overlay.
 * Shows the distance and direction to each nearby waypoint.
 */
@Mixin(InGameHud.class)
public class InGameHudMixin {

	@Inject(method = "render", at = @At("RETURN"))
	private void renderWaypoints(net.minecraft.client.util.math.MatrixStack matrices, float tickDelta, CallbackInfo ci) {
		MinecraftClient client = MinecraftClient.getInstance();
		ClientPlayerEntity player = client.player;
		if (player == null) return;

		List<Waypoint> waypoints = ClientWaypointData.getWaypoints();
		if (waypoints.isEmpty()) return;

		DrawContext context = new DrawContext(client, client.getBufferBuilders().getEntityVertexConsumers());
		int y = 40;

		for (Waypoint wp : waypoints) {
			// Only show waypoints in the same dimension
			if (!wp.dimension().equals(player.getWorld().getRegistryKey().getValue().toString())) {
				continue;
			}

			Vec3d current = player.getPos();
			double dx = wp.x() - current.x;
			double dy = wp.y() - current.y;
			double dz = wp.z() - current.z;
			double distance = Math.sqrt(dx * dx + dz * dz);

			String text = wp.name() + "  " + String.format("%.0f", distance) + "m";
			context.drawText(client.textRenderer, Text.literal(text),
					10, y, 0xFFFFFF, true);
			y += 10;
		}
	}
}
