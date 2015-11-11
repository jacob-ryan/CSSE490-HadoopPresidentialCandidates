package edu.rosehulman.csse490.mapReduce.nGram;

import java.util.*;

import org.apache.hadoop.util.*;

public class Main
{
	private String[] args;
	
	public Main(String[] args)
	{
		this.args = args;
		
		System.out.println("Presidential Candidates MapReduce application has started.");
		Politicians candidates = new Politicians();
		List<String> candidateNames = candidates.GetAllHandles();
		
		for (String candidateName : candidateNames)
		{
			System.out.println("Launching MapReduce job for candidate: " + candidateName);
			new Launcher(candidateName);
		}
		
		System.out.println("Presidential Candidates MapReduce application has launched all jobs...");
	}
	
	public static void main(String[] args)
	{
		new Main(args);
	}
	
	private class Launcher extends Thread
	{
		private String candidateName;
		
		public Launcher(String candidateName)
		{
			this.candidateName = candidateName;
			start();
		}
		
		@Override
		public void run()
		{
			try
			{
				int exitCode = ToolRunner.run(new NGramDriver(this.candidateName), Main.this.args);
				System.out.println("Job for " + this.candidateName + " has completed with exit code: " + exitCode);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}