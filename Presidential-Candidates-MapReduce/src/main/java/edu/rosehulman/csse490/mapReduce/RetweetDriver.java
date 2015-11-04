package edu.rosehulman.csse490.mapReduce;

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
import org.apache.hadoop.util.ToolRunner;

public class RetweetDriver extends Configured implements Tool{

	public static void main(String[] args) throws Exception {
		int exitCode = ToolRunner.run(new RetweetDriver(), args);
		System.exit(exitCode);
	}
	public int run(String[] args) throws Exception {
		if (args.length != 2) {
			System.err.println("Usage: inputDirectory outputDirectory");
			System.exit(-1);
		}
		
		Configuration conf = getConf();
		
		Job job = Job.getInstance(conf, "Retweet Count Average");
		job.setJarByClass(RetweetDriver.class);
		
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		
		job.setMapperClass(RetweetMapper.class);
		job.setReducerClass(RetweetReducer.class);
		
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(IntWritable.class);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(DoubleWritable.class);
		
		job.waitForCompletion(true);
		return 0;
	}

}
