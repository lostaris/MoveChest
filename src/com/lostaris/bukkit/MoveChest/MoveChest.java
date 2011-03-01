package com.lostaris.bukkit.MoveChest;

import java.util.HashMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;

/**
 * MoveChest for Bukkit
 *
 * @author Lostaris
 */
public class MoveChest extends JavaPlugin {
	//private final MoveChestPlayerListener playerListener = new MoveChestPlayerListener(this);
	private final MoveChestBlockListener blockListener = new MoveChestBlockListener(this);
	private final HashMap<Player, Boolean> debugees = new HashMap<Player, Boolean>();
	private boolean fromSet = false;
	private boolean copy = false;
	private boolean toSet = false;

	public void onEnable() {
		// TODO: Place any custom enable code here including the registration of any events

		// Register our events
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Event.Type.BLOCK_DAMAGED , blockListener, Priority.Monitor, this);
		pm.registerEvent(Event.Type.BLOCK_INTERACT , blockListener, Priority.Monitor, this);

		// EXAMPLE: Custom code, here we just output some info so we can check all is well
		PluginDescriptionFile pdfFile = this.getDescription();
		System.out.println( pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!" );
	}
	public void onDisable() {
		// TODO: Place any custom disable code here

		// NOTE: All registered events are automatically unregistered when a plugin is disabled

		// EXAMPLE: Custom code, here we just output some info so we can check all is well
		System.out.println("Goodbye world!");
	}

	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		Player player = null;
		// if the command sender is a player
		if(sender instanceof Player) {
			player = (Player) sender;
		}
		String[] split = args;
		String commandName = command.getName().toLowerCase();
		if (commandName.equals("mvchest")) {
			if (split.length == 1) {
				if (split[0].equalsIgnoreCase("from")) {
					if (!fromSet && !copy) {
						setFromSet(true);
						player.sendMessage("open a chest to copy it");
					} else if (copy) {
						player.sendMessage("you have already copied a chest!");
					}
				} else if (split[0].equalsIgnoreCase("to")) {
					if (copy) {
						setToSet(true);
						player.sendMessage("open a chest to move the contents in to");
					} else {
						player.sendMessage("copy a chest first");
					}
				}
			}
			
		}
		
		return true;
	}

	public boolean isDebugging(final Player player) {
		if (debugees.containsKey(player)) {
			return debugees.get(player);
		} else {
			return false;
		}
	}

	public void setDebugging(final Player player, final boolean value) {
		debugees.put(player, value);
	}
	
	public void setToSet(boolean toSet) {
		this.toSet = toSet;
	}
	public boolean isToSet() {
		return toSet;
	}
	public void setFromSet(boolean fromSet) {
		this.fromSet = fromSet;
	}
	public boolean isFromSet() {
		return fromSet;
	}
	public void setCopy(boolean copy) {
		this.copy = copy;
	}
	public boolean isCopy() {
		return copy;
	}
}

