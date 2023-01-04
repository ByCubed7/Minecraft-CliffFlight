package io.github.bycubed7.cliffflight.managers;

import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import io.github.bycubed7.corecubes.CubePlugin;
import io.github.bycubed7.corecubes.managers.ConfigManager;

public class FlightManager implements Listener, Runnable {
	
	public static HashSet<Player> playersMoved;

	public static int blockCount = 0;
	public static int blockCountWE = 0;
	
	public FlightManager(CubePlugin plugin) {
		Bukkit.getServer().getPluginManager().registerEvents((Listener) this, plugin);
		plugin.getServer().getScheduler().runTaskTimer(plugin, this, 0, 2);

		playersMoved = new HashSet<Player>();
		
		ConfigManager config = new ConfigManager(plugin, "Cliff Flight.yml");
		blockCount = config.getInt("height.withoutElytra");
		blockCountWE = config.getInt("height.withElytra");
	}
	
	
	@EventHandler(priority = EventPriority.HIGH)
	public void OnPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		
		// Player has moved, check to see if their in a zone
		ZoneManager.checkPlayer(player);
		checkPlayer(player);
	}
	
	@EventHandler
	public void OnPlayerGlide(EntityToggleGlideEvent event) {
		if (!(event.getEntity() instanceof Player)) return;
		Player player = (Player) event.getEntity();
		
		// Is the player in a zone?
		if (ZoneManager.isPlayerInZone(player)) {
			if(player.getAllowFlight())
			{
				player.setFlying(true);
				event.setCancelled(true);
			}
		}
	}
	
	public static void checkPlayer(Player player) {
		playersMoved.add(player);
	}

	public static void updatePlayer(Player player) {
		// Disallow flight from ground, whether their in a zone or not
		if (player.getGameMode().equals(GameMode.SURVIVAL))
		{
			if (!player.isFlying())
				player.setAllowFlight(false);
		}
		else return;
		
		// If not in a zone ignore
		if (!ZoneManager.isPlayerInZone(player)) return;
		
		// If not falling ignore
		if (player.getVelocity().getY() > -0.01f) return; 
		
		// Is the player wearing an elytra
		boolean hasElytra = false;
		if (player.getInventory().getChestplate() != null)
			hasElytra = player.getInventory().getChestplate().getType() == Material.ELYTRA;
		
		// Get the amount of blocks the player needs to have to enable flight
		int targetHeight = hasElytra ? blockCountWE : blockCount;
		
		// Get the player height
		//boolean isHighEnough = true;
		Location location = player.getLocation();
		for (int i = 0; i < targetHeight; i++) {
			location.add(0, -1, 0);
			if (location.getBlock().getType() != Material.AIR) {
				//isHighEnough = false;
				//break;
				return;
			}
		}
		
		player.setAllowFlight(true);
		player.setFlying(true);
		//player.setFlySpeed(hasElytra ? flySpeedWE : flySpeed);
	}
	

	@Override
	public void run() {
		for (Player player : playersMoved) {
			updatePlayer(player);
			//Tell.player(player, ChatColor.GREEN + "FlightManager: Updating");
		}
		playersMoved.clear();
	}
}
