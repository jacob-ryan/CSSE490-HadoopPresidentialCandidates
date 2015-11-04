package edu.rosehulman.csse490.mapReduce;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class RetweetMapper extends Mapper<LongWritable, Text, Text, IntWritable>{
	
	
	@Override
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		String[] tokens = value.toString().split("\t");
		Text name = new Text(tokens[0]);
		int retweetCount = Integer.parseInt(tokens[2]);
		
		context.write(name, new IntWritable(retweetCount));
	}
}
