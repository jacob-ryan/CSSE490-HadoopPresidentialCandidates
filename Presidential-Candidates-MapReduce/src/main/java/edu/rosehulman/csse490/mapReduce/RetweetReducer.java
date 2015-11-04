package edu.rosehulman.csse490.mapReduce;

import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class RetweetReducer extends Reducer<Text, IntWritable, Text, DoubleWritable>{
	
	@Override
	public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
		int sum = 0;
		int count = 0;
		for (IntWritable value : values) {
			sum = sum + Integer.parseInt(value.toString());
			count++;
		}
		
		double average = sum / count;
		
		context.write(key, new DoubleWritable(average));
	} 

}
