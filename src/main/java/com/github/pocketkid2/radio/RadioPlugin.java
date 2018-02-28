package com.github.pocketkid2.radio;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.pocketkid2.radio.commands.RadioCommand;
import com.github.pocketkid2.radio.listeners.RadioListener;
import com.github.pocketkid2.radio.util.Radio;
import com.github.pocketkid2.radio.util.Settings;

public class RadioPlugin extends JavaPlugin {

	private RadioAPI api;
	
	@Override
	public void onEnable() {
		// Create the API
		api = new RadioAPI(this);

		// Load values from config
		saveDefaultConfig();

		// Initialize maps
		Settings.radiuses = new HashMap<Integer, Integer>();
		Settings.colors = new HashMap<Integer, ChatColor>();

		// Get radius map
		Map<String, Object> map = getConfig().getConfigurationSection("radiuses").getValues(false);
		for (String s : map.keySet()) {
			// Check that the key and the value are both integers
			if (s.matches("^-?\\d+$") && map.get(s) instanceof Integer) {
				Settings.radiuses.put(Integer.parseInt(s), (Integer) map.get(s));
				if (Integer.parseInt(s) > Settings.maxTier) {
					Settings.maxTier = Integer.parseInt(s);
				}
			}
		}

		// Get color map
		map = getConfig().getConfigurationSection("colors").getValues(false);
		for (String s : map.keySet()) {
			if (s.matches("^-?\\d+$") && map.get(s) instanceof String) {
				Settings.colors.put(Integer.parseInt(s), ChatColor.valueOf((String) map.get(s)));
			}
		}
		
		createRecipe();

		// Register command
		getCommand("radio").setExecutor(new RadioCommand());

		// Register listener
		getServer().getPluginManager().registerEvents(new RadioListener(), this);

		// Log status
		getLogger().info("Done!");
	}
	
	@Override
	public void onDisable() {
		// Log status
		getLogger().info("Done!");
	}

	public RadioAPI getAPI() {
		return api;
	}
	
	private void createRecipe() {
		// Create recipe
		Settings.recipe = new ShapedRecipe(new NamespacedKey(this, "radio"), Radio.createRadio(1));
		Settings.recipe.shape(" g ", "bli", "rrr");
		Settings.recipe.setIngredient('g', Material.THIN_GLASS);
		Settings.recipe.setIngredient('b', Material.STONE_BUTTON);
		Settings.recipe.setIngredient('l', Material.REDSTONE_LAMP_OFF);
		Settings.recipe.setIngredient('i', Material.IRON_FENCE);
		Settings.recipe.setIngredient('r', Material.REDSTONE);
		getServer().addRecipe(Settings.recipe);
	}
	
}
