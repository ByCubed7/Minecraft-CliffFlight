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
	
	public static float flySpeed = 0.2f;
	public static float flySpeedWE = 0.4f;
	
	public ElytraManager(CubePlugin _plugin) {
		plugin = _plugin;
		plugin.getServer().getScheduler().runTaskTimer(plugin, this, 0, 40);
		
		ConfigManager config = new ConfigManager(plugin, "Cliff Flight.yml");
		flySpeed = config.getFloat("speed.withoutElytra");
		flySpeedWE = config.getFloat("speed.withElytra");
		
		playersWithElytra = new HashSet<Player>();
	}
	
	private static boolean isPlayerWearingElytra(Player player) {
		if (player.getInventory().getChestplate() == null) return false;
		if (player.getInventory().getChestplate().getType() != Material.ELYTRA) return false;
		
		return true;
	}
	
	public static void updateTick(Player player) {
		if (!player.getGameMode().equals(GameMode.SURVIVAL)) return;

		if (isPlayerWearingElytra(player)) {
			playersWithElytra.add(player);
			player.setFlySpeed(flySpeedWE);
		}
		else {
			playersWithElytra.remove(player); 
			player.setFlySpeed(flySpeed);
		}
	}
	
	public static boolean isWearingElyta(Player player) {
		return playersWithElytra.contains(player);
	}
	

	@Override
	public void run() {
	    for(Player player : Bukkit.getOnlinePlayers()) {
			updateTick(player);
			//Tell.player(player, ChatColor.GOLD + "ElytraManager: Updating to " + player.getFlySpeed());
	    }
	}
}
