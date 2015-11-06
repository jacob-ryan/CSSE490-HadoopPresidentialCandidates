package edu.rosehulman.csse490.dataExport;

import java.util.*;

import edu.rosehulman.csse490.dataImport.*;

public class NGramExport implements TimerListener
{
	public NGramExport()
	{
		Timer timer = new Timer(60);
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
				System.out.println("[NGramExport] Exporting n-gram data for Twitter handle: " + handle + "...");
				new ProcessRunner("hadoop",
						"fs",
						"-get",
						"/tmp/Output/N-gram/" + handle + "/part*",
						"/var/www/html/N-gram/" + handle + ".txt");
				System.out.println("[NGramExport] -----------------------------------------------------------");
			}
		}
	}
}