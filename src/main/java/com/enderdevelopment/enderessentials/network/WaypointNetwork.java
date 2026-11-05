package com.enderdevelopment.enderessentials.network;

import com.enderdevelopment.enderessentials.EnderEssentials;
import com.enderdevelopment.enderessentials.waypoint.Waypoint;
import com.enderdevelopment.enderessentials.waypoint.WaypointManager;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Handles network synchronisation of waypoints between server and client.
 */
public class WaypointNetwork {

	public record WaypointSyncPayload(List<Waypoint> waypoints) implements CustomPayload {
		public static final CustomPayload.Id<WaypointSyncPayload> ID =
				new CustomPayload.Id<>(EnderEssentials.id("waypoint_sync"));

		public static final PacketCodec<PacketByteBuf, WaypointSyncPayload> CODEC =
				PacketCodec.of(
						(payload, buf) -> {
							buf.writeVarInt(payload.waypoints().size());
							for (Waypoint wp : payload.waypoints()) {
								buf.writeString(wp.name());
								buf.writeDouble(wp.x());
								buf.writeDouble(wp.y());
								buf.writeDouble(wp.z());
								buf.writeString(wp.dimension());
							}
						},
						buf -> {
							int size = buf.readVarInt();
							List<Waypoint> list = new ArrayList<>();
							for (int i = 0; i < size; i++) {
								String name = buf.readString();
								double x = buf.readDouble();
								double y = buf.readDouble();
								double z = buf.readDouble();
								String dim = buf.readString();
								list.add(new Waypoint(name, x, y, z, dim));
							}
							return new WaypointSyncPayload(list);
						}
				);

		@Override
		public Id<? extends CustomPayload> getId() {
			return ID;
		}
	}

	public static void registerPayloadTypes() {
		PayloadTypeRegistry.playS2C().register(WaypointSyncPayload.ID, WaypointSyncPayload.CODEC);
	}

	public static void registerServerReceivers() {
		// Sync waypoints when a player joins the server
		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			ServerPlayerEntity player = handler.getPlayer();
			server.execute(() -> syncToPlayer(player));
		});
	}

	public static void registerClientReceivers() {
		ClientPlayNetworking.registerGlobalReceiver(WaypointSyncPayload.ID,
				(payload, context) -> {
					context.client().execute(() -> {
						ClientWaypointData.setWaypoints(payload.waypoints());
					});
				});
	}

	public static void syncToPlayer(ServerPlayerEntity player) {
		Map<String, Waypoint> waypoints = WaypointManager.getWaypoints(player.getUuid());
		WaypointSyncPayload payload = new WaypointSyncPayload(new ArrayList<>(waypoints.values()));
		ServerPlayNetworking.send(player, payload);
	}
}
