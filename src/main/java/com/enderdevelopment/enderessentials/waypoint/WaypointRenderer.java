package com.enderdevelopment.enderessentials.waypoint;

import com.enderdevelopment.enderessentials.network.ClientWaypointData;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexRendering;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.List;

/**
 * Renders waypoint markers in the world space as glowing boxes.
 */
public class WaypointRenderer {

	public static void register() {
		WorldRenderEvents.AFTER_TRANSLUCENT.register((context) -> {
			MinecraftClient client = MinecraftClient.getInstance();
			ClientPlayerEntity player = client.player;
			if (player == null) return;

			List<Waypoint> waypoints = ClientWaypointData.getWaypoints();
			if (waypoints.isEmpty()) return;

			MatrixStack matrices = context.matrixStack();
			VertexConsumerProvider consumers = context.consumers();
			Vec3d camera = context.camera().getPos();

			matrices.push();
			matrices.translate(-camera.x, -camera.y, -camera.z);

			for (Waypoint wp : waypoints) {
				// Only render waypoints in the current dimension
				if (!wp.dimension().equals(player.getWorld().getRegistryKey().getValue().toString())) {
					continue;
				}

				Box box = new Box(wp.x() - 0.5, wp.y() - 0.5, wp.z() - 0.5,
						wp.x() + 0.5, wp.y() + 0.5, wp.z() + 0.5);
				VertexConsumer consumer = consumers.getBuffer(RenderLayer.getLines());
				VertexRendering.drawBox(matrices, consumer, box, 0.0f, 1.0f, 0.2f, 1.0f);
			}

			matrices.pop();
		});
	}
}
