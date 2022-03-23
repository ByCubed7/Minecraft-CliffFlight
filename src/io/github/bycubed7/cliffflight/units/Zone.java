package io.github.bycubed7.cliffflight.units;

import org.bukkit.Location;

import io.github.bycubed7.corecubes.unit.Vector3Int;

public class Zone {
	public Vector3Int minPosition;
	public Vector3Int maxPosition;
	public String worldName;
	
	public Zone(Vector3Int _min, Vector3Int _max, String _worldName) {
		minPosition = _min;
		maxPosition = _max;
		worldName = _worldName;
	}
	
	public static Zone FromLocation(Location starting, Location ending) {
		Vector3Int minPosition = new Vector3Int(0, 0, 0);
		Vector3Int maxPosition = new Vector3Int(0, 0, 0);
		// Temperary assignment
		String worldName = starting.getWorld().getName();
		minPosition.x = Math.min(starting.getBlockX(), ending.getBlockX());
		minPosition.y = Math.min(starting.getBlockY(), ending.getBlockY());
		minPosition.z = Math.min(starting.getBlockZ(), ending.getBlockZ());				
		maxPosition.x = Math.max(starting.getBlockX(), ending.getBlockX());
		maxPosition.y = Math.max(starting.getBlockY(), ending.getBlockY());
		maxPosition.z = Math.max(starting.getBlockZ(), ending.getBlockZ());
		
		return new Zone(minPosition, maxPosition, worldName);		
	}
	
	public static Zone fromString(String stringData) {
		String[] dataArray = stringData.split(" ");
		// Example: "minecraft:Overworld 0 0 0 100 100 100"
		
		String worldName = dataArray[0];
		
		Vector3Int min = new Vector3Int(
			Integer.parseInt(dataArray[1]),
			Integer.parseInt(dataArray[2]),
			Integer.parseInt(dataArray[3])
		);
		
		Vector3Int max = new Vector3Int(
			Integer.parseInt(dataArray[4]),
			Integer.parseInt(dataArray[5]),
			Integer.parseInt(dataArray[6])
		);
		
		return new Zone(min, max, worldName);
	}
	
	@Override
	public String toString() {
		return worldName +" "+ minPosition.toString() +" "+ maxPosition.toString();
	}
	
	public boolean inLocation(Location point) {
		if (minPosition.x > point.getX()) return false;
		if (minPosition.y > point.getY()) return false;
		if (minPosition.z > point.getZ()) return false;
		if (maxPosition.x < point.getX()) return false;
		if (maxPosition.y < point.getY()) return false;
		if (maxPosition.z < point.getZ()) return false;
		if (!point.getWorld().getName().equals(worldName)) return false;
		
		return true;
	}
}
