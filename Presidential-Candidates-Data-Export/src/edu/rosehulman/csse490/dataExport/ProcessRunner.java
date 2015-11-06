package edu.rosehulman.csse490.dataExport;

import java.io.*;

public class ProcessRunner
{
	public ProcessRunner(String... arguments)
	{
		try
		{
			Process process = Runtime.getRuntime().exec(arguments);
			new ProcessLogger(process.getInputStream());
			new ProcessLogger(process.getErrorStream());
			process.waitFor();
		}
		catch (IOException | InterruptedException e)
		{
			System.out.println("ProcessRunner:  An exception occurred while trying to run: " + arguments);
			e.printStackTrace();
		}
	}
	
	private class ProcessLogger extends Thread
	{
		private BufferedReader reader;
		
		public ProcessLogger(InputStream inputStream)
		{
			this.reader = new BufferedReader(new InputStreamReader(inputStream));
			start();
		}
		
		@Override
		public void run()
		{
			try
			{
				String line = null;
				while ((line = this.reader.readLine()) != null)
				{
					System.out.println("Log: " + line);
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}