package edu.rosehulman.csse490.mapReduce.nGram;

import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.*;
import org.apache.hadoop.mapreduce.lib.output.*;
import org.apache.hadoop.util.*;

public class NGramDriver extends Configured implements Tool
{
	private String candidateName;
	
	public NGramDriver(String candidateName)
	{
		super();
		this.candidateName = candidateName;
	}
	
	public int run(String[] args) throws Exception
	{
		Job job = Job.getInstance(getConf(), "P.C. N-Gram MapReduce - " + candidateName);

		job.setJarByClass(NGramDriver.class);

		FileInputFormat.addInputPath(job, new Path("/tmp/TweetData/" + candidateName + "/"));
		FileOutputFormat.setOutputPath(job, new Path("/tmp/Output/N-gram/" + candidateName + "/"));

		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(NullWritable.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);

		job.setMapperClass(NGramMapper.class);
		job.setReducerClass(NGramReducer.class);

		return job.waitForCompletion(true) ? 0 : 1;
	}
}