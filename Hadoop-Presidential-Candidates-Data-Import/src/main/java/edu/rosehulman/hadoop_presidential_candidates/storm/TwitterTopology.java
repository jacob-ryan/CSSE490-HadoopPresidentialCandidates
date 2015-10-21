package edu.rosehulman.hadoop_presidential_candidates.storm;

import edu.rosehulman.hadoop_presidential_candidates.ConfigReader;
import storm.starter.spout.TwitterSampleSpout;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;

public class TwitterTopology {
	public static void main(String[] args) {
		//String[] keyWords = GetPoliticians();
		ConfigReader config = new ConfigReader();
		String[] keyWords = new String[1];
		keyWords[0] = "Donald Trump";
		
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("twitter", new TwitterSampleSpout(config.getConsumerKey(), config.getConsumerSecret(), config.getAccessToken(), config.getAccessTokenSecret(), keyWords));
		
		builder.setBolt("split", new WordSplitBolt());
			//.shuffleGrouping("twitter");
		builder.setBolt("count", new WordCountBolt());
			//.fieldsGrouping("split", new Fields("word"));
	}
	
	//public static String[] 
}
