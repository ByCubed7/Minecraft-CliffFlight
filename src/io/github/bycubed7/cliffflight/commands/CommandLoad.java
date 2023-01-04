package io.github.bycubed7.cliffflight.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.bycubed7.cliffflight.CliffFlight;
import io.github.bycubed7.cliffflight.managers.ElytraManager;
import io.github.bycubed7.cliffflight.managers.FlightManager;
import io.github.bycubed7.corecubes.commands.Action;
import io.github.bycubed7.corecubes.commands.ActionFailed;
import io.github.bycubed7.corecubes.managers.ConfigManager;
import io.github.bycubed7.corecubes.managers.Tell;

public class CommandLoad extends Action {

	// BOTCH
	CliffFlight cliffFlight;
	
	public CommandLoad(CliffFlight _plugin) {
		super("Load", _plugin);
		cliffFlight = _plugin;
	}

	@Override
	protected ActionFailed approved(Player player, String[] args) {
		return ActionFailed.NONE;
	}

	@Override
	protected boolean execute(Player player, String[] args) {

		Tell.player(player, "Reloading the config");
		ConfigManager config = new ConfigManager(plugin, "Cliff Flight.yml");
		
		FlightManager.blockCount = config.getInt("height.withoutElytra");
		FlightManager.blockCountWE = config.getInt("height.withElytra");
		ElytraManager.flySpeed = config.getFloat("speed.withoutElytra");
		ElytraManager.flySpeedWE = config.getFloat("speed.withElytra");
		
		Tell.player(player, 
			"flightManager.blockCount = " + FlightManager.blockCount + " \n" +
			"flightManager.blockCountWithElytra = " + FlightManager.blockCountWE + " \n" +
			"elytraManager.flySpeed = " + ElytraManager.flySpeed + " \n" +
			"elytraManager.flySpeedWithElytra = " + ElytraManager.flySpeedWE + " \n"
		);
		
	    for(Player updatePlayer : Bukkit.getOnlinePlayers())
	    	ElytraManager.updateTick(updatePlayer);
		
		return true;
	}

	@Override
	protected List<String> tab(Player player, Command command, String[] args) {
		return null;
	}

}
