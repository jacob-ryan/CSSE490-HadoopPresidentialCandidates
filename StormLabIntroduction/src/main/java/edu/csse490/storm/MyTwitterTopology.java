package edu.csse490.storm;

import storm.starter.spout.TwitterSampleSpout;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.utils.Utils;

public class MyTwitterTopology {
	public static void main(String[] args) throws AlreadyAliveException, InvalidTopologyException{
		String[] keyWords = {"Tokyo", "New York"};
		
		ConfigReader tconfig = new ConfigReader();

		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("twitter",
				new TwitterSampleSpout(tconfig.getConsumerKey(), tconfig.getConsumerSecret(),
						tconfig.getAccessToken(), tconfig.getAccessTokenSecret(), keyWords));
		
		builder.setBolt("split", new WordSplitBolt())
			.shuffleGrouping("twitter");
		builder.setBolt("count", new WordCountBolt())
			.shuffleGrouping("split");
		builder.setBolt("writer", new WritingBolt())
			.shuffleGrouping("count");
		
		Config config = new Config();
		config.setDebug(false);
		config.setNumWorkers(1);
		
		LocalCluster cluster = new LocalCluster();
		System.out.println("starting topology...");
		cluster.submitTopology("twitter", config, builder.createTopology());
		Utils.sleep(100000);
		System.out.println("killing topology...");
		cluster.killTopology("twitter");
		
//		StormSubmitter.submitTopology("twitter", config, builder.createTopology());
//		System.out.println("Twitter topology should be running...");
	}
}
