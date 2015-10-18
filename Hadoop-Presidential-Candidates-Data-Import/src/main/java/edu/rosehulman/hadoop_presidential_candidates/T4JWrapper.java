package edu.rosehulman.hadoop_presidential_candidates;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import twitter4j.GeoLocation;
import twitter4j.Location;
import twitter4j.PagableResponseList;
import twitter4j.Paging;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.RateLimitStatus;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.api.TrendsResources;
import twitter4j.conf.ConfigurationBuilder;

public class T4JWrapper {
	private Twitter twitter;
	
	public T4JWrapper() {
		Initialize();
	}
	
	private void Initialize() {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		ConfigReader config = new ConfigReader();
		
		cb.setDebugEnabled(true).setOAuthConsumerKey(config.getConsumerKey())
		.setOAuthConsumerSecret(config.getConsumerSecret())
		.setOAuthAccessToken(config.getAccessToken())
		.setOAuthAccessTokenSecret(config.getAccessTokenSecret());
		
		TwitterFactory tf = new TwitterFactory(cb.build());
		twitter = tf.getInstance();
	}
	
	/**
	 * Get a list of following users for the given user ID
	 * MAX 200 followers per call
	 * 
	 * @param userID
	 * @param amountOfFollowers
	 * @return
	 * @throws TwitterException
	 */
	public ArrayList<User> getFollowersForUser(Long userID, Long amountOfFollowers) throws TwitterException {
		ArrayList<User> followers = new ArrayList<User>();
		
		long remainingFollowers = amountOfFollowers;
		PagableResponseList<User> followersList;
		long cursor = -1;
		
		do {
			if (remainingFollowers < 200) {
				followersList = twitter.getFollowersList(userID, cursor, (int) remainingFollowers);
				remainingFollowers = 0;
			} else {
				followersList = twitter.getFollowersList(userID, cursor, 200);
				remainingFollowers -= 200;
			}
			
			for (User user : followersList) {
				followers.add(user);
			}
			
		} while ((cursor = followersList.getNextCursor()) != 0 && remainingFollowers > 0);
		
		return followers;
	}
	
	/**
	 * Get a list of the friend users for the given user ID
	 * MAX 200 friends per call
	 * 
	 * @param userID
	 * @param amountOfFriends
	 * @return
	 * @throws TwitterException
	 */
	public ArrayList<User> getFriendsForUser(Long userID, Long amountOfFriends) throws TwitterException {
		ArrayList<User> friends = new ArrayList<User>();
		
		long remainingFriends = amountOfFriends;
		PagableResponseList<User> friendsList;
		long cursor = -1;
		
		do {
			if (remainingFriends > 200) {
				friendsList = twitter.getFriendsList(userID, cursor, (int) remainingFriends);
				remainingFriends = 0;
			} else {
				friendsList = twitter.getFriendsList(userID, cursor, (int) remainingFriends);
				remainingFriends -= 200;
			}
			
			for (User user : friendsList) {
				System.out.println(user.getLocation());
				friends.add(user);
			}
			
		} while ((cursor = friendsList.getNextCursor()) != 0 && remainingFriends> 0);
		
		return friends;
	}
	
	/**
	 * Gets relevant tweets given for the search term
	 * MAX 100 tweets per call
	 * 
	 * @param searchTerm
	 * @param amountOfTweets
	 * @return
	 */
	public ArrayList<Status> searchAndReturnTweets(String searchTerm, int amountOfTweets) {
		ArrayList<Status> tweets = new ArrayList<Status>(); 
		long lastID = Long.MAX_VALUE;
		Query query = new Query(searchTerm);
		
		while (tweets.size() < amountOfTweets) {
			if (amountOfTweets - tweets.size() > 100) {
				query.setCount(100);
			} else {
				query.setCount(amountOfTweets - tweets.size());
			}
			
			try {
				QueryResult result = twitter.search(query);
				tweets.addAll(result.getTweets());
				for (Status t : tweets) {
					if (t.getId() < lastID)
						lastID = t.getId();
				}
			}
			catch (TwitterException te) {
				System.err.println(te.getErrorMessage());
			}
			query.setMaxId(lastID-1);
		}
		
		return tweets;
	}
	
	/**
	 * Gets the most recent tweets for the given screenname
	 * MAX 200 tweets per call
	 * 
	 * @param userScreenName
	 * @param amountOfTweets
	 * @return
	 * @throws TwitterException
	 */
	public ArrayList<Status> getUserTimeline(String userScreenName, int amountOfTweets) throws TwitterException {
		ArrayList<Status> totalTweets = new ArrayList<Status>();
		int remainingTweets = amountOfTweets;
		int page = 1;
		Paging pages;
		
		do {
			pages = new Paging(page++, 200);
			List<Status> tweets = twitter.getUserTimeline(userScreenName, pages);
			
			for (Status tweet : tweets) {
				totalTweets.add(tweet);
				
				if (--remainingTweets == 0) {
					break;
				}
			}
		} while (remainingTweets > 0);
		
		return totalTweets;
	}
	
	/**
	 * Gets the most recent tweets for the given user
	 * MAX 200 tweets per call
	 * 
	 * @param userID
	 * @param amountOfTweets
	 * @return
	 * @throws TwitterException
	 */
	public ArrayList<Status> getUserTimeline(Long userID, int amountOfTweets) throws TwitterException {
		ArrayList<Status> totalTweets = new ArrayList<Status>();
		int remainingTweets = amountOfTweets;
		int page = 1;
		Paging pages;
		
		do {
			pages = new Paging(page++, 200);
			List<Status> tweets = twitter.getUserTimeline(userID, pages);
			
			for (Status tweet : tweets) {
				totalTweets.add(tweet);
				
				if (--remainingTweets == 0) {
					break;
				}
			}
		} while (remainingTweets > 0);
		
		return totalTweets;
	}
	
	public int getTrendingByLocation(double latitude, double longitude) throws TwitterException {
		GeoLocation geo = new GeoLocation(latitude, longitude);
		TrendsResources trends = twitter.trends();
		ResponseList<Location> closeTrends = trends.getClosestTrends(geo);
		
		for (Location trend : closeTrends) {
			System.out.println(trend.getName());
		}
		return 1;
	}
	
	/**
	 * Returns the twitter object.
	 * Used to get authorized twitter user's ID.
	 * 
	 * @return
	 */
	public Twitter getTwitterObject(){
		return twitter;
	}
	
	
	/**
	 * Deprecated shouldn't use this right now.
	 * Quick and dirty way to print tweets if necessary.
	 * @param tweets
	 */
	@Deprecated
	public void printTweets(ArrayList<Status> tweets) {
		System.out.println("-------------");
		for (int i = 0; i < tweets.size(); i++) {
			System.out.println("@" + tweets.get(i).getUser().getScreenName() + ": " + tweets.get(i).getText());
		}
		System.out.println("-------------");
	}
}
