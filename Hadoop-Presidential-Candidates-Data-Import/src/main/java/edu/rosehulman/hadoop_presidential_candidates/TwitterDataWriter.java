package edu.rosehulman.hadoop_presidential_candidates;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import twitter4j.Status;
import twitter4j.User;

public class TwitterDataWriter {
	private String outputPath;
	private final String GENERIC_DELIMETER = "!;;";

	public TwitterDataWriter(String outputPath) {
		this.outputPath = outputPath;
	}

	public TwitterDataWriter() {
		outputPath = "/tmp/output/TwitterData/" + System.currentTimeMillis();
	}
	
	/**
	 * Set the output path of the file to write.
	 * 
	 * @param path
	 */
	public void setPath(String path) {
		outputPath = path;
	}
	
	private String FormatTweet(Status status) {
		StringBuilder tweet = new StringBuilder();
		String tweetText = status.getText().replace("\n", " ");
		tweet.append(status.getId() + GENERIC_DELIMETER + tweetText + "\n");
		
		return tweet.toString();
	}

	// region Write Tweets
	/**
	 * Writes a list of tweets to given output path. This does not write
	 * duplicate tweets based on their ID of the given list.
	 * 
	 * @param tweets
	 */
	public void writeTweets(List<Status> tweets) {
		Set<Long> idSet = new HashSet<Long>();

		for (Status stat : tweets) {
			if (!idSet.contains(stat.getId())) {
				idSet.add(stat.getId());
				writeTweet(stat);
			}
		}
	}

	/**
	 * Writes a tweet to the given output path
	 * 
	 * @param tweet
	 */
	public void writeTweet(Status tweet) {
		try (Writer writer = new BufferedWriter(new FileWriter(outputPath, true))) {

			writer.write(FormatTweet(tweet));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	// endregion

	// region Write Users
	/**
	 * Writes the user's id, screenname, name, and account created date.
	 * 
	 * @param user
	 * @throws IOException
	 */
	public void writeUser(User user){
		try (Writer writer = new BufferedWriter(new FileWriter(outputPath, true))) {

			writer.write(user.getId() + GENERIC_DELIMETER + user.getScreenName() + GENERIC_DELIMETER + user.getName()
					+ GENERIC_DELIMETER + user.getCreatedAt().toString() + "\n");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Writes the list of user's id, screenname, name, and account created date.
	 * 
	 * @param users
	 * @throws IOException
	 */
	public void writeUsers(ArrayList<User> users){
		for (User user : users) {
			writeUser(user);
		}
	}
	// endregion
}
