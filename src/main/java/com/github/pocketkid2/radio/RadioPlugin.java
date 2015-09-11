package com.github.pocketkid2.radio;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.pocketkid2.radio.commands.RadioCommand;
import com.github.pocketkid2.radio.listeners.RadioListener;
import com.github.pocketkid2.radio.util.Radio;
import com.github.pocketkid2.radio.util.Settings;

public class RadioPlugin extends JavaPlugin {

	@SuppressWarnings("deprecation")
	@Override
	public void onEnable() {
		// Load values from config
		saveDefaultConfig();
		// Initialize map
		Settings.radios = new HashMap<Integer, Integer>();
		// Get map from config
		Map<String, Object> map = getConfig().getConfigurationSection("radios").getValues(false);
		for (String s : map.keySet()) {
			// Check that the key and the value are both integers
			if (s.matches("^-?\\d+$") && map.get(s) instanceof Integer) {
				Settings.radios.put(Integer.parseInt(s), (Integer) map.get(s));
				if (Integer.parseInt(s) > Settings.maxTier) {
					Settings.maxTier = Integer.parseInt(s);
				}
			}
		}
		// Set the prefix
		Settings.prefix = getConfig().getString("prefix");
		// Create recipe
		Settings.recipe = new ShapedRecipe(Radio.createRadio(1));
		Settings.recipe.shape(" g ", "bli", "rrr");
		Settings.recipe.setIngredient('g', Material.THIN_GLASS);
		Settings.recipe.setIngredient('b', Material.STONE_BUTTON);
		Settings.recipe.setIngredient('l', Material.REDSTONE_LAMP_OFF);
		Settings.recipe.setIngredient('i', Material.IRON_FENCE);
		Settings.recipe.setIngredient('r', Material.REDSTONE);
		getServer().addRecipe(Settings.recipe);
		// Register command
		getCommand("radio").setExecutor(new RadioCommand(this));
		// Register listener
		getServer().getPluginManager().registerEvents(new RadioListener(this), this);
		// Log status
		getLogger().info("Done!");
	}

	@Override
	public void onDisable() {
		// Log status
		getLogger().info("Done!");
	}

}
