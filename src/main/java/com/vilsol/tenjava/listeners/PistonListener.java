package com.vilsol.tenjava.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;

import com.vilsol.tenjava.TenJava;
import com.vilsol.tenjava.utils.Utils;

public class PistonListener implements Listener {

	private TenJava plugin;
	
	public PistonListener(TenJava plugin){
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPistonExtend(BlockPistonExtendEvent evt){
		if(!plugin.getConfig().getBoolean("Settings.Movement.Piston")) return;
		Utils.pushEntities(evt.getBlock().getRelative(evt.getDirection()).getLocation(), evt.getDirection());
	}
	
}
