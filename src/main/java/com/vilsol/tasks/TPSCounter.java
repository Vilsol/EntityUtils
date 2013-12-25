package com.vilsol.tasks;

import java.util.LinkedList;

import org.bukkit.scheduler.BukkitRunnable;

public class TPSCounter extends BukkitRunnable {
	
	private transient long lastPoll = System.nanoTime();
	private final LinkedList<Double> history = new LinkedList<Double>();
	private final long tickInterval = 50;
	
	public TPSCounter() {
		history.add(20d);
	}
	
	@Override
	public void run() {
		final long startTime = System.nanoTime();
		long timeSpent = (startTime - lastPoll) / 1000;
		timeSpent = ((timeSpent == 0) ? 1 : timeSpent);
		if(history.size() > 10) history.remove();
		double tps = tickInterval * 1000000.0 / timeSpent;
		if(tps <= 21) history.add(tps);
		lastPoll = startTime;
	}
	
	public double getAverageTPS() {
		double avg = 0;
		for(Double f : history) {
			if(f != null) avg += f;
		}
		return avg / history.size();
	}
}
