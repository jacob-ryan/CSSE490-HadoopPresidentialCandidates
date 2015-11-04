package edu.rosehulman.csse490.dataImport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import twitter4j.Status;
import twitter4j.TwitterException;

public class TwitterFeedRetriever
{
	private T4JWrapper twitter;
	private TwitterRateLimitViewer limits;
	private TwitterDataWriter writer;
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
		writer = new TwitterDataWriter();
		candidates = new Politicians();
	}
	
	public void WriteAllPoliticianSearchTweets(int tweetsPerCandidate) throws TwitterException, IOException, InterruptedException
	{
		HashMap<String, ArrayList<String>> politicianMap = candidates.GetAllPoliticians();
		
		String localTweetPath = "TweetData";
		String hdfsPath = "/tmp";
		
		for (String key : politicianMap.keySet())
		{
			for (String username : politicianMap.get(key))
			{
				String localOutput = localTweetPath + "/" + username + ".txt";
				writer.setPath(localOutput);
				
				int callsRemaining = limits.GetSearchTweetsRemainingCalls(twitter.getTwitterObject());
				
				if (callsRemaining - (tweetsPerCandidate / 100) >= 0)
				{
					System.out.println("Getting tweets for " + username);
					System.out.println(callsRemaining - (tweetsPerCandidate / 100));

					ArrayList<Status> tweets = twitter.searchAndReturnTweets("@"+username, tweetsPerCandidate);
					writer.writeTweets(tweets);
				}
				else
				{
					limits.printSearchCallsUsage(twitter.getTwitterObject());
					HDFSUploader.DeleteAndUpload(hdfsPath, localTweetPath);
					return;
				}
			}
		}
		
		limits.printSearchCallsUsage(twitter.getTwitterObject());

		HDFSUploader.DeleteAndUpload(hdfsPath, localTweetPath);
	}

	public void GetAllPoliticianTimelineTweets(int tweetsPerCandidate) throws TwitterException, IOException, InterruptedException
	{
		HashMap<String, ArrayList<String>> politicianMap = candidates.GetAllPoliticians();

		String localTweetPath = "RetweetData";
		String hdfsPath = "/tmp";

		String localOutput = localTweetPath + "/retweets.txt";
		writer.setPath(localOutput);
		
		for (String key : politicianMap.keySet())
		{
			for (String username : politicianMap.get(key))
			{				
				int callsRemaining = limits.GetUserTimeLineRemainingCalls(twitter.getTwitterObject());
				
				if (callsRemaining - (tweetsPerCandidate / 200) >= 0)
				{
					System.out.println("Getting retweets for " + username);
					System.out.println(callsRemaining - (tweetsPerCandidate / 200));

					ArrayList<Status> tweets = twitter.getUserTimeline("@"+username, tweetsPerCandidate);				
					
					writer.writeRetweets(tweets, username);
				}
				else
				{
					limits.GetUserTimeLineRemainingCalls(twitter.getTwitterObject());
					HDFSUploader.DeleteAndUpload(hdfsPath, localTweetPath);
					return;
				}
			}
		}

		limits.printGetUserTimelineUsage(twitter.getTwitterObject());
		
		HDFSUploader.DeleteAndUpload(hdfsPath, localTweetPath);
	}
}