package edu.rosehulman.csse490.dataExport;

import java.util.*;

import edu.rosehulman.csse490.dataImport.*;

public class StormExport implements TimerListener
{
	public StormExport()
	{
		Timer timer = new Timer(10);
		timer.addTimerListener(this);
		timer.start();
	}
	
	@Override
	public void timerFired()
	{
		Politicians politicians = new Politicians();
		Collection<ArrayList<String>> candidates = politicians.GetAllPoliticians().values();
		
		for (ArrayList<String> candidate : candidates)
		{
			for (String handle : candidate)
			{
				System.out.println("[StormExport] Exporting Storm data for Twitter handle: " + handle + "...");
				new ProcessRunner("cp",
						"/tmp/Presidential Candidates/StormWordCount/" + handle + ".txt",
						"/var/www/html/Storm/" + handle + ".txt");
				System.out.println("[StormExport] -----------------------------------------------------------");
			}
		}
	}
}