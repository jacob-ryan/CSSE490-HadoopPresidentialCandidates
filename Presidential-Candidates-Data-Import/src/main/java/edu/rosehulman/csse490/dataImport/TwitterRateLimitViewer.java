package edu.rosehulman.csse490.dataImport;

import java.util.*;

import twitter4j.*;

public class TwitterRateLimitViewer
{
	private final String SEARCH_TWEETS_ROUTE = "/search/tweets";
	private final String FOLLOWERS_LIST_ROUTE = "/followers/list";
	private final String FRIENDS_LIST_ROUTE = "/friends/list";
	private final String USER_TIMELINE_ROUTE = "/statuses/user_timeline";

	private void printUsage(String route, Twitter twitter) throws TwitterException
	{
		RateLimitStatus status = GetRateLimit(route, twitter);

		System.out.println("API status for: " + route);
		System.out.println(" - Calls allowed:\t" + status.getLimit());
		System.out.println(" - Calls remaining:\t" + status.getRemaining());
		System.out.println(" - Time until reset:\t" + status.getSecondsUntilReset() + " seconds");
		System.out.println("-------------------------------------------------------------------");
	}

	private RateLimitStatus GetRateLimit(String apiRoute, Twitter twitter) throws TwitterException
	{
		Map<String, RateLimitStatus> limitMap = twitter.getRateLimitStatus();
		return limitMap.get(apiRoute);
	}

	public void printSearchCallsUsage(Twitter twitter) throws TwitterException
	{
		printUsage(this.SEARCH_TWEETS_ROUTE, twitter);
	}

	public void printGetFollowersUsage(Twitter twitter) throws TwitterException
	{
		printUsage(this.FOLLOWERS_LIST_ROUTE, twitter);
	}

	public void printGetFriendsUsage(Twitter twitter) throws TwitterException
	{
		printUsage(this.FRIENDS_LIST_ROUTE, twitter);
	}

	public void printGetUserTimelineUsage(Twitter twitter) throws TwitterException
	{
		printUsage(this.USER_TIMELINE_ROUTE, twitter);
	}

	private int GetRemainingCalls(String route, Twitter twitter) throws TwitterException
	{
		RateLimitStatus status = GetRateLimit(route, twitter);
		return status.getRemaining();
	}

	public int GetSearchTweetsRemainingCalls(Twitter twitter) throws TwitterException
	{
		return GetRemainingCalls(this.SEARCH_TWEETS_ROUTE, twitter);
	}

	public int GetFollowersListRemainingCalls(Twitter twitter) throws TwitterException
	{
		return GetRemainingCalls(this.FOLLOWERS_LIST_ROUTE, twitter);
	}

	public int GetFriendsListRemainingCalls(Twitter twitter) throws TwitterException
	{
		return GetRemainingCalls(this.FRIENDS_LIST_ROUTE, twitter);
	}

	public int GetUserTimeLineRemainingCalls(Twitter twitter) throws TwitterException
	{
		return GetRemainingCalls(this.USER_TIMELINE_ROUTE, twitter);
	}
}