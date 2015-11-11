package edu.rosehulman.csse490.dataExport;

public class DataExport
{
	// Exporting data from the following projects:
	// - N-grams MapReduce job output (HDFS: /tmp/Output/N-gram/<candidate name>/<part-...>)
	// - Retweet MapReduce job output (HDFS: /tmp/Output/Retweet/part*)
	// - Storm word-count analysis output (local: ./StormWordCount/<candidate name>.txt)
	//
	// Other useful directories:
	// - Storm full-text output (local: ./StormTweets/<candidate name>/storm-<timestamp>.txt)
	// - Input Tweet data (HDFS: /tmp/TweetData/<candidate name>/[static | storm]-<timestamp>.txt)
	
	public static void main(String[] args)
	{
		System.out.println("Export tool is starting...");
		new NGramExport();
		new RetweetExport();
		new StormExport();
		System.out.println("All export processes started.");
	}
}