package edu.rosehulman.csse490.dataImport;

import java.io.*;
import java.util.*;

import twitter4j.*;

public class TwitterWriter
{
	private static final String DELIMETER = "\t";
	private String outputPath;
	private BufferedWriter writer;

	public TwitterWriter(String outputPath) throws IOException
	{
		this.outputPath = outputPath;
		this.writer = new BufferedWriter(new FileWriter(this.outputPath, true));
	}

	/**
	 * Writes a list of tweets to given output path. This does not write duplicate tweets based on their ID of the given list.
	 *
	 * @param tweets
	 * @throws IOException
	 */
	public void writeTweets(List<Status> tweets) throws IOException
	{
		Set<Long> idSet = new HashSet<Long>();

		for (Status tweet : tweets)
		{
			if (!idSet.contains(tweet.getId()))
			{
				idSet.add(tweet.getId());
				this.writer.write(formatTweet(tweet));
			}
		}
	}

	public void writeRetweets(List<Status> tweets, String username) throws IOException
	{
		Set<Long> idSet = new HashSet<Long>();

		for (Status tweet : tweets)
		{
			if (!idSet.contains(tweet.getId()))
			{
				idSet.add(tweet.getId());
				this.writer.write(formatRetweet(tweet, username));
			}
		}
	}

	/**
	 * Writes the list of user's id, screenname, name, and account created date.
	 *
	 * @param users
	 * @throws IOException
	 */
	public void writeUsers(ArrayList<User> users) throws IOException
	{
		for (User user : users)
		{
			this.writer.write(formatUser(user));
		}
	}

	private String formatTweet(Status status)
	{
		return status.getText().replace("\n", " ") + "\n";
	}

	private String formatRetweet(Status status, String username)
	{
		String tweetText = status.getText().replace("\n", " ");
		int recount = status.getRetweetCount();

		return username + TwitterWriter.DELIMETER + tweetText + TwitterWriter.DELIMETER + recount + "\n";
	}

	private String formatUser(User user)
	{
		return user.getId() + TwitterWriter.DELIMETER + user.getScreenName() + TwitterWriter.DELIMETER + user.getName() + TwitterWriter.DELIMETER
				+ user.getCreatedAt().toString() + "\n";
	}
}