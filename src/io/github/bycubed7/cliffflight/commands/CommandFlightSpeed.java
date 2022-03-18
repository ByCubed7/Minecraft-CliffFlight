package io.github.bycubed7.cliffflight.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.bycubed7.cliffflight.managers.ZoneManager;
import io.github.bycubed7.cliffflight.units.Zone;
import io.github.bycubed7.corecubes.commands.Action;
import io.github.bycubed7.corecubes.commands.ActionFailed;
import io.github.bycubed7.corecubes.managers.Tell;
import io.github.bycubed7.corecubes.unit.Vector3Int;

public class CommandFlightSpeed extends Action {

	public CommandFlightSpeed(JavaPlugin _plugin) {
		super("Speed", _plugin);
	}

	@Override
	protected ActionFailed approved(Player player, String[] args) {
		
		if (args.length == 1) {
			Float targetSpeed = 0f;
			targetSpeed = Float.parseFloat(args[0]);
			try {			
			}
	        catch (NumberFormatException ex) {
	        	return ActionFailed.USAGE;
	        }
			
			if (targetSpeed > 1f || targetSpeed < 0f) {
	        	Tell.player(player, "Speed can only be set to between 0.0 and 1.0");
				return ActionFailed.USAGE;
	        }
		}
		else if (args.length != 0) {
			return ActionFailed.ARGUMENTLENGTH;
		}
		
		return ActionFailed.NONE;
	}

	@Override
	protected boolean execute(Player player, String[] args) {
		
		Float targetSpeed = 0.1f;
		if (args.length == 1)
			targetSpeed = Float.parseFloat(args[0]);
		else if (player.getFlySpeed() == 0.1f)
			targetSpeed = 0.3f;
		
		player.setFlySpeed(targetSpeed);
		Tell.player(player, "Set flight speed to " + targetSpeed + "!");
		return true;
	}

	@Override
	protected List<String> tab(Player player, Command command, String[] args) {
		List<String> suggestions = new ArrayList<String>();

		suggestions.add(
			"0.1"
		);
				
		return suggestions;
	}

}
