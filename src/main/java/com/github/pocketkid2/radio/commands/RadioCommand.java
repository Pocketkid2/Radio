package com.github.pocketkid2.radio.commands;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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

		if (!sender.hasPermission("radio.use")) {
			sender.sendMessage(ChatColor.RED + "You don't have permission!");
			return true;
		}

		Player player = (Player) sender;
		// Check that he's holding a radio
		if (!args[0].equalsIgnoreCase("give") && !Radio.isRadio(player.getItemInHand())) {
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
		case "give":
			if (!player.hasPermission("radio.give")) {
				player.sendMessage(ChatColor.RED + "You don't have permission");
				return true;
			}
			give(player, Arrays.copyOfRange(args, 1, args.length));
			return true;
		default:
			return true;
		}
	}

	private void give(Player player, String[] args) {
		try {
			player.getInventory().addItem(Radio.createRadio(Integer.parseInt(args[0])));
		} catch (Exception e) {
			player.sendMessage(ChatColor.RED + "You did something wrong! Type a number tier!");
		}
	}

	private void turnOff(Player player) {
		// If the radio is on
		ItemStack stack = player.getItemInHand();
		if (Radio.getState(stack)) {
			// Set the item and lore
			ItemMeta meta = stack.getItemMeta();
			List<String> lore = meta.getLore();
			lore.set(3, Radio.getStateString(false));
			meta.setLore(lore);
			stack.setItemMeta(meta);
			player.setItemInHand(stack);
			player.sendMessage(ChatColor.AQUA + "Turned the radio " + ChatColor.RED + "OFF");
		}
	}

	private void turnOn(Player player) {
		// If the radio is on
		ItemStack stack = player.getItemInHand();
		if (!Radio.getState(stack)) {
			// Set the item and lore
			ItemMeta meta = stack.getItemMeta();
			List<String> lore = meta.getLore();
			lore.set(3, Radio.getStateString(true));
			meta.setLore(lore);
			stack.setItemMeta(meta);
			player.setItemInHand(stack);
			player.sendMessage(ChatColor.AQUA + "Turned the radio " + ChatColor.GREEN + "ON");
		}
	}

	private void broadcast(Player player, String[] args) {
		// Check that it's on
		if (!Radio.getState(player.getItemInHand())) {
			player.sendMessage(ChatColor.RED + "That radio is off!");
			return;
		}
		// Get the message
		String message = ChatColor.translateAlternateColorCodes('&', String.format(Settings.format, player.getName(), String.join(" ", args)));
		// Get the radius
		int radius = Radio.getRadius(player.getItemInHand());
		// Send the message
		player.sendMessage(message);
		for (Player p : Bukkit.getOnlinePlayers()) {
			// Ignore same player
			if (p != player) {
				// Check if the player is in the radius
				if (player.getLocation().distance(p.getLocation()) <= radius) {
					// Check if the player has a radio
					for (ItemStack stack : p.getInventory().getContents()) {
						if (Radio.isRadio(stack) && Radio.getFrequency(stack) == Radio.getFrequency(player.getItemInHand())) {
							p.sendMessage(message);
						}
					}
				}
			}
		}
	}

	private void setFrequency(Player player, String freq) {
		if (freq.length() != 3) {
			player.sendMessage(ChatColor.RED + "Must be 3 digits!");
			return;
		}
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
		player.sendMessage(ChatColor.AQUA + String.format("Frequency now set to %s", frequency));
	}

}
