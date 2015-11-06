package edu.rosehulman.csse490.mapReduce.nGram;

import java.io.*;
import java.util.regex.*;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;

public class NGramMapper extends Mapper<LongWritable, Text, Text, NullWritable>
{
	@Override
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException
	{
		String input = value.toString().toLowerCase().trim();
		String text = "";
		for (int i = 0; i < input.length(); i += 1)
		{
			char c = input.charAt(i);
			if (c == ' ' || Character.isLetterOrDigit(c))
			{
				text += c;
			}
		}
		String[] words = text.split(Pattern.quote(" "));
		
		for (int n = 1; n <= 10 && n <= words.length; n += 1)
		{
			for (int i = 0; i <= words.length - n; i += 1)
			{
				String phrase = "";
				for (int j = 0; j < n; j += 1)
				{
					phrase += words[i + j] + " ";
				}
				Text output = new Text(phrase.trim());
				context.write(output, NullWritable.get());
			}
		}
	}
}