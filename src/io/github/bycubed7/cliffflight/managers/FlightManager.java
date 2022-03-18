package io.github.bycubed7.cliffflight.managers;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import io.github.bycubed7.corecubes.CubePlugin;
import io.github.bycubed7.corecubes.managers.ConfigManager;
import io.github.bycubed7.corecubes.managers.Tell;

public class FlightManager implements Listener {

	int blockCount = 0;
	int blockCountWE = 0;
	float flySpeed = 0.2f;
	float flySpeedWE = 0.4f;
	
	public FlightManager(CubePlugin plugin) {
		Bukkit.getServer().getPluginManager().registerEvents((Listener) this, plugin);
		
		ConfigManager config = new ConfigManager(plugin, "Cliff Flight.yml");
		blockCount = config.getInt("height.withoutElytra");
		blockCountWE = config.getInt("height.withElytra");
		flySpeed = config.getFloat("speed.withoutElytra");
		flySpeedWE = config.getFloat("speed.withElytra");
	}
	
	
	@EventHandler
	public void OnPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		
		// Player has moved, check to see if their in a zone
		ZoneManager.checkPlayer(player);
		
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
		Location location = player.getLocation();
		while (location.getBlock().getType() == Material.AIR)
			location.add(0, -0.5, 0);
		int currentHeight = player.getLocation().getBlockY() - location.getBlockY();
		
		// Does the player have enough height?
		if (currentHeight < targetHeight) return;
		
		player.setAllowFlight(true);
		player.setFlying(true);
		player.setFlySpeed(hasElytra ? flySpeedWE : flySpeed);
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
}
