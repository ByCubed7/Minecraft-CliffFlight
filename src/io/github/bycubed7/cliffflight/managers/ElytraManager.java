package io.github.bycubed7.cliffflight.managers;

import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import io.github.bycubed7.corecubes.CubePlugin;
import io.github.bycubed7.corecubes.managers.ConfigManager;

public class ElytraManager implements Runnable {
	private static CubePlugin plugin;
	
	public static HashSet<Player> playersWithElytra;
	
	private static float flySpeed = 0.2f;
	private static float flySpeedWE = 0.4f;
	
	public ElytraManager(CubePlugin plugin) {
		plugin.getServer().getScheduler().runTaskTimer(plugin, this, 0, 200);
		
		ConfigManager config = new ConfigManager(plugin, "Cliff Flight.yml");
		flySpeed = config.getFloat("speed.withoutElytra");
		flySpeedWE = config.getFloat("speed.withElytra");
		
		playersWithElytra = new HashSet<Player>();
	}
	
	public static void updateTick(Player player) {
		
		if (isPlayerWearingElytra(player)) {
			if (playersWithElytra.add(player))
				onUpdate(player, true);			
		}
		else 
			if (playersWithElytra.remove(player)) 
				onUpdate(player, false);
	}
	
	private static boolean isPlayerWearingElytra(Player player) {
		if (player.getInventory().getChestplate() == null) return false;
		if (player.getInventory().getChestplate().getType() != Material.ELYTRA) return false;
		
		return true;
	}

	@Override
	public void run() {
	    for(Player player : Bukkit.getOnlinePlayers())
			updateTick(player);
	}
	
	public static void onUpdate(Player player, boolean has) {
		if (player.getGameMode().equals(GameMode.SURVIVAL))
			player.setFlySpeed(has ? flySpeedWE : flySpeed);
	}
	
	public static boolean isWearingElyta(Player player) {
		return playersWithElytra.contains(player);
	}
}
