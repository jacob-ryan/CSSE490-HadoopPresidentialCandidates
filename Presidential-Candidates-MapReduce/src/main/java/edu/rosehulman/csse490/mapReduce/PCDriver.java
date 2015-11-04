package edu.rosehulman.csse490.mapReduce;

import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.*;
import org.apache.hadoop.mapreduce.lib.output.*;
import org.apache.hadoop.util.*;

public class PCDriver extends Configured implements Tool
{
	private String candidateName;
	
	public PCDriver(String candidateName)
	{
		super();
		this.candidateName = candidateName;
	}
	
	public int run(String[] args) throws Exception
	{
		Job job = Job.getInstance(getConf(), "Presidential Candidates MapReduce - " + candidateName);

		job.setJarByClass(PCDriver.class);

		FileInputFormat.addInputPath(job, new Path("/tmp/TweetData/Tweets/" + candidateName + ".txt"));
		FileOutputFormat.setOutputPath(job, new Path("/tmp/MapReduce Output/" + candidateName + "/"));

		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(NullWritable.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);

		job.setMapperClass(PCMapper.class);
		job.setReducerClass(PCReducer.class);

		return job.waitForCompletion(true) ? 0 : 1;
	}
}