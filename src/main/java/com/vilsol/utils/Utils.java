package com.vilsol.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.vilsol.EntityUtils;

public class Utils {

	public static String prefix = ChatColor.GOLD + "[" + ChatColor.AQUA + "E"
			+ ChatColor.GOLD + "] " + ChatColor.AQUA;
	public static String prefixe = ChatColor.GOLD + "[" + ChatColor.AQUA + "E"
			+ ChatColor.GOLD + "] " + ChatColor.DARK_RED;

	/**
	 * Converts the provided face to a vector that is pointing in that direction
	 *
	 * @param face
	 * @return Vector with the direction
	 */
	public static Vector faceToForce(BlockFace face) {
		Vector out = new Vector(0, 0, 0);
		if (face == BlockFace.NORTH)
			out.setZ(-0.01);
		if (face == BlockFace.SOUTH)
			out.setZ(0.01);
		if (face == BlockFace.EAST)
			out.setX(0.01);
		if (face == BlockFace.WEST)
			out.setX(-0.01);
		if (face == BlockFace.UP)
			out.setY(0.01);
		if (face == BlockFace.DOWN)
			out.setY(-0.01);
		return out;
	}

	/**
	 * Returns a vector that points to the center of the block
	 *
	 * @param align
	 * @return Vector with the direction
	 */
	public static Vector center(Location align) {
		Vector out = new Vector(0, 0, 0);
		if (align.getX() > align.getBlockX() + 0.5)
			out.setX(-0.1);
		if (align.getX() < align.getBlockX() + 0.5)
			out.setX(0.1);
		if (align.getZ() > align.getBlockZ() + 0.5)
			out.setZ(-0.1);
		if (align.getZ() < align.getBlockZ() + 0.5)
			out.setZ(0.1);
		return out;
	}

	/**
	 * Returns a vector that points to the center of the block excluding the
	 * selected face
	 *
	 * @param align
	 * @param face
	 * @return Vector with the direction
	 */
	public static Vector centerExcludeFace(Location align, BlockFace face) {
		Vector out = new Vector(0, 0, 0);
		if (face == BlockFace.NORTH || face == BlockFace.SOUTH) {
			if (align.getX() > align.getBlockX() + 0.5)
				out.setX(-0.1);
			if (align.getX() < align.getBlockX() + 0.5)
				out.setX(0.1);
		}

		if (face == BlockFace.EAST || face == BlockFace.WEST) {
			if (align.getZ() > align.getBlockZ() + 0.5)
				out.setZ(-0.1);
			if (align.getZ() < align.getBlockZ() + 0.5)
				out.setZ(0.1);
		}

		return out;
	}

	/**
	 * Powers the block activating any redstone around
	 *
	 * @param location
	 */
	@SuppressWarnings("deprecation")
	public static void powerBlock(Location location) {
		if (!EntityUtils.getPlugin().getConfig()
				.getBoolean("Settings.Other.Redstone"))
			return;
		final Block b = location.getBlock();

		if (b.getRelative(BlockFace.NORTH).getType() == Material.REDSTONE_WIRE) {
			b.getRelative(BlockFace.NORTH).setData((byte) 15);
			Bukkit.getScheduler().scheduleSyncDelayedTask(EntityUtils.getPlugin(),
					new Runnable() {
						public void run() {
							b.getRelative(BlockFace.NORTH).setData((byte) 0);
						}
					}, 5L);
		} else if (b.getRelative(BlockFace.EAST).getType() == Material.REDSTONE_WIRE) {
			b.getRelative(BlockFace.EAST).setData((byte) 15);
			Bukkit.getScheduler().scheduleSyncDelayedTask(EntityUtils.getPlugin(),
					new Runnable() {
						public void run() {
							b.getRelative(BlockFace.EAST).setData((byte) 0);
						}
					}, 5L);
		} else if (b.getRelative(BlockFace.SOUTH).getType() == Material.REDSTONE_WIRE) {
			b.getRelative(BlockFace.SOUTH).setData((byte) 15);
			Bukkit.getScheduler().scheduleSyncDelayedTask(EntityUtils.getPlugin(),
					new Runnable() {
						public void run() {
							b.getRelative(BlockFace.SOUTH).setData((byte) 0);
						}
					}, 5L);
		} else if (b.getRelative(BlockFace.WEST).getType() == Material.REDSTONE_WIRE) {
			b.getRelative(BlockFace.WEST).setData((byte) 15);
			Bukkit.getScheduler().scheduleSyncDelayedTask(EntityUtils.getPlugin(),
					new Runnable() {
						public void run() {
							b.getRelative(BlockFace.WEST).setData((byte) 0);
						}
					}, 5L);
		}
	}

	/**
	 *
	 * All entities that are in this location will get blasted away in the
	 * blockface direction
	 *
	 * @param location
	 * @param face
	 */
	public static void pushEntities(Location location, BlockFace face) {
		for (Entity e : location.getWorld().getEntities()) {
			if (e.getLocation().getBlockX() == location.getBlockX()
					&& e.getLocation().getBlockY() == location.getBlockY()
					&& e.getLocation().getBlockZ() == location.getBlockZ()) {
				e.setVelocity(e.getVelocity().add(
						Utils.faceToForce(face).multiply(100 * EntityUtils.getPlugin().getConfig().getDouble("Settings.Piston.Force"))));
			}
		}
	}

	public static void cleanupEntities() {
		new BukkitRunnable(){
			public void run() {
				int i = 0;
				if(EntityUtils.getPlugin().getCalc().getAllEntities().size() == 0) this.cancel();
				for(Entity e : EntityUtils.getPlugin().getCalc().getAllEntities()){
					if(e instanceof Player) continue;
					if(i == 10) return;
					e.remove();
					i++;
				}
				this.cancel();
			}
		}.runTaskTimer(EntityUtils.getPlugin(), 0L, 1L);
	}

}
