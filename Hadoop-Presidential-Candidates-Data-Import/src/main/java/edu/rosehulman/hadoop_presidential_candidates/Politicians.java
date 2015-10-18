package edu.rosehulman.hadoop_presidential_candidates;

import java.util.ArrayList;
import java.util.HashMap;

public class Politicians {
	private HashMap<String, ArrayList<String>> republicans;
	private HashMap<String, ArrayList<String>> democrats;
	
	public Politicians(){
		Initialize();
	}
	
	@SuppressWarnings("serial")
	private void Initialize() {
		republicans = new HashMap<String, ArrayList<String>>();
		democrats = new HashMap<String, ArrayList<String>>();
		
		republicans.put("Jeb Bush", new ArrayList<String>() {{
			add("JebBush");
		}});
		republicans.put("Ben Carson", new ArrayList<String>() {{
			add("RealBenCarson");
		}});
		republicans.put("Chris Christie", new ArrayList<String>() {{
			add("ChrisChristie");
			add("GovChristie");
		}});
		republicans.put("Ted Cruz", new ArrayList<String>() {{
			add("tedcruz");
		}});
		republicans.put("Carly Fiorina", new ArrayList<String>() {{
			add("CarlyFiorina");
		}});
		republicans.put("Jim Gilmore", new ArrayList<String>() {{
			add("gov_gilmore");
		}});
		republicans.put("Lindey Graham", new ArrayList<String>() {{
			add("LindeyGrahamSC");
		}});
		republicans.put("Mike Huckabee", new ArrayList<String>() {{
			add("GovMikeHuckabee");
		}});
		republicans.put("Bobby Jindal", new ArrayList<String>() {{
			add("Jindalin2016");
			add("BobbyJindal");
		}});
		republicans.put("John Kasich", new ArrayList<String>() {{
			add("JohnKasich");
		}});
		republicans.put("George Pataki", new ArrayList<String>() {{
			add("GovenorPataki");
			add("Gpataki2016");
		}});
		republicans.put("Rand Paul", new ArrayList<String>() {{
			add("RandPaul");
			add("Rand2016");
		}});
		republicans.put("Marco Rubio", new ArrayList<String>() {{
			add("marcorubio");
		}});
		republicans.put("Rick Santorum", new ArrayList<String>() {{
			add("RickSantorum");
		}});
		republicans.put("Donald Trump", new ArrayList<String>() {{
			add("realDonaldTrump");
		}});
		
		democrats.put("Lincoln Chafee", new ArrayList<String>() {{
			add("LincolnChafee");
			add("Chafee2016");
		}});
		democrats.put("Hillary Clinton", new ArrayList<String>() {{
			add("HillaryClinton");
			add("HillaryIn2016");
		}});
		democrats.put("Lawrence Lessig", new ArrayList<String>() {{
			add("lessig");
			add("Lessig2016");
		}});
		democrats.put("Martin O'Malley", new ArrayList<String>() {{
			add("GovenoreOMalley");
			add("MartinOMalley");
		}});
		democrats.put("Bernie Sanders", new ArrayList<String>() {{
			add("BernieSanders");
		}});
		democrats.put("Jim Webb", new ArrayList<String>() {{
			add("JimWebbUSA");
		}});
	}
	
	/**
	 * Returns all of the politicans currently in the presedential election
	 * 
	 * @return HashMap<String:Name, ArrayList<String>:TwitterUsername
	 */
	public HashMap<String, ArrayList<String>> GetAllPoliticians() {
		HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
		for (String nom : republicans.keySet()) {
			map.put(nom, republicans.get(nom));
		}
		for (String nom : democrats.keySet()) {
			map.put(nom, democrats.get(nom));
		}
		
		return map;
	}
	
	public HashMap<String, ArrayList<String>> GetRepublicans() {
		return republicans;
	}
	
	public HashMap<String, ArrayList<String>> GetDemocrats() {
		return democrats;
	}
}
