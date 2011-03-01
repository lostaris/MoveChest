package com.lostaris.bukkit.MoveChest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.logging.Logger;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockInteractEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * MoveChest block listener
 * @author Lostaris
 */
public class MoveChestBlockListener extends BlockListener {
	private final MoveChest plugin;
	public static final Logger log = Logger.getLogger("Minecraft");
	Chest source;
	Chest destination;
	ItemStack items[];

	public MoveChestBlockListener(final MoveChest plugin) {
		this.plugin = plugin;
	}

	public void onBlockInteract(BlockInteractEvent event) {
		Player player = null;
		if (event.getBlock().getState() instanceof Chest) {
			if (event.isCancelled()) {
				return;
			} else {
				if (event.getEntity() instanceof Player) {
					player = (Player) event.getEntity();
				}

				// copying
				if (plugin.isFromSet()) {
					Block block = event.getBlock();
					source = (Chest) block.getState();
					items = source.getInventory().getContents();
					
					player.sendMessage("copied chest");
					// flags to show we have copied
					plugin.setFromSet(false);
					plugin.setCopy(true);
					// pasting
				} else if(plugin.isToSet()) {
					Block block = event.getBlock();
					destination = (Chest) block.getState();
					Inventory destInven = destination.getInventory();
					Inventory sourceInven = source.getInventory();
					
					/* This gives a null pointer exception if there wouldn't be any left over
					 * Works fine if there would be any left over, but not if there isn't any left over
					 * as the amounts moved double
					 * eg pasting one sand would result in there being two sand
					 */
					try {
						// for when we can't fit everything in the destination chest
						// add to the destination chest and record what can't fit
						HashMap<Integer, ItemStack>  leftOver = destInven.addItem(sourceInven.getContents());
						sourceInven.clear();
						// add the left over back to the source chest
						for (Entry<Integer, ItemStack> value: leftOver.entrySet()) {
							sourceInven.addItem(value.getValue());
						}
						player.sendMessage("destination chest full, leaving some behind");
					} catch (NullPointerException e){
						// for when we can fit everything in the destination chest
						for (int i =0; i<items.length; i++) {
							if (items[i].getType() != Material.AIR) {
								destInven.addItem(items[i]);
								log.warning("pasted " + items[i].toString());
							}
						}
					}

					source.update();
					destination.update();
					
					// we have done the paste, clear the copied values.
					source = null;
					destination = null;

					plugin.setCopy(false);
					plugin.setToSet(false);
					player.sendMessage("moved contents of the chest.");
				}
			}
		}
	}
}
