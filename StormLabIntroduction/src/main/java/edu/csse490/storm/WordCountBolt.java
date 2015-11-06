package edu.csse490.storm;

import java.util.HashMap;
import java.util.Map;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.IBasicBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class WordCountBolt implements IBasicBolt {
	private HashMap<String, Integer> countMap = new HashMap<String, Integer>();
	
	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("word", "count", "keyword"));
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void cleanup() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void execute(Tuple input, BasicOutputCollector collector) {
		String word = (String) input.getValueByField("word");
		String keyword = (String) input.getValueByField("keyword");
		
		Integer count = countMap.get(word);
		if (count == null)
			count = 0;
		count++;

		countMap.put(word, count);
		
		collector.emit(new Values(word, count, keyword));
	}

	@Override
	public void prepare(Map arg0, TopologyContext arg1) {
		// TODO Auto-generated method stub
		
	}

}
