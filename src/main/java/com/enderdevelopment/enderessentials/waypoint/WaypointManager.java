package com.enderdevelopment.enderessentials.waypoint;

import com.enderdevelopment.enderessentials.EnderEssentials;
import com.enderdevelopment.enderessentials.network.WaypointNetwork;
import com.enderdevelopment.enderessentials.screen.WaypointScreenHandler;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.argument.StringArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

/**
 * Manages player waypoints with persistence and server-client sync.
 */
public class WaypointManager {
	private static final Map<UUID, Map<String, Waypoint>> waypointsByPlayer = new ConcurrentHashMap<>();

	public static void registerCommands() {
		EnderEssentials.LOGGER.info("Waypoint system ready");
	}

	public static void registerWaypointCommands(CommandDispatcher<ServerCommandSource> dispatcher) {
		dispatcher.register(literal("waypoint")
				.then(literal("add")
						.then(argument("name", StringArgumentType.word())
								.executes(ctx -> {
									String name = StringArgumentType.getString(ctx, "name");
									ServerPlayerEntity player = ctx.getSource().getPlayer();
									Vec3d pos = player.getPos();
									World world = player.getWorld();
									addWaypoint(player.getUuid(), name,
											new Waypoint(name, pos, world.getRegistryKey().getValue().toString()));
									WaypointNetwork.syncToPlayer(player);
									ctx.getSource().sendFeedback(() -> Text.literal(
											"Waypoint \"" + name + "\" added at your location"), false);
									return 1;
								})))
				.then(literal("remove")
						.then(argument("name", StringArgumentType.word())
								.executes(ctx -> {
									String name = StringArgumentType.getString(ctx, "name");
									ServerPlayerEntity player = ctx.getSource().getPlayer();
									if (removeWaypoint(player.getUuid(), name)) {
										WaypointNetwork.syncToPlayer(player);
										ctx.getSource().sendFeedback(() -> Text.literal(
												"Waypoint \"" + name + "\" removed"), false);
									} else {
										ctx.getSource().sendError(Text.literal(
												"No waypoint named \"" + name + "\""));
									}
									return 1;
								})))
				.then(literal("list")
						.executes(ctx -> {
							ServerPlayerEntity player = ctx.getSource().getPlayer();
							Map<String, Waypoint> waypoints = getWaypoints(player.getUuid());
							if (waypoints.isEmpty()) {
								ctx.getSource().sendFeedback(() -> Text.literal(
										"You have no waypoints. Use /waypoint add <name>"), false);
							} else {
								ctx.getSource().sendFeedback(() -> Text.literal("Your waypoints:"), false);
								waypoints.values().forEach(wp ->
										ctx.getSource().sendFeedback(() -> Text.literal("  - " + wp.name()
												+ " at " + (int) wp.x() + ", " + (int) wp.y() + ", " + (int) wp.z()), false));
							}
							return 1;
						}))
				.then(literal("teleport")
						.then(argument("name", StringArgumentType.word())
								.executes(ctx -> {
									String name = StringArgumentType.getString(ctx, "name");
									ServerPlayerEntity player = ctx.getSource().getPlayer();
									Waypoint wp = getWaypoint(player.getUuid(), name);
									if (wp != null) {
										player.teleport((ServerWorld) player.getWorld(), wp.x(), wp.y(), wp.z(),
												player.getYaw(), player.getPitch());
										ctx.getSource().sendFeedback(() -> Text.literal(
												"Teleported to \"" + name + "\""), false);
									} else {
										ctx.getSource().sendError(Text.literal(
												"No waypoint named \"" + name + "\""));
									}
									return 1;
								})))
				.then(literal("gui")
						.executes(ctx -> {
							ServerPlayerEntity player = ctx.getSource().getPlayer();
							player.openHandledScreen(new WaypointScreenHandler(0, player.getInventory()));
							return 1;
						})));
	}

	public static void addWaypoint(UUID playerId, String name, Waypoint waypoint) {
		waypointsByPlayer.computeIfAbsent(playerId, k -> new ConcurrentHashMap<>()).put(name, waypoint);
	}

	public static boolean removeWaypoint(UUID playerId, String name) {
		Map<String, Waypoint> map = waypointsByPlayer.get(playerId);
		return map != null && map.remove(name) != null;
	}

	public static Waypoint getWaypoint(UUID playerId, String name) {
		Map<String, Waypoint> map = waypointsByPlayer.get(playerId);
		return map != null ? map.get(name) : null;
	}

	public static Map<String, Waypoint> getWaypoints(UUID playerId) {
		return waypointsByPlayer.getOrDefault(playerId, Collections.emptyMap());
	}
}
