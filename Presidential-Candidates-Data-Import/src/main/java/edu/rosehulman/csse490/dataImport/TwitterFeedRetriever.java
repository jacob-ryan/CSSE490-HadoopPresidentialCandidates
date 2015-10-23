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

		tweets.GetAllPoliticianTimelineTweets();

		T4JWrapper t4j = new T4JWrapper();
		TwitterRateLimitViewer limit = new TwitterRateLimitViewer();
		limit.printGetUserTimelineUsage(t4j.getTwitterObject());
	}

	public TwitterFeedRetriever()
	{
		twitter = new T4JWrapper();
		limits = new TwitterRateLimitViewer();
		writer = new TwitterDataWriter();
		candidates = new Politicians();
	}

	public void GetAllPoliticianTimelineTweets() throws TwitterException, IOException, InterruptedException
	{
		HashMap<String, ArrayList<String>> politicianMap = candidates.GetAllPoliticians();
		int callsRemaining = limits.GetUserTimeLineRemainingCalls(twitter.getTwitterObject());

		String localTweetPath = "Tweets";
		String hdfsPath = "/tmp/output";

		// get tweets and write them out
		for (String key : politicianMap.keySet())
		{
			for (String username : politicianMap.get(key))
			{
				String localOutput = localTweetPath + "/" + username + ".txt";
				writer.setPath(localOutput);

				if (callsRemaining-- > 0)
				{
					System.out.println("Getting tweets for " + username);
					System.out.println(callsRemaining);

					ArrayList<Status> tweets = twitter.getUserTimeline(username, 200);
					writer.writeTweets(tweets);
				}
				else
				{
					limits.printGetUserTimelineUsage(twitter.getTwitterObject());
					HDFSUploader.DeleteAndUpload(hdfsPath, localTweetPath);
					return;
				}
			}
		}

		HDFSUploader.DeleteAndUpload(hdfsPath, localTweetPath);
	}
}