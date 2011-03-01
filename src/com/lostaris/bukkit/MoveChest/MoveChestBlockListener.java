package com.lostaris.bukkit.MoveChest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockInteractEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * MoveChest block listener
 * @author Lostaris
 */
public class MoveChestBlockListener extends BlockListener {
	private final MoveChest plugin;
	public static final Logger log = Logger.getLogger("Minecraft");
	Inventory source;
	ArrayList<ItemStack> sourceItems = new ArrayList<ItemStack>();
	Inventory destination;

	public MoveChestBlockListener(final MoveChest plugin) {
		this.plugin = plugin;
	}

	public void onBlockDamage(BlockDamageEvent event) {
		Player player = event.getPlayer();
		if (player.getItemInHand().getType() == Material.CHEST){

		}
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
					Chest chest = (Chest) block.getState();
					source = chest.getInventory();
					for (ItemStack i : source.getContents()) {
						if (i.getType() != Material.AIR) {
							sourceItems.add(i);
							//log.warning("copied " + i);
						}
					}
					plugin.setFromSet(false);
					plugin.setCopy(true);
					player.sendMessage("copied chest");
					// pasting
				} else if(plugin.isToSet()) {
					Block block = event.getBlock();
					Chest chest = (Chest) block.getState();
					destination = chest.getInventory();

					try {
						HashMap<Integer, ItemStack>  leftOver = destination.addItem(source.getContents());
						source.clear();
						for (Entry<Integer, ItemStack> value: leftOver.entrySet()) {
							source.addItem(value.getValue());
						}
						player.sendMessage("destination chest full, leaving some behind");
						sourceItems.clear();
					} catch (NullPointerException e){
//						ItemStack[] inven = source.getContents();
//						log.warning("inven " + inven);
//						for (ItemStack i : inven) {
//							i.toString();
//						}
						
						//destination.clear();
						source.clear();
						for ( ItemStack i : sourceItems) {
							destination.addItem(i);
							log.warning("pasted " + i);
						}
						
						//destination.addItem(inven);
						//source.clear();
						sourceItems.clear();						
					}
					source = null;
					destination = null;
					
					//chest.update();
					plugin.setCopy(false);
					plugin.setToSet(false);
					player.sendMessage("moved contents of the chest.");
				}
			}
		}
	}

	//put all Block related code here
}
