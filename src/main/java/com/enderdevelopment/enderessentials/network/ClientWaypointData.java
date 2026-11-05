package com.enderdevelopment.enderessentials.network;

import com.enderdevelopment.enderessentials.waypoint.Waypoint;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores the client-side copy of waypoints for HUD rendering.
 */
public class ClientWaypointData {
	private static List<Waypoint> waypoints = new ArrayList<>();

	public static void setWaypoints(List<Waypoint> newWaypoints) {
		waypoints = new ArrayList<>(newWaypoints);
	}

	public static List<Waypoint> getWaypoints() {
		return waypoints;
	}
}
