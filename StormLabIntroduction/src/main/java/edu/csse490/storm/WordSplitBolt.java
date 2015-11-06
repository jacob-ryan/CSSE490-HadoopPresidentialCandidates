package edu.csse490.storm;

import java.util.ArrayList;
import java.util.Map;

import twitter4j.Status;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.IBasicBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class WordSplitBolt implements IBasicBolt {
	
	ArrayList<String> keyWords = new ArrayList<String>();
	

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// Setting the output field names we plan to emit
		declarer.declare(new Fields("word", "keyword"));
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		return null;
	}

	@Override
	public void cleanup() {
		// do nothing here
	}

	@Override
	public void execute(Tuple input, BasicOutputCollector collector) {
		Status tweet = (Status) input.getValueByField("tweet");
		String text = tweet.getText();
		String keyword = "UNMATCHED_DATA";
		String[] words = text.split(" ");

		for (int i = 0; i < keyWords.size(); i++) {
			if (text.contains(keyWords.get(i))) {
				keyword = keyWords.get(i);
			}
		}
		
		// for each word, let's emit it to the next bolt
		for (String word : words) {
			collector.emit(new Values(word, keyword));
		}
	}

	@Override
	public void prepare(Map arg0, TopologyContext arg1) {
		keyWords.add("Tokyo");
		keyWords.add("New York");
	}
}
