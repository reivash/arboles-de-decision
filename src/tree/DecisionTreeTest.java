package tree;

import ide3.IDE3;

import java.util.ArrayList;
import java.util.List;

import data.DataManager;
import data.TableManager;

public class DecisionTreeTest {

	public static void main(String[] args) {
		TableManager tm = new DataManager("test.csv"), tmProbes;
		DecisionTree tree = IDE3.ide3(tm);
		List<String> attributes, values, listaux;
		int c, numberOfAleatoryProbes = 10;
		
		//DecisionTreeDrawer.drawDecisionTree((tree));
		
		attributes = new ArrayList<String>();
		attributes.add("Altura");
		attributes.add("ColorOjos");
		attributes.add("ColorPelo");
		
		//Test 1
		System.out.println("Introducing data 1 to evaluate.");
		values = new ArrayList<String>();
		values.add("Bajo");
		values.add("Azules");
		values.add("Rojo");
		if(!tree.evaluate(attributes, values).equals("Mas")) throw new RuntimeException("Error with the first evaluation.");
		System.out.println("Data 1 evaluated succesfully.");
		
		//Test 2
		System.out.println("Introducing data 2 to evaluate.");
		values = new ArrayList<String>();
		values.add("Alto");
		values.add("Marrones");
		values.add("Rojo");
		if(!tree.evaluate(attributes, values).equals("Mas")) throw new RuntimeException("Error with the second evaluation.");
		System.out.println("Data 2 evaluated succesfully.");
		
		//Test 3
		System.out.println("Introducing data 3 to evaluate.");
		values = new ArrayList<String>();
		values.add("Bajo");
		values.add("Marrones");
		values.add("Rojo");
		if(!tree.evaluate(attributes, values).equals("Mas")) throw new RuntimeException("Error with the third evaluation.");
		System.out.println("Data 3 evaluated succesfully.");
		
		//Test 4
		System.out.println("Introducing data 4 to evaluate.");
		values = new ArrayList<String>();
		values.add("Alto");
		values.add("Azules");
		values.add("Rojo");
		if(!tree.evaluate(attributes, values).equals("Mas")) throw new RuntimeException("Error with the fourth evaluation.");
		System.out.println("Data 4 evaluated succesfully.");
		
		//Test 5
		System.out.println("Introducing data 5 to evaluate.");
		values = new ArrayList<String>();
		values.add("Bajo");
		values.add("Azules");
		values.add("Moreno");
		if(!tree.evaluate(attributes, values).equals("Menos")) throw new RuntimeException("Error with the fifth evaluation.");
		System.out.println("Data 5 evaluated succesfully.");
		
		//Test 6
		System.out.println("Introducing data 6 to evaluate.");
		values = new ArrayList<String>();
		values.add("Alto");
		values.add("Marrones");
		values.add("Moreno");
		if(!tree.evaluate(attributes, values).equals("Menos")) throw new RuntimeException("Error with the sixth evaluation.");
		System.out.println("Data 6 evaluated succesfully.");
		
		//Test 7
		System.out.println("Introducing data 7 to evaluate.");
		values = new ArrayList<String>();
		values.add("Bajo");
		values.add("Marrones");
		values.add("Moreno");
		if(!tree.evaluate(attributes, values).equals("Menos")) throw new RuntimeException("Error with the seventh evaluation.");
		System.out.println("Data 7 evaluated succesfully.");
		
		//Test 8
		System.out.println("Introducing data 8 to evaluate.");
		values = new ArrayList<String>();
		values.add("Alto");
		values.add("Azules");
		values.add("Moreno");
		if(!tree.evaluate(attributes, values).equals("Menos")) throw new RuntimeException("Error with the eighth evaluation.");
		System.out.println("Data 8 evaluated succesfully.");
		
		//Test 9
		System.out.println("Introducing data 9 to evaluate.");
		values = new ArrayList<String>();
		values.add("Alto");
		values.add("Azules");
		values.add("Rubio");
		if(!tree.evaluate(attributes, values).equals("Mas")) throw new RuntimeException("Error with the ninth evaluation.");
		System.out.println("Data 9 evaluated succesfully.");
		
		//Test 10
		System.out.println("Introducing data 10 to evaluate.");
		values = new ArrayList<String>();
		values.add("Bajo");
		values.add("Azules");
		values.add("Rubio");
		if(!tree.evaluate(attributes, values).equals("Mas")) throw new RuntimeException("Error with the tenth evaluation.");
		System.out.println("Data 10 evaluated succesfully.");
		
		//Test 11
		System.out.println("Introducing data 11 to evaluate.");
		values = new ArrayList<String>();
		values.add("Alto");
		values.add("Marrones");
		values.add("Rubio");
		if(!tree.evaluate(attributes, values).equals("Menos")) throw new RuntimeException("Error with the eleventh evaluation.");
		System.out.println("Data 11 evaluated succesfully.");
		
		//Test 12
		System.out.println("Introducing data 12 to evaluate.");
		values = new ArrayList<String>();
		values.add("Bajo");
		values.add("Marrones");
		values.add("Rubio");
		if(!tree.evaluate(attributes, values).equals("Menos")) throw new RuntimeException("Error with the twelfth evaluation.");
		System.out.println("Data 12 evaluated succesfully.");
		
		System.out.println("All the possible cases for the example tree are correct.");
		
		System.out.println("Testing write the results.");
		if (tree.evaluate(tm, "out.csv")) {
			System.out.println("Writting in a csv file succesfully.");
		}else{
			System.out.println("Error writting in a csv file.");
		}
		
		
		//Test 13
		System.out.println("\nIntroducing aleatory set of data.");
		c=0;
		for(int i=0; i<numberOfAleatoryProbes; i++){
			tm = new DataManager("test.csv");
			tmProbes = tm.getTrainAndProbeSet(75.0);
			tree = IDE3.ide3(tm);
			listaux=tree.evaluate(tmProbes);
			if(tmProbes.getRow(0).get(0).equals(listaux.get(0))){
				c++;
			}else{
				System.out.println("\nError in the prediction number "+2*i+":");
				System.out.println("Train "+tm);
				System.out.println("Probes "+tmProbes);
				System.out.println("Predictions: "+listaux);
				//DecisionTreeDrawer.drawDecisionTree((tree2));
			}
			if(tmProbes.getRow(1).get(0).equals(listaux.get(1))){
				c++;
			}else{
				System.out.println("\nError in the prediction number "+(2*i+1)+":");
				System.out.println("Train "+tm);
				System.out.println("Probes "+tmProbes);
				System.out.println("Predictions: "+listaux);
				//DecisionTreeDrawer.drawDecisionTree((tree2));
			}
			/**
			if(map2.get("Salida").get(1).equals(listaux.get(1))){
				c++;
			}else{
				System.out.println(map2);
				System.out.println(tree2.evaluate(map2, 2));
				DecisionTreeDrawer.drawDecisionTree((tree2));
			}
			*/
			
		}
		
		
		System.out.println("For "+2*numberOfAleatoryProbes+" aleatory probes the algorithm predict correctment "+c+" times.");
		
	}
}
