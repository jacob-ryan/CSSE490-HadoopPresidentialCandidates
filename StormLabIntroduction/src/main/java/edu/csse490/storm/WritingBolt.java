package edu.csse490.storm;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import twitter4j.Status;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.IBasicBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Tuple;

public class WritingBolt implements IBasicBolt {

	public void declareOutputFields(OutputFieldsDeclarer arg0) {
		// TODO Auto-generated method stub
		
	}

	public Map<String, Object> getComponentConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

	public void cleanup() {
		// TODO Auto-generated method stub
		
	}

	public void execute(Tuple input, BasicOutputCollector collector) {
		String word = (String) input.getValueByField("word");
		int count = (Integer) input.getValueByField("count");
		String keyword = (String) input.getValueByField("keyword");
		
		try
		{
			FileWriter writer = new FileWriter(
					new File("tmp/StormLab/" + keyword + ".txt"), true);
			
			writer.write(word + ":" + count + "\n");
			writer.close();
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	public void prepare(Map arg0, TopologyContext arg1) {
		// TODO Auto-generated method stub
	}

}
