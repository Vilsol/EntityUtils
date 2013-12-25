package com.vilsol;

import java.io.IOException;

import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;

import com.vilsol.commands.CleanupCommand;
import com.vilsol.listeners.PistonListener;
import com.vilsol.tasks.EntityCalculator;

public class EntityUtils extends JavaPlugin {

	private static EntityUtils plugin;
	private EntityCalculator calc;

	public void onEnable() {
		plugin = this;
		saveDefaultConfig();
		calc = new EntityCalculator(this);
		// Run every 2 ticks
		calc.runTaskTimer(this, 0L, 2L);
		getServer().getPluginManager().registerEvents(new PistonListener(this), this);
		getCommand("cleanup").setExecutor(new CleanupCommand(this));
		
		try {
		    Metrics metrics = new Metrics(this);
		    metrics.start();
		} catch (IOException e) { }
	}

	public void onDisable() {
		
	}

	/**
	 * Returns the plugin instance
	 * @return
	 */
	public static EntityUtils getPlugin() {
		return plugin;
	}

	/**
	 * Returns the entity calculator
	 * @return
	 */
	public EntityCalculator getCalc(){
		return calc;
	}
	
}
