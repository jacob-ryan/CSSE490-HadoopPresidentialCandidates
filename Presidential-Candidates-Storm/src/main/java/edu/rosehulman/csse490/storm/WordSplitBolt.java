package edu.rosehulman.csse490.storm;

import java.util.*;

import twitter4j.Status;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.IBasicBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class WordSplitBolt implements IBasicBolt
{
	private ArrayList<String> candidates = new ArrayList<String>();

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer)
	{
		declarer.declare(new Fields("keyword", "word"));
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
		Status tweet = (Status) input.getValueByField("tweet");
		//String text = tweet.getText().replaceAll("\\p{Punct}", "").toLowerCase();
		//String[] words = text.split(" ");
		String word = "test";
		String keyword = "NONAME";
		boolean flag = false;

//		for (String word : words)
//		{
//			for (int j = 0; j < this.candidates.size(); j++)
//			{
//				if (word.toLowerCase().equals(this.candidates.get(j).toLowerCase()))
//				{
//					keyword = word;
//					flag = true;
//					break;
//				}
//			}
//			if (flag)
//			{
//				break;
//			}
//		}
//
//		for (String word : words)
//		{
			collector.emit(new Values(keyword, word));
//		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void prepare(Map arg0, TopologyContext arg1)
	{
		Politicians politicians = new Politicians();
		this.candidates = politicians.GetAllHandles();
	}
}