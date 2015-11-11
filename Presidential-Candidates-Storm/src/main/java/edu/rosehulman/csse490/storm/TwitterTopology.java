package edu.rosehulman.csse490.storm;

import java.util.*;

import storm.starter.spout.TwitterSampleSpout;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.*;
import backtype.storm.utils.Utils;

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
		
		builder.setBolt("split", new WordSplitBolt(), 2).shuffleGrouping("twitter");
		builder.setBolt("count", new WordCountBolt(), 2).fieldsGrouping("split", new Fields("keyword"));
		builder.setBolt("writeCount", new WriteCountBolt(), 2).fieldsGrouping("count", new Fields("keyword"));
		
		builder.setBolt("keywordFinder", new KeywordFinderBolt(), 2).shuffleGrouping("twitter");
		builder.setBolt("writeTweet", new WriteTweetBolt(), 1).fieldsGrouping("keywordFinder", new Fields("keyword"));

		Config configS = new Config();
		configS.setDebug(false);
		configS.setNumWorkers(1);
		// configS.setNumAckers(1);
		configS.setMaxSpoutPending(200);

//		LocalCluster cluster = new LocalCluster();
//		System.out.println("starting topology...");
//		cluster.submitTopology("twitter", configS, builder.createTopology());
//		Utils.sleep(100000);
//		System.out.println("killing topology...");
//		cluster.killTopology("twitter");
		
		StormSubmitter.submitTopology("twitter", configS, builder.createTopology());
		System.out.println("Twitter topology should be running...");
	}
}