package edu.rosehulman.csse490.storm;

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
		// String[] keyWords = GetPoliticians();
		ConfigReader config = new ConfigReader();
		String[] keyWords = new String[1];
		keyWords[0] = "Donald Trump";

		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("twitter",
				new TwitterSampleSpout(config.getConsumerKey(), config.getConsumerSecret(), config.getAccessToken(), config.getAccessTokenSecret(),
						keyWords));

		builder.setBolt("split", new WordSplitBolt()).shuffleGrouping("twitter");
		builder.setBolt("count", new WordCountBolt()).fieldsGrouping("split", new Fields("word"));

		Config configS = new Config();
		configS.setDebug(false);
		configS.setNumWorkers(1);
		//configS.setNumAckers(1);
		configS.setMaxSpoutPending(200);

		StormSubmitter.submitTopology("twitter", configS, builder.createTopology());
		// LocalCluster cluster = new LocalCluster();
		// cluster.submitTopology("twitter", configS, builder.createTopology());
		System.out.println("Twitter topology should be running...");
	}
}