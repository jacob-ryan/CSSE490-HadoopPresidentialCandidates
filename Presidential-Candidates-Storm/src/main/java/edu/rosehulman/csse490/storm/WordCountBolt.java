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
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;

public class WordCountBolt implements IBasicBolt
{
	private HashMap<String, Integer> countMap = new HashMap<String, Integer>();
	private File file = new File("/tmp/foobar.txt");

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer)
	{
		declarer.declare(new Fields("word", "count"));
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

		Integer count = countMap.get(word);
		if (count == null)
			count = 0;
		count++;

		countMap.put(word, count);

		try
		{
			FileWriter writer = new FileWriter(this.file, true);
			writer.write(word + ": " + countMap.get(word) + "\n");
			writer.close();
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void prepare(Map arg0, TopologyContext arg1)
	{
	}
}