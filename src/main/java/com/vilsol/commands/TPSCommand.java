package com.vilsol.commands;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.google.common.base.Joiner;
import com.vilsol.EntityUtils;
import com.vilsol.utils.Utils;

public class TPSCommand implements CommandExecutor {

	private EntityUtils plugin;
	
	public TPSCommand(EntityUtils tenJava) {
		this.plugin = tenJava;
	}

	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		arg0.sendMessage(Utils.prefix + "Current TPS: " + ChatColor.GREEN + Double.parseDouble(new DecimalFormat("#.#").format(plugin.getTpsCounter().getAverageTPS())) * 1.0);
		arg0.sendMessage(Utils.prefix + "Total Entity Count: " + ChatColor.GREEN + plugin.getCalc().getAllEntities().size());
		
		List<String> disabled = new ArrayList<String>();
		for(World w : Bukkit.getWorlds()){
			if(w.getLoadedChunks().length == 0){
				disabled.add(ChatColor.RED + w.getName());
			}else{
				arg0.sendMessage(Utils.prefix + ChatColor.GREEN + w.getName() + ChatColor.AQUA + " - Chunks: " + ChatColor.GREEN + w.getLoadedChunks().length + ChatColor.AQUA + ", Entities: " + ChatColor.GREEN + w.getEntities().size());
			}
		}
		
		if(disabled.size() > 0){
			arg0.sendMessage(Utils.prefixe + "Unloaded: " + Joiner.on(ChatColor.DARK_RED + ", ").join(disabled));
		}
		return true;
	}

}
