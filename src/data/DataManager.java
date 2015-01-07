package data;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

public class DataManager implements TableManager {
	public static final String SEPARADOR = ",";
	List<String> attributes = new ArrayList<String>();
	Map<String, Set<String>> possibleValues = new HashMap<String, Set<String>>();
	List<List<String>> recordMatrix = new ArrayList<List<String>>();

	public DataManager(String path) {
		Scanner sc;
		try {
			sc = new Scanner(new File(path));
			loadAttributes(sc.nextLine());
			loadData(sc);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public DataManager(List<List<String>> record, List<String> att,
			Map<String, Set<String>> pValues) {
		recordMatrix = record;
		attributes = att;
		possibleValues = pValues;
	}
	
	public DataManager(String path, String attObjective){
		this(path);
		String straux;
		int attColumn = attributes.lastIndexOf(attObjective);
		if(attColumn != 0){
			for(int i=0;i<attColumn;i++){
				straux = recordMatrix.get(i).remove(attColumn);				 
				recordMatrix.get(i).add(0,straux);
			}
			straux = attributes.remove(attColumn);
			attributes.add(0, straux);
		}
	}

	/**
	 * Load the header with its attributes
	 */
	private void loadAttributes(String line) {
		String[] atts = line.split(SEPARADOR);

		for (String s : atts) {
			attributes.add(s);
			possibleValues.put(s, new HashSet<String>());
			// recordMatrix.add(new ArrayList<String>());
		}
	}

	/**
	 * Loads the data that has been previously loaded in the scanner.
	 */
	private void loadData(Scanner sc) {
		while (sc.hasNext()) {
			// Read the line and split it by the separator (def: ',');
			String line = sc.nextLine();
			String[] instanceValues = line.split(SEPARADOR);

			// Generate the new record
			// We assume the data for each attribute comes correctly ordered
			// which is a rather stupid assumption to make.
			List<String> record = new ArrayList<String>();
			for (int i = 0; i < instanceValues.length; i++) {
				record.add(instanceValues[i]);

				String attribute = attributes.get(i);
				Set<String> pValues = possibleValues.get(attribute);
				if (!pValues.contains(instanceValues[i])) {
					pValues.add(instanceValues[i]);
				}
			}

			recordMatrix.add(record);
		}
	}

	/**
	 * Returns the whole record set
	 */
	public List<List<String>> getRows() {
		return new ArrayList<List<String>>(recordMatrix);
	}
	
	/**
	 * Returns a row.
	 */
	
	public List<String> getRow(int i){
		return recordMatrix.get(i);
	}

	/**
	 * Returns all the instances in a column by column id
	 */
	public List<String> getColumn(int column) {
		List<String> instances = new ArrayList<String>();
		for (List<String> record : recordMatrix) {
			instances.add(record.get(column));
		}

		return instances;
	}

	/**
	 * Returns all the instances in a column by column name
	 */
	public List<String> getColumn(String column) {
		return getColumn(attributes.lastIndexOf(column));
	}
	
	public List<String> getAttributes(){
		return attributes;
	}

	/**
	 * Returns all the records filtering by the value in one of the attributes
	 */
	public List<List<String>> getRowsByKeyValue(String attribute, String value) {
		int attributeIndex = attributes.lastIndexOf(attribute);
		List<List<String>> records = new ArrayList<List<String>>();

		for (List<String> instance : recordMatrix) {
			if (instance.get(attributeIndex).equals(value)) {
				records.add(new ArrayList<String>(instance));
			}
		}
		return records;
	}

	/**
	 * Returns all the possible values for an attribute
	 */
	public Set<String> getPossibleValues(String attribute) {
		return possibleValues.get(attribute);
	}

	/**
	 * Returns the objective attribute.
	 */
	public String getObjectiveAttribute() {
		return attributes.get(0);
	}

	/**
	 * Returns the objective attribute value.
	 */

	public Set<String> getObjectiveAttributeValues() {
		return possibleValues.get(getObjectiveAttribute());
	}

	/**
	 * Returns the subtable without the attribute column and only with the rows
	 * that includes the specific value for the specific attribute.
	 */

	public DataManager getSubTable(String attribute, String value) {
		List<List<String>> subTable = new ArrayList<List<String>>();
		List<String> auxl;
		List<String> subAttributes = new ArrayList<String>();
		Map<String, Set<String>> subMap = new HashMap<String, Set<String>>();
		int attcolumn = attributes.lastIndexOf(attribute);

		// Generates the new subTable.
		for (int i = 0; i < recordMatrix.size(); i++) {
			auxl = new ArrayList<String>();
			for (int j = 0; j < recordMatrix.get(i).size(); j++) {
				if (attcolumn != j && recordMatrix.get(i).get(attcolumn).equals(value)) {
					auxl.add(recordMatrix.get(i).get(j));
				}
			}
			if (!auxl.isEmpty()) subTable.add(auxl);
		}

		// Generates the new list of attributes.
		for (int i = 0; i < attributes.size(); i++) {
			if (!attributes.get(i).equals(attribute)) {
				subAttributes.add(attributes.get(i));
			}
		}
		
		// Generates the new map with the attributes and the possible values.
		/**
		for (int i = 0; i < subAttributes.size(); i++) {
			auxatt = new HashSet<String>();
			for (int j = 0; j < subTable.size(); j++) {
				auxatt.add(subTable.get(j).get(i));
			}
			subMap.put(subAttributes.get(i), auxatt);
		}
		*/
		for(int i = 0; i<subAttributes.size(); i++){
			subMap.put(subAttributes.get(i), possibleValues.get(subAttributes.get(i)));
		}

		return new DataManager(subTable, subAttributes, subMap);
	}
	
	public String getBestObjectiveValueFromAttribute(String attribute, String value){
		Map<String, Integer> map = new HashMap<String, Integer>();
		List<String> attColumn = this.getColumn(attribute);
		List<String> objColumn = this.getColumn(0);
		String attGain = "Error";
		//Get the number of appearances of each objective value for the rows with the attribute value.
		for(int i=0; i<objColumn.size(); i++){
			if(!map.containsKey(objColumn.get(i))){
				map.put(objColumn.get(i), 0);
			}
			if(attColumn.get(i).equals(value)){
				map.put(objColumn.get(i), map.get(objColumn.get(i)) +1 );
			}
			/**
			if(attColumn.get(i).equals(value)){
				if(!map.containsKey(objColumn.get(i))){
					map.put(objColumn.get(i), 1);
				}else{
					//map.get(objColumn.get(i))++;
					map.put(objColumn.get(i), map.get(objColumn.get(i)) +1 );
				}
			}
			*/
		}
		//Get the maximum value of the list.
		for(String str : map.keySet()){
			if(attGain.equals("Error") || map.get(str)>map.get(attGain)){
				attGain=str;
			}
		}
		if(map.keySet().isEmpty()) System.out.println("Best objective debug: value: "+value+" attribute :"+attribute+" objColumn: "+objColumn);
		return attGain;
	}

	public Double getProbability(String attribute, String value) {
		Double p = 0.0;
		List<String> c = getColumn(attribute);
		for (String v : c)
			if (v.equals(value))
				p++;
		p /= c.size();

		return p;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder("Table\n");
		for (List<String> record : recordMatrix) {
			sb.append(record.toString() + "\n");
		}
		
		return sb.toString();
	}

	public List<String> getNonObjectiveAttributes() {
		List<String> attrs = new ArrayList<String>(attributes);
		attrs.remove(getObjectiveAttribute());
		return attrs;
	}
	
	/**
	 * Returns one objective value if all the rows with value at attribute has the same objective value. Returns null in another chase.
	 */
	public String hasOneObjectiveValue(String attribute, String value) {
		int attColumn = attributes.lastIndexOf(attribute);
		String str = null;
		boolean b = true;
		for(int i=0; i<recordMatrix.size(); i++){
			if(b && recordMatrix.get(i).get(attColumn).equals(value)){
				if(str == null) str = recordMatrix.get(i).get(0);
				if(!recordMatrix.get(i).get(0).equals(str)){
					b = false;
					str = null;
				}
			}
		}
		return str;
	}
	
	/**
	 * Modifies the table to obtain a table with the p percent of data. Returns a map of tests with the rest.
	 */
	public TableManager getTrainAndProbeSet(Double p){
		Random rnd = new Random();
		List<String>  subAttributes = new ArrayList<String>();
		List<List<String>> subRecord = new ArrayList<List<String>>();
		int i,c=0; //Variable to count the number of lists taken for training.
		int originalMatrixSize = recordMatrix.size();
		for (i=0; i<attributes.size(); i++){
			subAttributes.add(attributes.get(i));
		}
		i=0;
		while(i<recordMatrix.size()){
			//We need to observe if the p percent is not obtained and if it could be obtained.
			if(originalMatrixSize*(p/100) > c  && subRecord.size() < ((100.0-p)/100*originalMatrixSize)){
				if(rnd.nextBoolean()){
					//We take this line for training.
					c++;
					i++;
				}else{
					//We take this line for testing.
					subRecord.add(recordMatrix.get(i));					
					recordMatrix.remove(i);
					//We don't increment the variable i because we have reduced the size of recordMatrix in one.
				}
			}else{
				if(originalMatrixSize*(p/100) <= c){
					//We take this line for testing.
					subRecord.add(recordMatrix.get(i));					
					recordMatrix.remove(i);
					//We don't increment the variable i because we have reduced the size of recordMatrix in one.
				}else{
					//We take this line for training.
					c++;
					i++;
				}
			}
		}		
		return new DataManager(subRecord, subAttributes, possibleValues);
	}

}
