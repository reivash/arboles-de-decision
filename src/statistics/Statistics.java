package statistics;

import java.util.List;
import java.util.Set;

import data.TableManager;

public class Statistics {

	public static double entropy(TableManager tm, String attribute) {

		double res = 0.0;
		Set<String> possibleValues =  tm.getPossibleValues(attribute);
		for (String value : possibleValues) {

			// Compute probability
			double p = tm.getProbability(attribute, value);
			
			// Add
			res += -p * log2(p);
		} 

		return res;
	}

	public static double conditionalEntropy(TableManager tm, 
										    String attribute, 
										    String value) {
		
		double res = 0.0;
		List<List<String>> rows = tm.getRowsByKeyValue(attribute, value);
		if(!rows.isEmpty()) {
			for(String targetAttr : tm.getObjectiveAttributeValues()) {
				
				// Compute probability
				double p = 0.0;
				for(List<String> row : rows) 
					if(targetAttr.equals(row.get(0))) 
						p++;
				p /= rows.size();
				
				// Add 
				if(p > 0) 
					res += -p * log2(p);
			}
		}

		return res;
	}
	
	public static double gain(TableManager tm, String attribute) {
		double gain = entropy(tm, tm.getObjectiveAttribute());
		
		for(String value : tm.getPossibleValues(attribute)) 
			gain -= tm.getProbability(attribute, value) * conditionalEntropy(tm, attribute, value);
		
		return gain;
	}

	public static double log2(double x) {
		return Math.log(x) / Math.log(2);
	}
}
