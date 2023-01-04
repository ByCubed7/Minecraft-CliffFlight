package io.github.bycubed7.cliffflight.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.bycubed7.cliffflight.managers.ZoneManager;
import io.github.bycubed7.cliffflight.units.Zone;
import io.github.bycubed7.corecubes.commands.Action;
import io.github.bycubed7.corecubes.commands.ActionFailed;
import io.github.bycubed7.corecubes.commands.Arg;
import io.github.bycubed7.corecubes.managers.Tell;
import io.github.bycubed7.corecubes.unit.CoString;
import io.github.bycubed7.corecubes.unit.Node;
import io.github.bycubed7.corecubes.unit.Vector3Int;

public class CommandFlyZone extends Action {

	public CommandFlyZone(JavaPlugin _plugin) {
		super("FlyZone", _plugin);

		// startPosition endPosition world
		//Arg use1Start = Arg.<Vector3Int>Create("startPosition", new Vector3Int());
		//Arg use1End   = Arg.<Vector3Int>Create("endPosition", new Vector3Int());
		//Arg use1World = Arg.<CoString>Create("world", new CoString("world"));

		//Node<Arg<?>> use1StartNode = arguments.add(use1Start);
		//Node<Arg<?>> use1EndNode   = arguments.join(use1StartNode, use1End);
		//Node<Arg<?>> use1WorldNode = 
		//arguments.join(use1EndNode, use1World);
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

			if (args.length == 8)
				if (Bukkit.getWorld(args[7]) == null) {
					Tell.player(player, "Can't find world " + args[7] + "!");
					Tell.player(player, "Found world list contains: " + ChatColor.LIGHT_PURPLE + String.join(", ",  
						Bukkit.getWorlds()
						.stream().map(World::getName)
	                       .collect(Collectors.joining(", "))
					));
					return ActionFailed.OTHER;
				}
	        
	      
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
	        
	        UUID worldId = player.getWorld().getUID();
			if (args.length == 8)
				worldId = Bukkit.getWorld(args[7]).getUID();

			ZoneManager.addZone(new Zone(pos1, pos2, worldId));
			
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
