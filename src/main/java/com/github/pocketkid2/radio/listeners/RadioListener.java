package com.github.pocketkid2.radio.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.CraftingInventory;

import com.github.pocketkid2.radio.util.Radio;
import com.github.pocketkid2.radio.util.Settings;

public class RadioListener implements Listener {

	@EventHandler
	public void onCraft(PrepareItemCraftEvent event) {
		CraftingInventory inventory = event.getInventory();
		if (Radio.isRadio(inventory.getResult())) {
			// Check the middle
			if (Radio.isRadio(inventory.getMatrix()[4])) {
				// Replace the result
				inventory.setResult(Radio.createRadio(Math.min(Radio.getTier(inventory.getMatrix()[4]) + 1, Settings.maxTier)));
			}
		}
	}

	@EventHandler
	public void onPlace(BlockPlaceEvent event) {
		if (Radio.isRadio(event.getItemInHand())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		// Check for right click
		if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			// Check for item
			if (event.hasItem()) {
				// Check for radio
				if (Radio.isRadio(event.getItem())) {
					// Decide whether we are turning on or off
					if (Radio.getState(event.getItem())) {
						event.getPlayer().performCommand("radio off");
					} else {
						event.getPlayer().performCommand("radio on");
					}
				}
			}
		}
	}

}
