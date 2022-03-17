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

public class CommandFlyZone extends Action {

	public CommandFlyZone(JavaPlugin _plugin) {
		super("FlyZone", _plugin);
	}

	@Override
	protected ActionFailed approved(Player player, String[] args) {
		// x y z x y z world
		
		if (args.length < 1)
			return ActionFailed.ARGUMENTLENGTH;
		
		if (args[0].equals("add")) {			
	        try{
	            Integer.parseInt(args[1]);
	            Integer.parseInt(args[2]);
	            Integer.parseInt(args[3]);
	            Integer.parseInt(args[4]);
	            Integer.parseInt(args[5]);
	            Integer.parseInt(args[6]);
	        }
	        catch (NumberFormatException ex) {
	        	return ActionFailed.USAGE;
	        }
	        
	        // Check the world name exists
	        //String worldName = player.getWorld().getName();
			//if (args.length == 7)
			//	worldName = args[6];
		}
		else if (args[0].equals("remove")) {
	        // Check if a region exists here
			Optional<Zone> possibleZone = ZoneManager.getZoneByLocation(player.getLocation());
			if (possibleZone.isEmpty()) {
				Tell.player(player, "You are not in a zone!");
				return ActionFailed.OTHER;
			}
		}
        
        
		return ActionFailed.NONE;
	}

	@Override
	protected boolean execute(Player player, String[] args) {
		
		if (args[0].equals("add")) {
	        Vector3Int pos1 = new Vector3Int(
	            Integer.parseInt(args[1]),
	            Integer.parseInt(args[2]),
	            Integer.parseInt(args[3])
	        );
	
	        Vector3Int pos2 = new Vector3Int(
	            Integer.parseInt(args[4]),
	            Integer.parseInt(args[5]),
	            Integer.parseInt(args[6])
	        );
	        
	        String worldName = player.getWorld().getName();
			if (args.length == 8)
				worldName = args[7];

			ZoneManager.addZone(new Zone(pos1, pos2, worldName));
			
			Tell.player(player, "Added a zone!");
			return true;
		}
		
		if (args[0].equals("remove")) {
			Zone zone = ZoneManager.getZoneByLocation(player.getLocation()).get();
			ZoneManager.removeZone(zone);
			Tell.player(player, "Remove zone!");
			return true;
		}
		
		return false;
	}

	@Override
	protected List<String> tab(Player player, Command command, String[] args) {
		List<String> suggestions = new ArrayList<String>();

		suggestions.add(
			"add POSITION POSITION WORLD"
			.replace("POSITION", Vector3Int.fromLocation(player.getLocation()).toString())
			.replace("WORLD", player.getWorld().getName())
		);
		
		suggestions.add(
			"edit POSITION POSITION WORLD"
			.replace("POSITION", Vector3Int.fromLocation(player.getLocation()).toString())
			.replace("WORLD", player.getWorld().getName())
		);
		
		suggestions.add(
			"remove"
		);
		
		// add POSITION POSITION WORLD
		// remove
		// edit
		
		return suggestions;
	}

}
