package com.vilsol.tasks;

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

import com.vilsol.EntityUtils;
import com.vilsol.utils.Utils;

public class EntityCalculator extends BukkitRunnable {

	private EntityUtils plugin;
	private List<Entity> following = new ArrayList<Entity>();
	
	public EntityCalculator(EntityUtils plugin){
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
		if(plugin.getTpsCounter().getAverageTPS() < plugin.getConfig().getDouble("Settings.Other.MinimumTPS")) return;
		
		following.addAll(getAllEntities());
		Iterator<Entity> iterator = following.iterator();
		while(iterator.hasNext()){
			Entity i = iterator.next();

			if(i.isDead() || !i.isValid() || i instanceof Player){
				iterator.remove();
				continue;
			}

			Block myBlock = i.getLocation().getBlock();

			if(myBlock.getType() != Material.AIR){
				if(myBlock.getType() == Material.DIODE_BLOCK_ON){
					if(!plugin.getConfig().getBoolean("Settings.Repeater.Enabled")) continue;
					Diode b = (Diode) myBlock.getState().getData();
					Utils.powerBlock(i.getLocation());
					
					if(myBlock.getRelative(b.getFacing()).getType() != Material.DIODE_BLOCK_ON){
						Material m = myBlock.getRelative(b.getFacing()).getType();
						if(m == Material.WOOD_STAIRS){
							if(!plugin.getConfig().getBoolean("Settings.StairCannon.Enabled")) continue;
							if(i instanceof Item){
								i.setVelocity(Utils.faceToForce(b.getFacing()).multiply(100 * plugin.getConfig().getDouble("Settings.StairCannon.Force")).add(new Vector(0, 1, 0)));
							}else{
								i.setVelocity(Utils.faceToForce(b.getFacing()).multiply(400 * plugin.getConfig().getDouble("Settings.StairCannon.Force")).add(new Vector(0, 1, 0)));
							}
							continue;
						}else{
							if(!plugin.getConfig().getBoolean("Settings.Teleporter.Enabled")) continue;
							
							if(m == Material.IRON_BLOCK){
								i.teleport(i.getLocation().add(Utils.faceToForce(b.getFacing()).multiply(plugin.getConfig().getDouble("Settings.Teleporter.Iron")*100)));
							}else if(m == Material.REDSTONE_BLOCK){
								i.teleport(i.getLocation().add(Utils.faceToForce(b.getFacing()).multiply(plugin.getConfig().getDouble("Settings.Teleporter.Redstone")*100)));
							}else if(m == Material.GOLD_BLOCK){
								i.teleport(i.getLocation().add(Utils.faceToForce(b.getFacing()).multiply(plugin.getConfig().getDouble("Settings.Teleporter.Gold")*100)));
							}else if(m == Material.LAPIS_BLOCK){
								i.teleport(i.getLocation().add(Utils.faceToForce(b.getFacing()).multiply(plugin.getConfig().getDouble("Settings.Teleporter.Lapis")*100)));
							}else if(m == Material.DIAMOND_BLOCK){
								i.teleport(i.getLocation().add(Utils.faceToForce(b.getFacing()).multiply(plugin.getConfig().getDouble("Settings.Teleporter.Diamond")*100)));
							}else if(m == Material.EMERALD_BLOCK){
								i.teleport(i.getLocation().add(Utils.faceToForce(b.getFacing()).multiply(plugin.getConfig().getDouble("Settings.Teleporter.Emerald")*100)));
							}else{
								if(myBlock.getRelative(BlockFace.DOWN).getType() == Material.SNOW_BLOCK && plugin.getConfig().getBoolean("Settings.Repeater.FastEnabled")){
									i.setVelocity(Utils.faceToForce(b.getFacing()).multiply(30 * plugin.getConfig().getDouble("Settings.Repeater.Fast")).add(Utils.centerExcludeFace(i.getLocation(), b.getFacing()).multiply(0.5)));
								}else{
									i.setVelocity(Utils.faceToForce(b.getFacing()).multiply(30 * plugin.getConfig().getDouble("Settings.Repeater.Normal")).add(Utils.centerExcludeFace(i.getLocation(), b.getFacing()).multiply(0.5)));
								}
							}
						}
					}else{
						if(myBlock.getRelative(BlockFace.DOWN).getType() == Material.SNOW_BLOCK && plugin.getConfig().getBoolean("Settings.Repeater.FastEnabled")){
							i.setVelocity(Utils.faceToForce(b.getFacing()).multiply(30 * plugin.getConfig().getDouble("Settings.Repeater.Fast")).add(Utils.centerExcludeFace(i.getLocation(), b.getFacing()).multiply(0.7)));
						}else{
							i.setVelocity(Utils.faceToForce(b.getFacing()).multiply(30 * plugin.getConfig().getDouble("Settings.Repeater.Normal")).add(Utils.centerExcludeFace(i.getLocation(), b.getFacing()).multiply(0.7)));
						}
					}
					
				}else if(myBlock.getType() == Material.LADDER){
					if(!plugin.getConfig().getBoolean("Settings.Other.Ladder")) continue;
					Ladder l = (Ladder) myBlock.getState().getData();
					
					if(myBlock.getRelative(BlockFace.UP, 2).getType() == Material.LADDER && myBlock.getRelative(BlockFace.UP).getType() == Material.LADDER){
						if(i.getVelocity().getY() > 1) continue;
						Ladder lu = (Ladder) myBlock.getRelative(BlockFace.UP).getState().getData();
						i.setVelocity(new Vector(0, 1, 0).add(Utils.center(i.getLocation()).multiply(0.3)).add(Utils.faceToForce(lu.getAttachedFace()).multiply(50)));
					}else{
						i.teleport(i.getLocation().add(0, 2, 0).add(Utils.faceToForce(l.getAttachedFace()).multiply(100)));
						i.setVelocity(new Vector(0, 0, 0));
					}
					
					Utils.powerBlock(i.getLocation());
				}	
			}else if(myBlock.getRelative(BlockFace.DOWN).getType() == Material.CHEST){
				if(!plugin.getConfig().getBoolean("Settings.Other.ChestEater")) continue;
				if(i instanceof Item){
					Chest c = (Chest) myBlock.getRelative(BlockFace.DOWN).getState();
					c.getInventory().addItem(((Item) i).getItemStack());
					i.remove();
					Utils.powerBlock(i.getLocation());
				}
			}
		}
	}

}
