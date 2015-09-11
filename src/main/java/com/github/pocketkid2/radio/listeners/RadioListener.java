package com.github.pocketkid2.radio.listeners;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.pocketkid2.radio.RadioPlugin;
import com.github.pocketkid2.radio.util.Radio;
import com.github.pocketkid2.radio.util.Settings;

public class RadioListener implements Listener {

	private RadioPlugin plugin;

	public RadioListener(RadioPlugin pl) {
		plugin = pl;
	}

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
	public void onInteract(PlayerInteractEvent event) {
		// Check for right click
		if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			// Check for item
			if (event.hasItem()) {
				// Check for radio
				if (Radio.isRadio(event.getItem())) {
					// Get item
					ItemStack stack = event.getItem();
					boolean state;
					// Check current state
					if (stack.getType() == Material.REDSTONE_LAMP_ON) {
						// Set it to off
						stack.setType(Material.REDSTONE_LAMP_OFF);
						state = false;
					} else {
						// Set it to on
						stack.setType(Material.REDSTONE_LAMP_ON);
						state = true;
					}
					// Now manipulate the meta
					ItemMeta meta = stack.getItemMeta();
					List<String> lore = meta.getLore();
					lore.set(3, Radio.getStateString(state));
					meta.setLore(lore);
					stack.setItemMeta(meta);
				}
			}
		}
	}

}
