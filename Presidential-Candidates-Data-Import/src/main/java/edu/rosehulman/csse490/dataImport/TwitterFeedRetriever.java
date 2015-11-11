package edu.rosehulman.csse490.dataImport;

import java.io.*;
import java.text.*;
import java.util.*;

import twitter4j.*;

public class TwitterFeedRetriever
{
	protected T4JWrapper twitter;
	private TwitterRateLimitViewer limits;
	private Politicians candidates;
	protected int tweetsPerCandidate;

	public static void main(String[] args) throws TwitterException, IOException, InterruptedException
	{
		TwitterFeedRetriever tweets = new TwitterFeedRetriever();
		
		tweets.WriteAllPoliticianSearchTweets();
		tweets.GetAllPoliticianTimelineTweets();
	}

	public TwitterFeedRetriever() throws TwitterException
	{
		this.twitter = new T4JWrapper();
		this.limits = new TwitterRateLimitViewer();
		this.candidates = new Politicians();
		this.tweetsPerCandidate = 100;
	}

	public void WriteAllPoliticianSearchTweets() throws TwitterException, IOException, InterruptedException
	{
		HashMap<String, ArrayList<String>> politicianMap = this.candidates.GetAllPoliticians();

		for (String key : politicianMap.keySet())
		{
			for (String username : politicianMap.get(key))
			{
				String dateString = new SimpleDateFormat("yyyy-MM-dd_HH-mm").format(new Date());
				String localOutput = "./TweetData/" + username + "/static-" + dateString + ".txt";
				TwitterWriter writer = new TwitterWriter(localOutput);

				int callsRemaining = this.limits.GetSearchTweetsRemainingCalls(this.twitter.getTwitterObject());
				System.out.println("API search calls remaining: " + callsRemaining);

				if (callsRemaining >= tweetsPerCandidate / 100)
				{
					System.out.println("Getting tweets for: " + username);
					new Launcher(username, writer, localOutput);
				}
			}
		}

		System.out.println("Ending API calls remaining:");
		this.limits.printSearchCallsUsage(this.twitter.getTwitterObject());
	}

	public void GetAllPoliticianTimelineTweets() throws TwitterException, IOException, InterruptedException
	{
		HashMap<String, ArrayList<String>> politicianMap = this.candidates.GetAllPoliticians();
		
		String dateString = new SimpleDateFormat("yyyy-MM-dd_HH-mm").format(new Date());
		String localOutput = "./RetweetData/retweets-" + dateString + ".txt";
		TwitterWriter writer = new TwitterWriter(localOutput);

		for (String key : politicianMap.keySet())
		{
			for (String username : politicianMap.get(key))
			{
				int callsRemaining = this.limits.GetUserTimeLineRemainingCalls(this.twitter.getTwitterObject());
				System.out.println("API user timeline calls remaining: " + callsRemaining);

				if (callsRemaining >= this.tweetsPerCandidate / 200)
				{
					System.out.println("Getting retweets for: " + username);
					
					ArrayList<Status> tweets = this.twitter.getUserTimeline("@" + username, this.tweetsPerCandidate);
					writer.writeRetweets(tweets, username);
				}
			}
		}
		writer.close();

		System.out.println("Ending API calls remaining:");
		this.limits.printGetUserTimelineUsage(this.twitter.getTwitterObject());

		String hdfsOutput = "/tmp/RetweetData";
		HDFS.uploadToHDFS(localOutput, hdfsOutput);
	}
	
	private class Launcher extends Thread
	{
		private String candidateName;
		private TwitterWriter writer;
		private String localOutput;
		
		public Launcher(String candidateName, TwitterWriter writer, String localOutput)
		{
			this.writer = writer;
			this.candidateName = candidateName;
			this.localOutput = localOutput;
			start();
		}
		
		@Override
		public void run()
		{
			try
			{
				ArrayList<Status> tweets = TwitterFeedRetriever.this.twitter.searchAndReturnTweets("@" + candidateName, TwitterFeedRetriever.this.tweetsPerCandidate);
				writer.writeTweets(tweets);
				writer.close();
				
				String hdfsPath = "/tmp/TweetData/" + candidateName;
				HDFS.uploadToHDFS(this.localOutput, hdfsPath);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}