package edu.rosehulman.csse490.mapReduce.retweet;

import org.apache.hadoop.util.*;

public class Main
{
	public static void main(String[] args) throws Exception
	{
		int exitCode = ToolRunner.run(new RetweetDriver(), args);
		System.exit(exitCode);
	}
}