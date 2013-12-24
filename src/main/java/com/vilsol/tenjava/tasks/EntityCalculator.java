package com.vilsol.tenjava.tasks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.material.Diode;
import org.bukkit.material.Ladder;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.vilsol.tenjava.TenJava;
import com.vilsol.tenjava.utils.Utils;

public class EntityCalculator extends BukkitRunnable {

	private TenJava plugin;
	private List<Entity> following = new ArrayList<Entity>();
	
	public EntityCalculator(TenJava plugin){
		this.plugin = plugin;
	}
	
	/**
	 * Returns all entities from all worlds
	 * @return
	 */
	public Collection<? extends Entity> getAllEntities(){
		List<Entity> ent = new ArrayList<Entity>();
		for(World w : Bukkit.getWorlds()){
			ent.addAll(w.getEntities());
		}
		return ent;
	}
	
	public void run() {
		following.addAll(getAllEntities());
		Iterator<Entity> iterator = following.iterator();
		while(iterator.hasNext()){
			Entity i = iterator.next();

			if(i.isDead() || !i.isValid() || i instanceof Player){
				iterator.remove();
				continue;
			}

			Block myBlock = i.getLocation().getBlock();

			if(myBlock.getType() == Material.DIODE_BLOCK_ON){
				if(!plugin.getConfig().getBoolean("Settings.Movement.Repeater")) continue;
				Diode b = (Diode) myBlock.getState().getData();

				
				if(myBlock.getRelative(b.getFacing()).getType() != Material.DIODE_BLOCK_ON){
					Material m = myBlock.getRelative(b.getFacing()).getType();
					if(m == Material.WOOD_STAIRS){
						if(!plugin.getConfig().getBoolean("Settings.Movement.StairCannon")) continue;
						if(i instanceof Item){
							i.setVelocity(Utils.faceToForce(b.getFacing()).multiply(100).add(new Vector(0, 1, 0)));
						}else{
							i.setVelocity(Utils.faceToForce(b.getFacing()).multiply(400).add(new Vector(0, 1, 0)));
						}
						continue;
					}else if(m == Material.IRON_BLOCK){
						i.teleport(i.getLocation().add(Utils.faceToForce(b.getFacing()).multiply(500)));
						continue;
					}else if(m == Material.REDSTONE_BLOCK){
						i.teleport(i.getLocation().add(Utils.faceToForce(b.getFacing()).multiply(1000)));
						continue;
					}else if(m == Material.GOLD_BLOCK){
						i.teleport(i.getLocation().add(Utils.faceToForce(b.getFacing()).multiply(1500)));
						continue;
					}else if(m == Material.LAPIS_BLOCK){
						i.teleport(i.getLocation().add(Utils.faceToForce(b.getFacing()).multiply(2000)));
						continue;
					}else if(m == Material.DIAMOND_BLOCK){
						i.teleport(i.getLocation().add(Utils.faceToForce(b.getFacing()).multiply(2500)));
						continue;
					}
				}

				if(myBlock.getRelative(BlockFace.DOWN).getType() == Material.SNOW_BLOCK){
					i.setVelocity(Utils.faceToForce(b.getFacing()).multiply(100).add(Utils.centerExcludeFace(i.getLocation(), b.getFacing()).multiply(0.5)));
				}else{
					i.setVelocity(Utils.faceToForce(b.getFacing()).multiply(30).add(Utils.centerExcludeFace(i.getLocation(), b.getFacing()).multiply(0.5)));
				}
				Utils.powerBlock(i.getLocation());
			}else if(myBlock.getRelative(BlockFace.DOWN).getType() == Material.CHEST){
				if(!plugin.getConfig().getBoolean("Settings.Other.ChestEater")) continue;
				if(i instanceof Item){
					Chest c = (Chest) myBlock.getRelative(BlockFace.DOWN).getState();
					c.getInventory().addItem(((Item) i).getItemStack());
					i.remove();
					Utils.powerBlock(i.getLocation());
				}
			}else if(myBlock.getType() == Material.LADDER){
				if(!plugin.getConfig().getBoolean("Settings.Movement.Ladder")) continue;
				Ladder l = (Ladder) myBlock.getState().getData();
				if(myBlock.getRelative(BlockFace.UP, 2).getType() == Material.LADDER && myBlock.getRelative(BlockFace.UP).getType() == Material.LADDER){
					Ladder lu = (Ladder) myBlock.getRelative(BlockFace.UP).getState().getData();
					i.setVelocity(new Vector(0, 0.5, 0).add(Utils.center(i.getLocation()).multiply(0.3)).add(Utils.faceToForce(lu.getAttachedFace()).multiply(50)));
				}else{
					i.teleport(i.getLocation().add(0, 2, 0).add(Utils.faceToForce(l.getAttachedFace()).multiply(100)));
					i.setVelocity(new Vector(0, 0, 0));
				}
				Utils.powerBlock(i.getLocation());
			}
		}
	}

}
