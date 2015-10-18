package edu.rosehulman.hadoop_presidential_candidates;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
import twitter4j.api.FavoritesResources;
import twitter4j.api.TweetsResources;
import twitter4j.conf.*;

public class TwitterFeedRetriever {

	public static void main(String[] args) throws TwitterException, IOException, InterruptedException {
		GetUserTimelineTweets();
		
		// Gets recent tweets from my timeline

		// List<Status> statuses = twitter.getHomeTimeline();
		// System.out.println("Showing home timeline.");
		// for (Status status : statuses) {
		// System.out.println(status.getUser().getName() + ":" +
		// status.getText());
		// }

		// Gets recent tweets from my timeline with paging objects

		// Paging page = new Paging(1, 3);
		// Paging page2 = new Paging(2, 3);
		// Paging pageTotal = new Paging(10, 6);
		//
		// List<Status> firstRetrieve = twitter.getHomeTimeline(page);
		// List<Status> secondRetrieve = twitter.getHomeTimeline(page2);
		// List<Status> thirdRetrieve = twitter.getHomeTimeline(pageTotal);
		//
		// System.out.println("first");
		// for (Status stat : firstRetrieve) {
		// System.out.println(stat.getUser().getName() + ":" + stat.getText());
		// }
		//
		// System.out.println("second");
		// for (Status stat : secondRetrieve) {
		// System.out.println(stat.getUser().getName() + ":" + stat.getText());
		// }
		//
		// System.out.println("third");
		// for (Status stat : thirdRetrieve) {
		// System.out.println(stat.getUser().getName() + ":" + stat.getText());
		// }

		// get the id of the person i favorited most recently
		// FavoritesResources favorites = twitter.favorites();
		// List<Status> favs = favorites.getFavorites(new Paging(1, 1));
		// long friendID = favs.get(0).getUser().getId();

		// print out that persons last 10 tweets
		// List<Status> friendTweets = twitter.getUserTimeline(friendID, new
		// Paging(1, 10));
		// for (Status tweet : friendTweets) {
		// System.out.println(tweet.getUser().getName() + ": " +
		// tweet.getText());
		// }

		// sending a search query and attempting to page the results

		// do {
		// for (Status tweet : result.getTweets()) {
		// System.out.println(tweet.getUser().getScreenName() + " - " +
		// tweet.getText());
		// }
		// query = result.nextQuery();
		// if (query != null)
		// result = twitter.search(query);
		// } while (query != null);

		// get my following list

//		ArrayList<User> followingList = new ArrayList<User>();
//		ArrayList<User> followersList = new ArrayList<User>();
//
//		try {
//			long cursor = -1;
//			PagableResponseList<User> followingPagnition;
//
//			do {
//				followingPagnition = twitter.getFriendsList(twitter.getId(), -1);
//				for (User user : followingPagnition) {
//					followingList.add(user);
//					System.out.println("adding following user");
//				}
//			} while ((cursor = followingPagnition.getNextCursor()) != 0);
//
//			cursor = -1;
//			PagableResponseList<User> followersPagnition;
//
//			do {
//				followersPagnition = twitter.getFollowersList(twitter.getId(), cursor, 30);
//				for (User user : followersPagnition) {
//					followersList.add(user);
//				}
//			} while ((cursor = followersPagnition.getNextCursor()) != 0);
//
//		} catch (TwitterException e) {
//			System.err.println(e.getErrorMessage());
//			System.exit(-1);
//		}
//
//		ArrayList<User> lol = new ArrayList<User>();
//
//		for (User user : followingList) {
//			if (followersList.contains(user)) {
//				System.out.println(user.getName());
//			}
//		}
		
//		ArrayList<User> followingListTotal = new ArrayList<User>();
//		ArrayList<User> followersListTotal = new ArrayList<User>();
//		
//		Map<String, RateLimitStatus> rateLimitStatus = twitter.getRateLimitStatus();
//		RateLimitStatus getFollowersRateLimit = rateLimitStatus.get("/followers/list");
//		System.out.println(getFollowersRateLimit.getRemaining());
//		System.out.println(getFollowersRateLimit.getSecondsUntilReset());
//		System.out.println(getFollowersRateLimit.getLimit());
//		
//		PagableResponseList<User> followersList;
//		long cursor = -1;
//		do {
//			followersList = twitter.getFollowersList(twitter.getId(), cursor, 50);
//			for (User user : followersList) {
//				followersListTotal.add(user);
//			}
//		} while ((cursor = followersList.getNextCursor()) != 0);
//		
//		PagableResponseList<User> followingList;
//		cursor = -1;
//		
//		do {
//			followingList = twitter.getFriendsList(twitter.getId(), cursor, 50);
//			for (User user : followingList) {
//				followingListTotal.add(user);
//			}
//		} while ((cursor = followingList.getNextCursor()) != 0);
//		
//		Map<String, RateLimitStatus> rateLimitStatus2 = twitter.getRateLimitStatus();
//		RateLimitStatus getFollowersRateLimit2 = rateLimitStatus2.get("/friends/list");
//		System.out.println(getFollowersRateLimit2.getRemaining());
//		System.out.println(getFollowersRateLimit2.getSecondsUntilReset());
//		System.out.println(getFollowersRateLimit2.getLimit());
//		
//		for (User user : followingListTotal) {
//			if (!followersListTotal.contains(user)) {
//				System.out.println(user.getScreenName());
//			}
//		}
	}
	
	public static void GetUserTimelineTweets() throws TwitterException, IOException, InterruptedException {
		Politicians candidates = new Politicians();
		final HashMap<String, ArrayList<String>> politicianMap = candidates.GetAllPoliticians();
		String localTweetPath = "Tweets";
		String hdfsPath = "/tmp/output";
		T4JWrapper t4j = new T4JWrapper();
		TwitterDataWriter writer = new TwitterDataWriter();
		TwitterRateLimitViewer limits = new TwitterRateLimitViewer();
		
		// get tweets and write them out
		for (String key : politicianMap.keySet()) {
			for (String username : politicianMap.get(key)) {
				String localOutput = localTweetPath + "/" + username + ".txt";
				writer.setPath(localOutput);
				
				if (limits.GetUserTimeLineRemainingCalls(t4j.getTwitterObject()) > 0) {
					//ArrayList<Status> tweets = t4j.getUserTimeline(username, 200);
					//writer.writeTweets(tweets);
				} else {
					limits.printGetUserTimelineUsage(t4j.getTwitterObject());
					break;
				}
			}
		}

		HDFSUploader.DeleteAndUpload(hdfsPath, localTweetPath);
	}
	
	public static void WorkingWithTrends() throws TwitterException {
		T4JWrapper t4j = new T4JWrapper();
		// Terre Haute - latitude/longitude
		t4j.getTrendingByLocation(39.4696, 87.3898);
		t4j.getTrendingByLocation(40.7127, 74.0059);
	}
}
