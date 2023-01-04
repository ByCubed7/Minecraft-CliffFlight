package io.github.bycubed7.cliffflight.units;

import java.util.UUID;

import org.bukkit.Location;

import io.github.bycubed7.corecubes.unit.Vector3Int;

public class Zone {
	public Vector3Int minPosition;
	public Vector3Int maxPosition;
	public UUID worldId;
	
	public Zone(Vector3Int _min, Vector3Int _max, UUID _worldId) {
		minPosition = new Vector3Int(0, 0, 0);
		maxPosition = new Vector3Int(0, 0, 0);
		minPosition.x = Math.min(_min.x, _max.x);
		minPosition.y = Math.min(_min.y, _max.y);
		minPosition.z = Math.min(_min.z, _max.z);				
		maxPosition.x = Math.max(_min.x, _max.x);
		maxPosition.y = Math.max(_min.y, _max.y);
		maxPosition.z = Math.max(_min.z, _max.z);
		worldId = _worldId;
	}
	
	public static Zone FromLocation(Location starting, Location ending) {
		Vector3Int minPosition = new Vector3Int(starting.getBlockX(), starting.getBlockY(), starting.getBlockZ());
		Vector3Int maxPosition = new Vector3Int(ending.getBlockX(), ending.getBlockY(), ending.getBlockZ());

		UUID worldId = starting.getWorld().getUID();
//		minPosition.x = Math.min(starting.getBlockX(), ending.getBlockX());
//		minPosition.y = Math.min(starting.getBlockY(), ending.getBlockY());
//		minPosition.z = Math.min(starting.getBlockZ(), ending.getBlockZ());				
//		maxPosition.x = Math.max(starting.getBlockX(), ending.getBlockX());
//		maxPosition.y = Math.max(starting.getBlockY(), ending.getBlockY());
//		maxPosition.z = Math.max(starting.getBlockZ(), ending.getBlockZ());
		
		return new Zone(minPosition, maxPosition, worldId);		
	}
	
	public static Zone fromString(String stringData) {
		String[] dataArray = stringData.split(" ");
		// Example: "minecraft:Overworld 0 0 0 100 100 100"
		
		UUID worldId = UUID.fromString(dataArray[0]);
		
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
		
		return new Zone(min, max, worldId);
	}
	
	@Override
	public String toString() {
		return worldId.toString() +" "+ minPosition.toString() +" "+ maxPosition.toString();
	}
	
	public boolean inLocation(Location point) {
		if (minPosition.x > point.getX()) return false;
		if (minPosition.y > point.getY()) return false;
		if (minPosition.z > point.getZ()) return false;
		if (maxPosition.x < point.getX()) return false;
		if (maxPosition.y < point.getY()) return false;
		if (maxPosition.z < point.getZ()) return false;
		if (!point.getWorld().getUID().equals(worldId)) return false;
		return true;
	}
}
