package edu.rosehulman.csse490.storm;

import java.io.*;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.task.*;
import backtype.storm.topology.*;
import backtype.storm.tuple.*;

public class WriteTweetBolt implements IBasicBolt
{
	private int tweetBuffer = 0;
	private String hdfsUri = "hdfs://hadoop-02.csse.rose-hulman.edu:8020";
	private ArrayList<String> tweets = new ArrayList<String>();
	private FileSystem fs;

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

		this.tweets.add(text);

		if (++this.tweetBuffer > 100)
		{
			System.out.println("[WriteTweetBolt] writing to " + keyword + "...");
			this.tweetBuffer = 0;
			
			try
			{
				String dateString = new SimpleDateFormat("yyyy-MM-dd_HH-mm").format(new Date());
				Path path = new Path("/tmp/TweetData/" + keyword + "/storm-" + dateString + ".txt");
				FSDataOutputStream out = fs.create(path);
				
				String data = "";
				for (int i = 0; i < this.tweets.size(); i++)
				{
					data = this.tweets.get(i) + "\n";
					out.write(data.getBytes());
				}

				out.close();
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
		try {
			Configuration conf = new Configuration();
			this.fs = FileSystem.get(URI.create(this.hdfsUri), conf);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
