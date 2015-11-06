package edu.rosehulman.csse490.storm;

import java.util.*;

import twitter4j.*;
import backtype.storm.task.*;
import backtype.storm.topology.*;
import backtype.storm.tuple.*;
import edu.rosehulman.csse490.dataImport.*;

public class KeywordFinderBolt implements IBasicBolt
{
	private ArrayList<String> candidates = new ArrayList<String>();

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer)
	{
		declarer.declare(new Fields("keyword", "word", "text", "id"));
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
		String text = tweet.getText().replaceAll("\\p{Punct}", "").toLowerCase();
		long id = tweet.getId();
		String[] words = text.split(" ");
		String keyword = "NONAME";
		boolean flag = false;

		for (String word : words)
		{
			for (int j = 0; j < this.candidates.size(); j++)
			{
				if (word.toLowerCase().equals(this.candidates.get(j).toLowerCase()))
				{
					keyword = word;
					flag = true;
					break;
				}
			}
			if (flag)
			{
				break;
			}
		}

		for (String word : words)
		{
			collector.emit(new Values(keyword, word, text, id));
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void prepare(Map arg0, TopologyContext arg1)
	{
		Politicians politicians = new Politicians();
		this.candidates = politicians.GetAllHandles();
	}
}