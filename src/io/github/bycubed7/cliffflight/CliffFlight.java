package io.github.bycubed7.cliffflight;

import io.github.bycubed7.cliffflight.commands.CommandFlyZone;
import io.github.bycubed7.cliffflight.managers.FlightManager;
import io.github.bycubed7.cliffflight.managers.ZoneManager;
import io.github.bycubed7.corecubes.CubePlugin;

public class CliffFlight extends CubePlugin {

	@Override
	protected void onBoot() {
		banner.add("  _____   _   _    __    __     ______   _   _           _       _");
		banner.add(" / ____| | | (_)  / _|  / _|   |  ____| | | (_)         | |     | |");
		banner.add("| |      | |  _  | |_  | |_    | |__    | |  _    __ _  | |__   | |_");
		banner.add("| |      | | | | |  _| |  _|   |  __|   | | | |  / _` | | '_ \\  | __|");
		banner.add("| |____  | | | | | |   | |     | |      | | | | | (_| | | | | | | |_");
		banner.add(" \\_____| |_| |_| |_|   |_|     |_|      |_| |_|  \\__, | |_| |_|  \\__|");
		banner.add("                                                  __/ |");
		banner.add("                                                 |___/ ");		
	}

	@Override
	protected void onCommands() {
		new CommandFlyZone(this);
	}

	@Override
	protected void onListeners() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onManagers() {
		new FlightManager(this);
		new ZoneManager(this);
	}

	@Override
	protected void onReady() {}

	@Override
	protected void onStart() {}

}
