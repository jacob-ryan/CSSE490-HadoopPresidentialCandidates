package edu.csse490.storm;

import java.io.*;

public class ConfigReader
{
	private static final String fileLocation = "./access-keys.txt";
	
	private String consumerKey;
	private String consumerSecret;
	private String accessToken;
	private String accessTokenSecret;
	
	public ConfigReader()
	{
		try
		{
			File file = new File(ConfigReader.fileLocation);
			try (BufferedReader reader = new BufferedReader(new FileReader(file)))
			{
				this.consumerKey = reader.readLine();
				this.consumerSecret = reader.readLine();
				this.accessToken = reader.readLine();
				this.accessTokenSecret = reader.readLine();
			}
		}
		catch (IOException e)
		{
			System.out.println("***** edu.rosehulman.csse490.dataImport.ConfigReader *****");
			System.out.println("Error:  Could not read configuration file!  Exception:");
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public String getConsumerKey()
	{
		return this.consumerKey;
	}
	
	public String getConsumerSecret()
	{
		return this.consumerSecret;
	}
	
	public String getAccessToken()
	{
		return this.accessToken;
	}
	
	public String getAccessTokenSecret()
	{
		return this.accessTokenSecret;
	}
}