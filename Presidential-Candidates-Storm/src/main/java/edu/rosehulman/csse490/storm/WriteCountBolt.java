package edu.rosehulman.csse490.storm;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

import backtype.storm.task.*;
import backtype.storm.topology.*;
import backtype.storm.tuple.*;

public class WriteCountBolt implements IBasicBolt
{
	private static final String filePath = "/tmp/Tweets/";

	private int wordBuffer = 0;
	private Map<String, Integer> countMap = new HashMap<String, Integer>();

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer)
	{
		// No output fields, because nothing is emitted.
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
		String keyword = (String) input.getValueByField("keyword");
		String word = (String) input.getValueByField("word");
		int count = (Integer) input.getValueByField("count");

		this.countMap.put(word, count);
		this.wordBuffer += 1;

		if (this.wordBuffer >= 100)
		{
			this.wordBuffer = 0;

			System.out.println("[WriteCountBolt] Writing tweet count map...");
			try
			{
				FileWriter writer = new FileWriter(new File(WriteCountBolt.filePath + keyword + ".txt"));

				List<Map.Entry<String, Integer>> sortedWords = new ArrayList<Map.Entry<String, Integer>>(this.countMap.entrySet());
				Collections.sort(sortedWords, new Comparator<Map.Entry<String, Integer>>()
						{
					@Override
					public int compare(Entry<String, Integer> a, Entry<String, Integer> b)
					{
						return a.getValue().compareTo(b.getValue());
					}
						});
				for (Map.Entry<String, Integer> entry : sortedWords)
				{
					writer.write(entry.getKey() + "\t" + entry.getValue() + "\n");
				}
				writer.close();
			}
			catch (IOException e)
			{
				throw new RuntimeException(e);
			}
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void prepare(Map map, TopologyContext context)
	{
	}
}