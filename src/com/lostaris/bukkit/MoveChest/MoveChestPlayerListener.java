package com.lostaris.bukkit.MoveChest;

import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockInteractEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerItemEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Handle events for all Player related events
 * @author Lostaris
 */
public class MoveChestPlayerListener extends PlayerListener {
	private final MoveChest plugin;
	public static final Logger log = Logger.getLogger("Minecraft");

	public MoveChestPlayerListener(MoveChest instance) {
		plugin = instance;
	}

	public void onBlockInteract(BlockInteractEvent event) {
		if (event.getBlock().getType() == Material.CHEST) {
			if (event.isCancelled()) {
				return;
			} else {
				log.info("testing");
				Chest chest = (Chest) event.getBlock();
				Inventory inven = chest.getInventory();
				for (ItemStack i : inven.getContents()) {
					log.info(i.toString());
				}
			}
		}
	}

	//Insert Player related code here
}

