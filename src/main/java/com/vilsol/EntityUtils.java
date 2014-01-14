package com.vilsol;

import java.io.IOException;

import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;

import com.vilsol.commands.CleanupCommand;
import com.vilsol.commands.TPSCommand;
import com.vilsol.listeners.PistonListener;
import com.vilsol.tasks.EntityCalculator;
import com.vilsol.tasks.TPSCounter;

public class EntityUtils extends JavaPlugin {
	
	private static EntityUtils plugin;
	private EntityCalculator calc;
	private TPSCounter tps;
	
	public void onEnable() {
		plugin = this;
		saveDefaultConfig();
		
		calc = new EntityCalculator(this);
		calc.runTaskTimer(this, 0L, 5L);
		
		getServer().getPluginManager().registerEvents(new PistonListener(this), this);
		getCommand("cleanup").setExecutor(new CleanupCommand(this));
		getCommand("tps").setExecutor(new TPSCommand(this));
		
		tps = new TPSCounter();
		tps.runTaskTimer(this, 1000, 50);
		
		try {
			Metrics metrics = new Metrics(this);
			metrics.start();
		} catch(IOException e) { }
	}
	
	public void onDisable() {
		
	}
	
	/**
	 * Returns the plugin instance
	 * 
	 * @return
	 */
	public static EntityUtils getPlugin() {
		return plugin;
	}
	
	/**
	 * Returns the entity calculator
	 * 
	 * @return
	 */
	public EntityCalculator getCalc() {
		return calc;
	}
	
	/**
	 * Returns the TPS counter
	 * 
	 * @return
	 */
	public TPSCounter getTpsCounter() {
		return tps;
	}
	
}
