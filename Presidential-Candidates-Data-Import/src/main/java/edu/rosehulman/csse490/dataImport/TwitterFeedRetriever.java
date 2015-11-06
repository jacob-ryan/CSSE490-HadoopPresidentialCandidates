package edu.rosehulman.csse490.dataImport;

import java.io.IOException;
import java.text.*;
import java.util.*;

import twitter4j.Status;
import twitter4j.TwitterException;

public class TwitterFeedRetriever
{
	private T4JWrapper twitter;
	private TwitterRateLimitViewer limits;
	private Politicians candidates;

	public static void main(String[] args) throws TwitterException, IOException, InterruptedException
	{
		TwitterFeedRetriever tweets = new TwitterFeedRetriever();

		//tweets.WriteAllPoliticianSearchTweets(50);
		tweets.limits.printSearchCallsUsage(tweets.twitter.getTwitterObject());
		tweets.GetAllPoliticianTimelineTweets(10);
	}

	public TwitterFeedRetriever()
	{
		twitter = new T4JWrapper();
		limits = new TwitterRateLimitViewer();
		candidates = new Politicians();
	}
	
	public void WriteAllPoliticianSearchTweets(int tweetsPerCandidate) throws TwitterException, IOException, InterruptedException
	{
		HashMap<String, ArrayList<String>> politicianMap = candidates.GetAllPoliticians();
		
		for (String key : politicianMap.keySet())
		{
			for (String username : politicianMap.get(key))
			{
				String localOutput = "./Tweet Data/" + username + ".txt";
				TwitterWriter writer = new TwitterWriter(localOutput);
				
				int callsRemaining = limits.GetSearchTweetsRemainingCalls(twitter.getTwitterObject());
				System.out.println("API calls remaining: " + callsRemaining);
				
				if (callsRemaining >= tweetsPerCandidate / 200)
				{
					System.out.println("Getting tweets for: " + username);

					ArrayList<Status> tweets = twitter.searchAndReturnTweets("@"+username, tweetsPerCandidate);
					writer.writeTweets(tweets);
					
					String dateString = new SimpleDateFormat("yyyy-MM-dd-HH:mm").format(new Date());
					String hdfsOutput = "/tmp/TweetData/" + username + "/static-" + dateString + ".txt";
					HDFS.uploadToHDFS(localOutput, hdfsOutput);
				}
			}
		}
		
		System.out.println("Ending API calls remaining:");
		limits.printSearchCallsUsage(twitter.getTwitterObject());
	}

	public void GetAllPoliticianTimelineTweets(int tweetsPerCandidate) throws TwitterException, IOException, InterruptedException
	{
		HashMap<String, ArrayList<String>> politicianMap = candidates.GetAllPoliticians();

		String localOutput = "./Retweet Data/retweets.txt";
		TwitterWriter writer = new TwitterWriter(localOutput);
		
		for (String key : politicianMap.keySet())
		{
			for (String username : politicianMap.get(key))
			{				
				int callsRemaining = limits.GetUserTimeLineRemainingCalls(twitter.getTwitterObject());
				System.out.println("API calls remaining: " + callsRemaining);
				
				if (callsRemaining >= tweetsPerCandidate / 200)
				{
					System.out.println("Getting retweets for: " + username);

					ArrayList<Status> tweets = twitter.getUserTimeline("@"+username, tweetsPerCandidate);
					writer.writeRetweets(tweets, username);
				}
			}
		}

		System.out.println("Ending API calls remaining:");
		limits.printGetUserTimelineUsage(twitter.getTwitterObject());
		
		String hdfsOutput = "/tmp/RetweetData/retweets.txt";
		HDFS.uploadToHDFS(localOutput, hdfsOutput);
	}
}