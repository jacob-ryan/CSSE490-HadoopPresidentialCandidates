package edu.rosehulman.csse490.storm;

import java.util.ArrayList;

import storm.starter.spout.TwitterSampleSpout;
import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;

public class TwitterTopology
{
	public static void main(String[] args) throws AlreadyAliveException, InvalidTopologyException
	{
		Politicians politicians = new Politicians();
		ArrayList<String> candidates = politicians.GetAllPoliticians();
		
		String[] keyWords = new String[candidates.size()];
		
		for (int i = 0; i < candidates.size(); i++) {
			keyWords[i] = candidates.get(i);
		}
		
		ConfigReader config = new ConfigReader();

		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("twitter",
				new TwitterSampleSpout(config.getConsumerKey(), config.getConsumerSecret(), config.getAccessToken(), config.getAccessTokenSecret(),
						keyWords));

		builder.setBolt("split", new WordSplitBolt()).shuffleGrouping("twitter");
		builder.setBolt("count", new WordCountBolt()).fieldsGrouping("split", new Fields("keyword"));

		Config configS = new Config();
		configS.setDebug(false);
		configS.setNumWorkers(1);
		//configS.setNumAckers(1);
		configS.setMaxSpoutPending(200);

		StormSubmitter.submitTopology("twitter", configS, builder.createTopology());
		System.out.println("Twitter topology should be running...");
	}
}