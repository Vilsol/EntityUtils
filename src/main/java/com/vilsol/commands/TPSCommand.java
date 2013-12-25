package com.vilsol.commands;

import java.text.DecimalFormat;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.vilsol.EntityUtils;
import com.vilsol.utils.Utils;

public class TPSCommand implements CommandExecutor {

	private EntityUtils plugin;
	
	public TPSCommand(EntityUtils tenJava) {
		this.plugin = tenJava;
	}

	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		arg0.sendMessage(Utils.prefix + "Current TPS: " + new DecimalFormat("#.#").format(plugin.getTpsCounter().getAverageTPS()));
		arg0.sendMessage(Utils.prefix + "Entity Count: " + plugin.getCalc().getAllEntities().size());
		return true;
	}

}
