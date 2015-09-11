package com.github.pocketkid2.radio.commands;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.pocketkid2.radio.RadioPlugin;
import com.github.pocketkid2.radio.util.Radio;
import com.github.pocketkid2.radio.util.Settings;

public class RadioCommand implements CommandExecutor {

	private RadioPlugin plugin;

	public RadioCommand(RadioPlugin pl) {
		plugin = pl;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		// Check for player
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "You must be a player!");
			return true;
		}

		// Check for an argument
		if (args.length < 1) {
			sender.sendMessage(ChatColor.RED + "Not enough arguments!");
			return false;
		}

		Player player = (Player) sender;
		// Check that he's holding a radio
		if (!Radio.isRadio(player.getItemInHand())) {
			player.sendMessage(ChatColor.RED + "You are not holding a radio!");
			return true;
		}

		switch (args[0].toLowerCase()) {
		case "frequency":
			if (args.length < 2) {
				sender.sendMessage(ChatColor.RED + "Not enough arguments!");
				return false;
			}
			if (args.length > 2) {
				sender.sendMessage(ChatColor.RED + "Too many arguments!");
				return false;
			}
			setFrequency(player, args[1]);
			return true;
		case "broadcast":
			broadcast(player, Arrays.copyOfRange(args, 1, args.length));
			return true;
		case "on":
			turnOn(player);
			return true;
		case "off":
			turnOff(player);
			return true;
		default:
			return true;
		}
	}

	private void turnOff(Player player) {
		// If the radio is on
		ItemStack stack = player.getItemInHand();
		if (Radio.getState(stack)) {
			// Set the item and lore
			stack.setType(Material.REDSTONE_LAMP_OFF);
			ItemMeta meta = stack.getItemMeta();
			List<String> lore = meta.getLore();
			lore.set(3, Radio.getStateString(false));
			meta.setLore(lore);
			stack.setItemMeta(meta);
			player.setItemInHand(stack);
			player.sendMessage(ChatColor.AQUA + "Turned the radio OFF");
		}
	}

	private void turnOn(Player player) {
		// If the radio is on
		ItemStack stack = player.getItemInHand();
		if (!Radio.getState(stack)) {
			// Set the item and lore
			stack.setType(Material.REDSTONE_LAMP_ON);
			ItemMeta meta = stack.getItemMeta();
			List<String> lore = meta.getLore();
			lore.set(3, Radio.getStateString(true));
			meta.setLore(lore);
			stack.setItemMeta(meta);
			player.setItemInHand(stack);
			player.sendMessage(ChatColor.AQUA + "Turned the radio ON");
		}
	}

	private void broadcast(Player player, String[] args) {
		// Check that it's on
		if (!Radio.getState(player.getItemInHand())) {
			player.sendMessage(ChatColor.RED + "That radio is off!");
			return;
		}
		// Get the message
		String message = String.format(Settings.prefix, player) + String.join(" ", args);
		// Get the radius
		int radius = Radio.getRadius(player.getItemInHand());
		// Send the message
		player.sendMessage(message);
		for (Player p : Bukkit.getOnlinePlayers()) {
			try {
				// Check if the player is in the radius
				if (player.getLocation().distance(p.getLocation()) <= radius) {
					// Check if the player has a radio
					for (ItemStack stack : p.getInventory().getContents()) {
						if (Radio.isRadio(stack) && Radio.getFrequency(stack) == Radio.getFrequency(player.getItemInHand())) {
							p.sendMessage(message);
						}
					}
				}
			} catch (Exception e) {
				// Do nothing. Seriously.
			}
		}
	}

	private void setFrequency(Player player, String freq) {
		String frequency;
		try {
			frequency = Radio.getFrequencyString(Integer.parseInt(freq));
		} catch (NumberFormatException e) {
			player.sendMessage(ChatColor.RED + "That's not a number!");
			return;
		}

		ItemMeta meta = player.getItemInHand().getItemMeta();
		List<String> lore = meta.getLore();
		lore.set(2, frequency);
		meta.setLore(lore);
		player.getItemInHand().setItemMeta(meta);
	}

}
