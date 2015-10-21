package edu.rosehulman.hadoop_presidential_candidates.storm;

import java.util.Map;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.IBasicBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class WordSplitBolt implements IBasicBolt {

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("word"));
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
		String tweet = input.getString(0);
		System.out.println(tweet);
		
		String[] words = tweet.split(" ");
		for (String word : words) {
			System.out.println("passing: " + word);
			collector.emit(new Values(word));
		}
	}

	@Override
	public void prepare(Map arg0, TopologyContext arg1) {
	}

}
