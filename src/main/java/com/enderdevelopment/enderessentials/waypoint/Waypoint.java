package com.enderdevelopment.enderessentials.waypoint;

/**
 * Immutable record representing a waypoint location.
 */
public record Waypoint(String name, double x, double y, double z, String dimension) {
	public Waypoint(String name, net.minecraft.util.math.Vec3d pos, String dimension) {
		this(name, pos.x, pos.y, pos.z, dimension);
	}
}
