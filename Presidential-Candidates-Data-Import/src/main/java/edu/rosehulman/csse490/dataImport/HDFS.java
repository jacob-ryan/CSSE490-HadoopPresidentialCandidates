package edu.rosehulman.csse490.dataImport;

import java.io.*;

public class HDFS
{
	public static synchronized void removeFromHDFS(String hdfsPath) throws IOException, InterruptedException
	{
		String command = "hadoop fs -rm -r " + hdfsPath;
		System.out.println(command);
		Runtime.getRuntime().exec(command).waitFor();
	}

	public static synchronized void uploadToHDFS(String localPath, String hdfsPath) throws IOException, InterruptedException
	{
		String command = "hadoop fs -put " + localPath + " " + hdfsPath;
		System.out.println(command);
		Runtime.getRuntime().exec(command).waitFor();
	}
}