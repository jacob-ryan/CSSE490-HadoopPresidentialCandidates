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
		
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("twitter",
				new TwitterSampleSpout("CONSUMER KEY", "CONSUMER SECRET",
						"ACCESS KEY", "ACCESS TOKEN", keyWords));
		
		builder.setBolt("writer", new WritingBolt())
			.shuffleGrouping("twitter");
		
		Config config = new Config();
		config.setDebug(false);
		config.setNumWorkers(1);
		
		StormSubmitter.submitTopology("twitter", config, builder.createTopology());
		System.out.println("Twitter topology should be running...");
	}
}
