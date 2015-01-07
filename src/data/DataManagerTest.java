package data;

public class DataManagerTest {
	public static void main(String[] args){
		// Test 1: the file is loaded without errors
		DataManager dm = new DataManager("test.csv");
		System.out.println("First test passed: initialized without errors.");
		
		// Test 2: there should be 8 records
		if(dm.getRows().size() != 8) throw new RuntimeException("Test 2 failed");
		System.out.println("Second test passed: records loaded.");
		
		// Test 3a: There should be a value 'Bajo'
		// as the first element of the second column
		//
		// NOTE: WE START BY 0 SO THE INDEX IS 1!
		if(!dm.getColumn(1).get(0).equals("Bajo")) throw new RuntimeException("Test 3a failed");
		System.out.println("Third-a test passed: getting a column filters correctly. (1)");
		
		// Test 3b: same as 3a but filtering by attribute 'Altura'
		if(!dm.getColumn("Altura").get(0).equals("Bajo")) throw new RuntimeException("Test 3b failed");
		System.out.println("Third-b test passed: getting a column filters correctly. (2)");
	
		// Test 4: Filtering by values for an attribute
		// There should be 4 instances of 'Menos' for attribute 'Salida'
		if(!(dm.getRowsByKeyValue("Salida", "Menos").size()==5)) throw new RuntimeException("Test 4 failed");
		System.out.println("Fourth test passed: filtering rows.");
		
		// Test 5: Possible values for an attribute should be computed
		// There should be 3 possible values for ColorPelo:
		//		* Moreno
		//		* Rubio
		//		* Rojo
		if(!(dm.getPossibleValues("ColorPelo").size()==3)) throw new RuntimeException("Test 5 failed");
		if(!dm.getPossibleValues("ColorPelo").contains("Moreno")) throw new RuntimeException("Test 5 failed");
		if(!dm.getPossibleValues("ColorPelo").contains("Rubio")) throw new RuntimeException("Test 5 failed");
		if(!dm.getPossibleValues("ColorPelo").contains("Rojo")) throw new RuntimeException("Test 5 failed");
		System.out.println("Fifth test passed: possible values for an attribute.");
	}
}
