package edu.rosehulman.csse490.mapReduce.nGram;

import java.io.*;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;

public class NGramReducer extends Reducer<Text, NullWritable, Text, IntWritable>
{
	@SuppressWarnings("unused")
	@Override
	public void reduce(Text key, Iterable<NullWritable> values, Context context) throws IOException, InterruptedException
	{
		int count = 0;
		for (NullWritable value : values)
		{
			count += 1;
		}
		context.write(key, new IntWritable(count));
	}
}