package edu.rosehulman.csse490.storm;

import java.awt.List;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import twitter4j.Status;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.IBasicBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Tuple;

public class WriteTweetBolt implements IBasicBolt {
	private int tweetBuffer = 0;
	private String filePath = "tmp/StormTweets/";
	private ArrayList<String> tweets = new ArrayList<String>();
	private HashSet<Long> idSet = new HashSet<Long>();

	@Override
	public void declareOutputFields(OutputFieldsDeclarer arg0) {
		// none for now
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		return null;
	}

	@Override
	public void cleanup() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void execute(Tuple input, BasicOutputCollector collector) {
		String text = (String) input.getValueByField("text");
		String keyword = (String) input.getValueByField("keyword");
		long id = (Long) input.getValueByField("id");
		
		if (idSet.contains(id)) {
			System.out.println("skipping bad id");
			return;
		} else {
			idSet.add(id);
		}
		tweets.add(text);
		
		if (++tweetBuffer > 100) {
			tweetBuffer = 0;
			
			System.out.println("Writing tweets...");
			try
			{
				FileWriter writer = new FileWriter(new File(this.filePath + keyword + ".txt"), true);
				
				for (int i = 0; i < tweets.size(); i++) {
					writer.write(tweets.get(i) + "\n");
				}
				
				writer.close();
				tweets.clear();
			}
			catch (IOException e)
			{
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public void prepare(Map arg0, TopologyContext arg1) {
	}
}
