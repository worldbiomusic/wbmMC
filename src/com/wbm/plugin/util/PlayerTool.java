package com.wbm.plugin.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;

import com.wbm.plugin.WbmMC;

public class PlayerTool {
	public static Collection<? extends Player> onlinePlayers() {
		return Bukkit.getOnlinePlayers();
	}

	public static boolean isPlayerOnline(Player p) {
		return p != null;
	}

	public static boolean isPlayerOnline(UUID uuid) {
		return Bukkit.getPlayer(uuid) != null;
	}

	public static boolean isPlayerOnline(String name) {
		return Bukkit.getPlayer(name) != null;
	}

	public static int onlinePlayersCount() {
		return onlinePlayers().size();
	}

	public static void makePureState(Player p) {
		// 1. 체력 회복
		// 2. 배고픔 회복
		// 3. hide 제거
		// 4. glowing 제거
		// 5. 포션 효과 제거
		heal(p);
		removeAllState(p);
	}

	public static void heal(Player p) {
		p.setHealth(p.getHealthScale());
		p.setFoodLevel(20);
	}

	public static void removeAllState(Player p) {
		// unhide
		unhidePlayerFromEveryone(p);
		// set glow off
		p.setGlowing(false);
		// 모든 포션효과 제거
		removeAllPotionEffects(p);
	}

	public static void setHungry(Player p, int amount) {
		p.setFoodLevel(amount);
	}

	public static void hidePlayerFromAnotherPlayer(Player hideTarget, Player anotherPlayer) {
		anotherPlayer.hidePlayer(WbmMC.getInstance(), hideTarget);
	}

	public static void hidePlayerFromOtherPlayers(Player hideTarget, List<Player> others) {
		for (Player other : others) {
			hidePlayerFromAnotherPlayer(hideTarget, other);
		}
	}

	public static void hidePlayerFromEveryone(Player hideTarget) {
		for (Player all : Bukkit.getOnlinePlayers()) {
			hidePlayerFromAnotherPlayer(hideTarget, all);
		}
	}

	public static void unhidePlayerFromAnotherPlayer(Player unhideTarget, Player anotherPlayer) {
		anotherPlayer.showPlayer(WbmMC.getInstance(), unhideTarget);
	}

	public static void unhidePlayerFromOtherPlayers(Player unhideTarget, List<Player> others) {
		for (Player other : others) {
			unhidePlayerFromAnotherPlayer(unhideTarget, other);
		}
	}

	public static void unhidePlayerFromEveryone(Player unhideTarget) {
		for (Player all : Bukkit.getOnlinePlayers()) {
			unhidePlayerFromAnotherPlayer(unhideTarget, all);
		}
	}

	public static void playSound(Player p, Sound sound) {
		p.playSound(p.getLocation(), sound, 10, 1);
	}

	public static void playSoundToEveryone(Sound sound) {
		for (Player p : Bukkit.getOnlinePlayers()) {
			playSound(p, sound);
		}
	}

	public static void removeAllPotionEffects(Player p) {
		for (PotionEffect effect : p.getActivePotionEffects()) {
			p.removePotionEffect(effect.getType());
		}
	}

	public static boolean isHidden(Player target) {
		for (Player other : Bukkit.getOnlinePlayers()) {
			if (!(other.equals(target)) && other.canSee(target)) {
				return false;
			}
		}
		return true;
	}

	public static List<Player> getPlayersCannotSeeTarget(Player target) {
		// return who can't see target player
		List<Player> canNotSeePlayers = new ArrayList<>();
		for (Player other : Bukkit.getOnlinePlayers()) {
			if (!(other.equals(target)) && !(other.canSee(target))) {
				canNotSeePlayers.add(other);
			}
		}

		return canNotSeePlayers;
	}

	@SuppressWarnings("deprecation")
	public static ItemStack getPlayerHead(Player p) {
		boolean isNewVersion = Arrays.stream(Material.values()).map(Material::name).collect(Collectors.toList())
				.contains("PLAYER_HEAD");
		Material type = Material.matchMaterial(isNewVersion ? "PLAYER_HEAD" : "SKULL_ITEM");
		ItemStack item = new ItemStack(type, 1);

		if (!isNewVersion) {
			item.setDurability((short) 3);
		}
		SkullMeta meta = (SkullMeta) item.getItemMeta();
		meta.setOwningPlayer(p);

		item.setItemMeta(meta);

		return item;

	}

	public static String getPlayersNameString(List<Player> players, String delimiter) {
		String members = "";

		Player lastPlayer = players.get(players.size() - 1);
		for (Player p : players) {
			members += p.getName();
			if (!lastPlayer.equals(p)) {
				members += delimiter + " ";
			}
		}
		return members;

	}
	
}

//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
