package com.github.rdiaz002.fallingPlugs;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import org.bukkit.event.player.PlayerMoveEvent;

import org.bukkit.material.MaterialData;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class fallingPlugs extends JavaPlugin implements Listener {

	private final int radius = 5;

	public fallingPlugs() {

	}

	@Override
	public void onEnable() {
		// TODO Auto-generated method stub
		getServer().getPluginManager().registerEvents(this, this);
		super.onEnable();
	}

	@Override
	public void onDisable() {
		// TODO Auto-generated method stub
		super.onDisable();
	}

	@EventHandler
	public void onPlayMove(PlayerMoveEvent event) {

		Player player = event.getPlayer();
		int max_X = player.getLocation().getBlockX() + radius;
		int max_Z = player.getLocation().getBlockZ() + radius;
		int min_X = player.getLocation().getBlockX() - radius;
		int min_Z = player.getLocation().getBlockZ() - radius;

		new BukkitRunnable() { // Creates a new thread that runs on the next tic.

			@Override
			public void run() {
				for (int i = min_X; i < max_X; i++) { // Go through all blocks within the radius of the player and
														// update them if they have air beneath them.
					for (int j = 0; j < 256; j++) {
						for (int k = min_Z; k < max_Z; k++) {
							Location loc = new Location(player.getWorld(), i + 0.5, j, k + 0.5);
							Block fallingBlock = loc.getBlock();
							if (allowedTypes(fallingBlock) && airBeneathBlock(loc)) {
								MaterialData mat = new MaterialData(loc.getBlock().getType());
								fallingBlock.setType(Material.AIR); // Deletes the block
								player.getWorld().spawnFallingBlock(loc, mat); // creates new falling block of the
																				// previous type
							}

						}
					}
				}

			}
		}.runTask(this);

	}

	public boolean withinRadius(Location bloc) {
		for (Player player : getServer().getOnlinePlayers()) {
			if (player.getLocation().distance(bloc) < 20) {
				return true;
			}
		}
		return false;
	}

	public boolean airBeneathBlock(Location loc) {
		Location temp = new Location(loc.getWorld(), loc.getX(), loc.getY() - 1, loc.getZ());
		if (temp.getBlock().getType() == Material.AIR) {
			return true;
		}
		return false;
	}
	
	public boolean allowedTypes(Block bloc) {
		if (bloc.getType() != Material.AIR && bloc.getType() != Material.BEDROCK
				&& bloc.getType() != Material.WATER && bloc.getType()!=Material.OBSIDIAN && bloc.getType()!=Material.LAVA) {
			return true;
		}
		return false;
	}

}
