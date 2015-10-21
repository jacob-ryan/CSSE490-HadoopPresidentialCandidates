package edu.rosehulman.hadoop_presidential_candidates.storm;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.IBasicBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;

public class WordCountBolt implements IBasicBolt {
	private HashMap<String, Integer> countMap = new HashMap<String, Integer>();
	private File file = new File("/root/foobar.txt");
	
	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("word", "count"));
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		return null;
	}

	@Override
	public void cleanup() {
	}

	@Override
	public void execute(Tuple input, BasicOutputCollector collector) {
		String word = input.getString(0);
		
		Integer count = countMap.get(word);
		if (count == null) count = 0;
		count++;
		
		countMap.put(word, count);
		System.out.println(word + " appeared: " + count);
		//collector.emit(word, count);
		try {
			FileWriter writer = new FileWriter(this.file, true);
			writer.write(word + " appeared " + count);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void prepare(Map arg0, TopologyContext arg1) {
	}

}
