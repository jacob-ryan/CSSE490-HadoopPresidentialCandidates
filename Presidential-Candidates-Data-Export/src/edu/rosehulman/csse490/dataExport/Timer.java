package edu.rosehulman.csse490.dataExport;

import java.util.*;

public class Timer extends Thread
{
	private int seconds;
	private List<TimerListener> listeners;
	
	public Timer(int seconds)
	{
		this.seconds = seconds;
		this.listeners = new ArrayList<TimerListener>();
	}
	
	public void addTimerListener(TimerListener listener)
	{
		this.listeners.add(listener);
	}
	
	@Override
	public void run()
	{
		try
		{
			while (true)
			{
				for (TimerListener listener : this.listeners)
				{
					listener.timerFired();
				}
				Thread.sleep(this.seconds * 1000);
			}
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
}