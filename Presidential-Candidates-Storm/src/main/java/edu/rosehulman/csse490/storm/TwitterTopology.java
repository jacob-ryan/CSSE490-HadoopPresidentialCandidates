package edu.rosehulman.csse490.storm;

import java.util.*;

import storm.starter.spout.*;
import backtype.storm.*;
import backtype.storm.generated.*;
import backtype.storm.topology.*;
import backtype.storm.tuple.*;
import backtype.storm.utils.*;
import edu.rosehulman.csse490.dataImport.*;

public class TwitterTopology
{
	public static void main(String[] args) throws AlreadyAliveException, InvalidTopologyException
	{
		Politicians politicians = new Politicians();
		ArrayList<String> candidates = politicians.GetAllHandles();

		String[] keyWords = new String[candidates.size()];

		for (int i = 0; i < candidates.size(); i++)
		{
			keyWords[i] = candidates.get(i);
		}

		ConfigReader config = new ConfigReader();

		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("twitter",
				new TwitterSampleSpout(config.getConsumerKey(), config.getConsumerSecret(), config.getAccessToken(), config.getAccessTokenSecret(),
						keyWords));

		builder.setBolt("keywordFinder", new KeywordFinderBolt()).shuffleGrouping("twitter");
		builder.setBolt("count", new WordCountBolt()).fieldsGrouping("keywordFinder", new Fields("keyword"));
		builder.setBolt("writeCount", new WriteCountBolt()).fieldsGrouping("count", new Fields("keyword"));
		builder.setBolt("writeTweet", new WriteTweetBolt()).fieldsGrouping("keywordFinder", new Fields("keyword"));

		Config configS = new Config();
		configS.setDebug(false);
		configS.setNumWorkers(1);
		// configS.setNumAckers(1);
		configS.setMaxSpoutPending(200);

		LocalCluster cluster = new LocalCluster();
		System.out.println("starting topology...");
		cluster.submitTopology("twitter", configS, builder.createTopology());
		Utils.sleep(100000);
		System.out.println("killing topology...");
		cluster.killTopology("twitter");

		// StormSubmitter.submitTopology("twitter", configS, builder.createTopology());
		// System.out.println("Twitter topology should be running...");
	}
}