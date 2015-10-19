package edu.rosehulman.hadoop_presidential_candidates;

import java.io.IOException;

public class HDFSUploader {
	public static void DeleteAndUpload(String hdfsPath, String directoryName) throws IOException, InterruptedException {
		RemoveFromHDFS(hdfsPath, directoryName);
		UploadFromSandbox(hdfsPath, directoryName);
	}

	private static void RemoveFromHDFS(String path, String directoryName) throws IOException, InterruptedException {
		String command = "hadoop fs -rm -r " + path +  "/" + directoryName + "/";
		System.out.println(command);
		Runtime.getRuntime().exec(command).waitFor();
	}

	private static void UploadFromSandbox(String hdfsPath, String directoryName) throws IOException, InterruptedException {
		String command = "hadoop fs -put " + directoryName + " " + hdfsPath;
		System.out.println(command);
		Runtime.getRuntime().exec(command).waitFor();
	}
}
