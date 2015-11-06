package edu.rosehulman.csse490.storm;

import java.util.*;

import backtype.storm.task.*;
import backtype.storm.topology.*;
import backtype.storm.tuple.*;

public class WordCountBolt implements IBasicBolt
{
	private HashMap<String, Integer> countMap = new HashMap<String, Integer>();

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer)
	{
		declarer.declare(new Fields("keyword", "word", "count"));
	}

	@Override
	public Map<String, Object> getComponentConfiguration()
	{
		return null;
	}

	@Override
	public void cleanup()
	{
	}

	@Override
	public void execute(Tuple input, BasicOutputCollector collector)
	{
		String word = (String) input.getValueByField("word");
		String keyword = (String) input.getValueByField("keyword");

		Integer count = this.countMap.get(word);
		if (count == null)
		{
			count = 0;
		}
		count++;

		this.countMap.put(word, count);

		collector.emit(new Values(keyword, word, count));
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void prepare(Map arg0, TopologyContext arg1)
	{
	}
}