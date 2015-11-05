package edu.rosehulman.csse490.storm;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.IBasicBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Tuple;

public class WriteCountBolt implements IBasicBolt {

	private int wordBuffer = 0;
	private HashMap<String, Integer> countMap = new HashMap<String, Integer>();
	private String filePath = "tmp/Tweets/";

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
		String keyword = (String) input.getValueByField("keyword");
		String word = (String) input.getValueByField("word");
		int count = (Integer) input.getValueByField("count");
		
		countMap.put(word, count);
		
		if (++wordBuffer > 100) {
			wordBuffer = 0;
			
			System.out.println("Writing tweet count map...");
			try
			{
				FileWriter writer = new FileWriter(new File(this.filePath + keyword + ".txt"), true);
				
				for (String key : countMap.keySet()) {
					writer.write(key + ": " + countMap.get(key) + "\n");
				}
				writer.close();
			}
			catch (IOException e)
			{
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public void prepare(Map arg0, TopologyContext arg1) {
		//wordBuffer = 0;
	}

}
