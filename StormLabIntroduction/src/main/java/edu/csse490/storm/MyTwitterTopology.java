package edu.csse490.storm;

import storm.starter.spout.TwitterSampleSpout;
import backtype.storm.topology.TopologyBuilder;

public class MyTwitterTopology {
	public static void main(String[] args){
		String[] keyWords = {"Tokyo", "New York"};
		
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("twitter",
				new TwitterSampleSpout("CONSUMER KEY", "CONSUMER SECRET",
						"ACCESS KEY", "ACCESS TOKEN", keyWords));
		
		
	}
}
