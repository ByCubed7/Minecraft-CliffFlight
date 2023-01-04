package io.github.bycubed7.cliffflight.managers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import io.github.bycubed7.cliffflight.units.Zone;
import io.github.bycubed7.corecubes.CubePlugin;

public class ZoneManager implements Listener, Runnable {
	private static CubePlugin plugin;
	
	private static ArrayList<Zone> zones;
	private static HashSet<Player> playersInZone;
	private static HashSet<Player> playersToCheck;
	
	public ZoneManager(CubePlugin _plugin) {
		plugin = _plugin;
		plugin.getServer().getScheduler().runTaskTimer(plugin, this, 0, 20);

		zones = new ArrayList<Zone>(); // TODO: Load from file
		playersInZone = new HashSet<Player>();
		playersToCheck = new HashSet<Player>();
		
		loadFromConfig();
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void OnPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		ZoneManager.checkPlayer(player);
		checkPlayer(player);
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
	
	public static boolean isPlayerInZone(Player player) {
		return playersInZone.contains(player);
	}
	
	public static void checkPlayer(Player player) {
		playersToCheck.add(player);
	}

	
	@Override
	public void run() {
	    for(Player player : playersToCheck) {
			updateTick(player);
			//if (playersInZone.contains(player)) Tell.player(player, ChatColor.YELLOW + "ZoneManager: Updating");
			//else Tell.player(player, ChatColor.RED + "ZoneManager: Updating");
				
	    }
	    playersToCheck.clear();
	}
}
	