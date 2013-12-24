package com.vilsol.tenjava;

import org.bukkit.plugin.java.JavaPlugin;

import com.vilsol.tenjava.commands.CleanupCommand;
import com.vilsol.tenjava.listeners.PistonListener;
import com.vilsol.tenjava.tasks.EntityCalculator;

public class TenJava extends JavaPlugin {

	private static TenJava plugin;
	private EntityCalculator calc;

	public void onEnable() {
		plugin = this;
		saveDefaultConfig();
		calc = new EntityCalculator(this);
		// Run every 2 ticks
		calc.runTaskTimer(this, 0L, 2L);
		getServer().getPluginManager().registerEvents(new PistonListener(this), this);
		getCommand("cleanup").setExecutor(new CleanupCommand(this));
	}

	public void onDisable() {
		
	}

	/**
	 * Returns the plugin instance
	 * @return
	 */
	public static TenJava getPlugin() {
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
