package edu.rosehulman.csse490.dataExport;

public class RetweetExport implements TimerListener
{
	public RetweetExport()
	{
		Timer timer = new Timer(60);
		timer.addTimerListener(this);
		timer.start();
	}
	
	@Override
	public void timerFired()
	{
		System.out.println("[RetweetExport] Exporting retweet data for all candidates...");
		new ProcessRunner("hadoop",
				"fs",
				"-get",
				"/tmp/Output/Retweet/part*",
				"/var/www/html/Retweet/retweets.txt");
		System.out.println("[RetweetExport] -----------------------------------------------------------");
	}
}