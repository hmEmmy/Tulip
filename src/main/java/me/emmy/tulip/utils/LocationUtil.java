package me.emmy.tulip.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

/**
 * Utility class for locations
 * Author: no idea
 */
@UtilityClass
public class LocationUtil {
	/**
	 * Get the faces of a location
	 *
	 * @param start the location to get the faces of
	 * @return the faces of the location
	 */
	public Location[] getFaces(Location start) {
		Location[] faces = new Location[4];
		faces[0] = new Location(start.getWorld(), start.getX() + 1, start.getY(), start.getZ());
		faces[1] = new Location(start.getWorld(), start.getX() - 1, start.getY(), start.getZ());
		faces[2] = new Location(start.getWorld(), start.getX(), start.getY() + 1, start.getZ());
		faces[3] = new Location(start.getWorld(), start.getX(), start.getY() - 1, start.getZ());
		return faces;
	}

	/**
	 * Serialize a location to a string
	 *
	 * @param location the location to serialize
	 * @return the serialized location
	 */
	public String serialize(Location location) {
		if (location == null) {
			return "null";
		}

		return location.getWorld().getName() + ":" + location.getX() + ":" + location.getY() + ":" + location.getZ() +
		       ":" + location.getYaw() + ":" + location.getPitch();
	}

	/**
	 * Deserialize a location from a string
	 *
	 * @param source the string to deserialize
	 * @return the deserialized location
	 */
	public Location deserialize(String source) {
		if (source == null || source.equalsIgnoreCase("null")) {
			return null;
		}

		String[] split = source.split(":");
		World world = Bukkit.getServer().getWorld(split[0]);

		if (world == null) {
			return null;
		}

		return new Location(world, Double.parseDouble(split[1]), Double.parseDouble(split[2]),
				Double.parseDouble(split[3]), Float.parseFloat(split[4]), Float.parseFloat(split[5]));
	}

}