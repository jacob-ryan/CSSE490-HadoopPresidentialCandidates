package edu.rosehulman.csse490.dataImport;

import java.util.*;

public class Politicians
{
	private HashMap<String, ArrayList<String>> republicans;
	private HashMap<String, ArrayList<String>> democrats;

	public Politicians()
	{
		this.republicans = new HashMap<String, ArrayList<String>>();
		this.democrats = new HashMap<String, ArrayList<String>>();

		this.republicans.put("Jeb Bush", new ArrayList<String>()
				{
			{
				add("JebBush");
			}
				});
		this.republicans.put("Ben Carson", new ArrayList<String>()
				{
			{
				add("RealBenCarson");
			}
				});
		this.republicans.put("Chris Christie", new ArrayList<String>()
				{
			{
				add("ChrisChristie");
				add("GovChristie");
			}
				});
		this.republicans.put("Ted Cruz", new ArrayList<String>()
				{
			{
				add("tedcruz");
			}
				});
		this.republicans.put("Carly Fiorina", new ArrayList<String>()
				{
			{
				add("CarlyFiorina");
			}
				});
		this.republicans.put("Jim Gilmore", new ArrayList<String>()
				{
			{
				add("gov_gilmore");
			}
				});
		this.republicans.put("Lindey Graham", new ArrayList<String>()
				{
			{
				add("LindseyGrahamSC");
			}
				});
		this.republicans.put("Mike Huckabee", new ArrayList<String>()
				{
			{
				add("GovMikeHuckabee");
			}
				});
		this.republicans.put("Bobby Jindal", new ArrayList<String>()
				{
			{
				// add("Jindalin2016");
				add("BobbyJindal");
			}
				});
		this.republicans.put("John Kasich", new ArrayList<String>()
				{
			{
				add("JohnKasich");
			}
				});
		this.republicans.put("George Pataki", new ArrayList<String>()
				{
			{
				add("GovernorPataki");
				add("Gpataki2016");
			}
				});
		this.republicans.put("Rand Paul", new ArrayList<String>()
				{
			{
				add("RandPaul");
				add("Rand2016");
			}
				});
		this.republicans.put("Marco Rubio", new ArrayList<String>()
				{
			{
				add("marcorubio");
			}
				});
		this.republicans.put("Rick Santorum", new ArrayList<String>()
				{
			{
				add("RickSantorum");
			}
				});
		this.republicans.put("Donald Trump", new ArrayList<String>()
				{
			{
				add("realDonaldTrump");
			}
				});

		this.democrats.put("Lincoln Chafee", new ArrayList<String>()
				{
			{
				add("LincolnChafee");
			}
				});
		this.democrats.put("Hillary Clinton", new ArrayList<String>()
				{
			{
				add("HillaryClinton");
				add("HillaryIn2016");
			}
				});
		this.democrats.put("Lawrence Lessig", new ArrayList<String>()
				{
			{
				add("lessig");
				add("Lessig2016");
			}
				});
		this.democrats.put("Martin O'Malley", new ArrayList<String>()
				{
			{
				add("MartinOMalley");
			}
				});
		this.democrats.put("Bernie Sanders", new ArrayList<String>()
				{
			{
				add("BernieSanders");
			}
				});
		this.democrats.put("Jim Webb", new ArrayList<String>()
				{
			{
				add("JimWebbUSA");
			}
				});
	}

	/**
	 * Returns all of the politicians currently in the presidential election
	 *
	 * @return HashMap<String:Name, ArrayList<String>:TwitterUsername
	 */
	public HashMap<String, ArrayList<String>> GetAllPoliticians()
	{
		HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
		map.putAll(this.democrats);
		map.putAll(this.republicans);
		return map;
	}
	
	public ArrayList<String> GetAllHandles()
	{
		ArrayList<String> results = new ArrayList<String>();
		for (ArrayList<String> handles : GetAllPoliticians().values())
		{
			results.addAll(handles);
		}
		return results;
	}

	public HashMap<String, ArrayList<String>> GetRepublicans()
	{
		return this.republicans;
	}

	public HashMap<String, ArrayList<String>> GetDemocrats()
	{
		return this.democrats;
	}
}