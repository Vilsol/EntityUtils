package com.vilsol.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;

import com.vilsol.EntityUtils;
import com.vilsol.utils.Utils;

public class PistonListener implements Listener {

	private EntityUtils plugin;
	
	public PistonListener(EntityUtils plugin){
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPistonExtend(BlockPistonExtendEvent evt){
        if(!plugin.getConfig().getBoolean("Settings.Piston.Enabled")) return;
        Utils.pushEntities(evt.getBlock().getRelative(evt.getDirection()).getLocation(), evt.getDirection());
	}
	
}
