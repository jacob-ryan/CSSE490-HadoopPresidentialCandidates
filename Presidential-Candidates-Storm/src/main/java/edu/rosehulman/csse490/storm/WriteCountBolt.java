package edu.rosehulman.csse490.storm;

import java.io.*;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import backtype.storm.task.*;
import backtype.storm.topology.*;
import backtype.storm.tuple.*;

public class WriteCountBolt implements IBasicBolt
{
	private int wordBuffer = 0;
	private Map<String, Integer> countMap = new HashMap<String, Integer>();
	private String hdfsUri = "hdfs://hadoop-02.csse.rose-hulman.edu:8020";
	private FileSystem fs;

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

		if (this.wordBuffer >= 1000)
		{
			this.wordBuffer = 0;

			System.out.println("[WriteCountBolt] Writing tweet count map for " + keyword + "...");
			try
			{
				List<Map.Entry<String, Integer>> sortedWords = new ArrayList<Map.Entry<String, Integer>>(this.countMap.entrySet());
				Collections.sort(sortedWords, new Comparator<Map.Entry<String, Integer>>()
						{
					@Override
					public int compare(Entry<String, Integer> a, Entry<String, Integer> b)
					{
						return a.getValue().compareTo(b.getValue());
					}
						});
				
				Path path = new Path("/tmp/StormWordCount/" + keyword + "-storm.txt");
				
				if (fs.exists(path)) {
					fs.delete(path, false);
				}
				
				FSDataOutputStream out = fs.create(path);
				
				String data = "";
				for (Map.Entry<String, Integer> entry : sortedWords)
				{
					data = entry.getKey() + "\t" + entry.getValue() + "\n";
					out.write(data.getBytes());
				}
				out.close();
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
		try {
			Configuration conf = new Configuration();
			this.fs = FileSystem.get(URI.create(this.hdfsUri), conf);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}