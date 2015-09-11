package com.github.pocketkid2.radio.util;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Radio {

	public static Pattern tier = Pattern.compile("Tier: (\\d)");
	public static Pattern radius = Pattern.compile("Radius: (\\d+)");
	public static Pattern frequency = Pattern.compile("Frequency: (\\d{3})");
	public static Pattern state = Pattern.compile("State: (OFF|ON)");

	@SuppressWarnings("deprecation")
	public static ItemStack createRadio(int tier) {
		// Check that the tier exists
		if (Settings.radios.containsKey(tier)) {
			// Make the item
			ItemStack stack = new ItemStack(Material.REDSTONE_LAMP_OFF);
			// Create the meta
			ItemMeta meta = stack.getItemMeta();
			// Set the name
			meta.setDisplayName("Radio");
			// Set the lores
			List<String> lores = Arrays.asList(getTierString(tier), getRadiusString(tier), getFrequencyString(000), getStateString(false));
			meta.setLore(lores);
			// Return
			stack.setItemMeta(meta);
			return stack;
		}
		return null;
	}

	public static boolean isRadio(ItemStack stack) {
		if (stack.getType() == Material.REDSTONE_LAMP_ON || stack.getType() == Material.REDSTONE_LAMP_OFF) {
			if (stack.hasItemMeta()) {
				ItemMeta meta = stack.getItemMeta();
				if (meta.hasDisplayName() && meta.getDisplayName().equalsIgnoreCase("Radio")) {
					if (meta.hasLore()) {
						List<String> lores = meta.getLore();
						if (!Radio.tier.matcher(lores.get(0)).matches()) {
							return false;
						}
						if (!Radio.radius.matcher(lores.get(1)).matches()) {
							return false;
						}
						if (!Radio.frequency.matcher(lores.get(2)).matches()) {
							return false;
						}
						if (!Radio.state.matcher(lores.get(3)).matches()) {
							return false;
						}
						return true;
					}
				}
			}
		}
		return false;
	}

	public static int getTier(ItemStack stack) {
		if (Radio.isRadio(stack)) {
			String lore = stack.getItemMeta().getLore().get(0);
			Matcher m = Radio.tier.matcher(lore);
			m.find();
			return Integer.parseInt(m.group(1));
		}
		return 0;
	}

	public static int getRadius(ItemStack stack) {
		if (Radio.isRadio(stack)) {
			String lore = stack.getItemMeta().getLore().get(1);
			Matcher m = Radio.radius.matcher(lore);
			m.find();
			return Integer.parseInt(m.group(1));
		}
		return 0;
	}

	public static int getFrequency(ItemStack stack) {
		if (Radio.isRadio(stack)) {
			String lore = stack.getItemMeta().getLore().get(2);
			Matcher m = Radio.frequency.matcher(lore);
			m.find();
			return Integer.parseInt(m.group(1));
		}
		return 0;
	}

	public static boolean getState(ItemStack stack) {
		if (Radio.isRadio(stack)) {
			String lore = stack.getItemMeta().getLore().get(3);
			Matcher m = Radio.state.matcher(lore);
			m.find();
			return Boolean.parseBoolean(m.group(1));
		}
		return false;
	}

	/**
	 * Returns the lore string for the given tier
	 * 
	 * @param tier
	 * @return
	 */
	public static String getTierString(int tier) {
		return String.format("Tier: %d", tier);
	}

	/**
	 * Returns the lore string for the given radius
	 * 
	 * @param tier
	 * @return
	 */
	public static String getRadiusString(int tier) {
		return String.format("Radius: %d", Settings.radios.get(tier));
	}

	/**
	 * Returns the lore string for the given frequency
	 * 
	 * @param freq
	 * @return
	 */
	public static String getFrequencyString(int freq) {
		return String.format("Frequency: %03d", freq);
	}

	/**
	 * Returns the lore string for the given state
	 * 
	 * @param state
	 * @return
	 */
	public static String getStateString(boolean state) {
		return String.format("State: %s", state ? "ON" : "OFF");
	}
}
