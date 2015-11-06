package edu.rosehulman.csse490.storm;

import java.io.*;
import java.util.*;

import backtype.storm.task.*;
import backtype.storm.topology.*;
import backtype.storm.tuple.*;

public class WriteTweetBolt implements IBasicBolt
{
	private int tweetBuffer = 0;
	private String filePath = "tmp/StormTweets/";
	private ArrayList<String> tweets = new ArrayList<String>();
	private HashSet<Long> idSet = new HashSet<Long>();

	@Override
	public void declareOutputFields(OutputFieldsDeclarer arg0)
	{
		// none for now
	}

	@Override
	public Map<String, Object> getComponentConfiguration()
	{
		return null;
	}

	@Override
	public void cleanup()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void execute(Tuple input, BasicOutputCollector collector)
	{
		String text = (String) input.getValueByField("text");
		String keyword = (String) input.getValueByField("keyword");
		long id = (Long) input.getValueByField("id");

		if (this.idSet.contains(id))
		{
			System.out.println("skipping bad id");
			return;
		}
		else
		{
			this.idSet.add(id);
		}
		this.tweets.add(text);

		if (++this.tweetBuffer > 100)
		{
			this.tweetBuffer = 0;

			System.out.println("Writing tweets...");
			try
			{
				FileWriter writer = new FileWriter(new File(this.filePath + keyword + ".txt"), true);

				for (int i = 0; i < this.tweets.size(); i++)
				{
					writer.write(this.tweets.get(i) + "\n");
				}

				writer.close();
				this.tweets.clear();
			}
			catch (IOException e)
			{
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public void prepare(Map arg0, TopologyContext arg1)
	{
	}
}
