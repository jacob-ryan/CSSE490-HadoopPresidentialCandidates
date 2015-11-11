package edu.rosehulman.csse490.mapReduce.retweet;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;

public class RetweetDriver extends Configured implements Tool
{
	public int run(String[] args) throws Exception
	{
		if (args.length != 0)
		{
			System.err.println("Usage (with YARN):");
			System.err.println("yarn jar <jar file> edu.rosehulman.csse490.mapReduce.Main");
			System.exit(-1);
		}
		
		Configuration conf = getConf();

		Job job = Job.getInstance(conf, "P.C. Re-tweet Average MapReduce");
		job.setJarByClass(RetweetDriver.class);

		FileInputFormat.addInputPath(job, new Path("/tmp/RetweetData"));
		FileOutputFormat.setOutputPath(job, new Path("/tmp/Output/Retweet/"));

		job.setMapperClass(RetweetMapper.class);
		job.setReducerClass(RetweetReducer.class);

		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(IntWritable.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(DoubleWritable.class);

		return job.waitForCompletion(true) ? 0 : 1;
	}
}