package com.github.pocketkid2.radio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;

public class RadioAPI {
	
	// Main constructor
	public RadioAPI(RadioPlugin pl) {
		channels = new HashMap<Integer, List<Player>>();
	}
	
	// The data structure for channels
	private Map<Integer, List<Player>> channels;
	
	/**
	 * Attempts to add this player to the channel
	 *
	 * @param c
	 * @param p
	 */
	public void addPlayer(Integer c, Player p) {
		// Check if the integer is a valid positive three-digit value
		if (c < 0 || c > 999) return;
		if (channels.containsKey(c)) {
			List<Player> players = channels.get(c);
			players.add(p);
		} else {
			List<Player> players = new ArrayList<Player>();
			players.add(p);
			channels.put(c, players);
		}
	}
	
	/**
	 * Attempts to remove this player to the channel
	 *
	 * @param c
	 * @param p
	 */
	public void removePlayer(Integer c, Player p) {
		// Check if the integer is a valid positive three-digit value
		if (c < 0 || c > 999) return;
		if (channels.containsKey(c)) {
			List<Player> players = channels.get(c);
			if (players.contains(p)) {
				players.remove(p);
			}
		}
	}
	
	/**
	 * Determines whether the player is in the given channel right now
	 *
	 * @param c
	 * @param p
	 * @return
	 */
	public boolean isPlayerInChannel(Integer c, Player p) {
		// Check if the integer is a valid positive three-digit value
		if (c < 0 || c > 999) return false;
		if (channels.containsKey(c)) {
			List<Player> players = channels.get(c);
			return players.contains(p);
		} else return false;
	}

	/**
	 * Gets a list of all players in the given channel
	 *
	 * @param c
	 * @param p
	 * @return null if the channel integer is wrong, a list otherwise
	 */
	public List<Player> getAllPlayersInChannel(Integer c, Player p) {
		// Check if the integer is a valid positive three-digit value
		if (c < 0 || c > 999) return null;
		if (channels.containsKey(c)) return channels.get(c);
		else {
			List<Player> players = new ArrayList<Player>();
			channels.put(c, players);
			return players;
		}
	}
	
}
