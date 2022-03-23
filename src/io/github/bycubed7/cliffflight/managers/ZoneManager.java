package io.github.bycubed7.cliffflight.managers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import io.github.bycubed7.cliffflight.units.Zone;
import io.github.bycubed7.corecubes.CubePlugin;
import io.github.bycubed7.corecubes.unit.Vector3Int;

public class ZoneManager implements Listener, Runnable {
	private static CubePlugin plugin;
	
	private static ArrayList<Zone> zones;
	private static HashSet<Player> playersInZone;
	private static HashSet<Player> playersToCheck;
	
	public ZoneManager(CubePlugin _plugin) {
		plugin = _plugin;
		plugin.getServer().getScheduler().runTaskTimer(plugin, this, 0, 200);

		zones = new ArrayList<Zone>(); // TODO: Load from file
		playersInZone = new HashSet<Player>();
		playersToCheck = new HashSet<Player>();
		
		loadFromConfig();
	}
	
	private static void loadFromConfig() {
        File file = new File(plugin.getDataFolder(), "regions.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        
        List<String> list = config.getStringList("regions");

        for (String zoneData : list)
        	zones.add(Zone.fromString(zoneData));
	}
	
	private static void saveToConfig() {
        File file = new File(plugin.getDataFolder(), "regions.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        
        List<String> list = new ArrayList<String>();//config.getStringList("regions");
        //list.clear();
        
        for (Zone zone : zones)
        	list.add(zone.toString());
        
        config.set("regions", list);

        try {
            config.save(file);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
	}
	
	public static void addZone(Zone zone) {
		if (zones.add(zone))
			saveToConfig();
	}
	
	public static void removeZone(Zone zone) {
		if (zones.remove(zone))
			saveToConfig();
	}
	
	public static Optional<Zone> getZoneByLocation(Location location) {
		return zones.stream().filter(z -> z.inLocation(location)).findFirst();
	}
	
	public static void updateTick(Player player) {
		if (zones.stream().anyMatch(z -> z.inLocation(player.getLocation()))) 
			playersInZone.add(player);
		else 
			playersInZone.remove(player);
	}

	@Override
	public void run() {
	    Iterator<Player> playerIterator = playersToCheck.iterator();
	    while(playerIterator.hasNext())
			updateTick(playerIterator.next());
	    playersToCheck.clear();
	}
	
	public static boolean isPlayerInZone(Player player) {
		return playersInZone.contains(player);
	}
	
	public static void checkPlayer(Player player) {
		playersToCheck.add(player);
	}
}
	