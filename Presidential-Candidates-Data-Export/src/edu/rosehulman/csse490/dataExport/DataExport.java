package edu.rosehulman.csse490.dataExport;

public class DataExport
{
	// Exporting data from the following projects:
	// - N-grams MapReduce job output (HDFS: /tmp/Output/N-gram/<candidate name>/<part-...>)
	// - Retweet MapReduce job output (HDFS: /tmp/Output/Retweet/retweets.txt)
	// - Storm analysis output (local: /tmp/Tweets/<candidate name>.txt)
	
	public static void main(String[] args)
	{
		System.out.println("Export tool is starting...");
		new NGramExport();
		new RetweetExport();
		new StormExport();
		System.out.println("All export processes started.");
	}
}