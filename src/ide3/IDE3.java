package ide3;

import java.util.List;

import statistics.Statistics;
import tree.DecisionTree;
import tree.Leaf;
import tree.Node;
import data.TableManager;

public class IDE3 {

	public static DecisionTree ide3(TableManager tm) throws RuntimeException {
		Node node;
		Leaf leaf;
		List<String> nonObjectiveAttributes = tm.getNonObjectiveAttributes();
		
		if(nonObjectiveAttributes.isEmpty()){
			//Error with the table. Throw Exception.
			throw new RuntimeException("Tabla en formato incorrecto. Le faltan columnas.");
		}else if(nonObjectiveAttributes.size()==1){
			//Get the most probable objective value for the attribute value.
			node = new Node(nonObjectiveAttributes.get(0));
			for(String str : tm.getPossibleValues(nonObjectiveAttributes.get(0))){
				leaf = new Leaf(tm.getObjectiveAttribute(), tm.getBestObjectiveValueFromAttribute(nonObjectiveAttributes.get(0), str));
				node.addChild(str, leaf);
			}
		}else{
			TableManager subTabla;
			String straux, attribute = getGreatestGain(tm);
			
			node = new Node(attribute);
			for(String value : tm.getPossibleValues(attribute)){
				straux = tm.hasOneObjectiveValue(attribute, value);
				if(straux != null){
					leaf = new Leaf(tm.getObjectiveAttribute(), straux);
					node.addChild(value, leaf);
				}else{
					subTabla=tm.getSubTable(attribute, value);
					node.addChild(value, ide3(subTabla));
				}
			}
		}
		return node;
	}
	
	public static String getGreatestGain(TableManager tm) {
		String ggAttr = null;
		Double greatestGain = -1.0;
		
		for(String attr : tm.getNonObjectiveAttributes()) {
			Double gain = Statistics.gain(tm, attr);
			
			if(ggAttr == null)
				ggAttr = attr;
			if(greatestGain == -1.0)
				greatestGain = gain;
			
			if(gain > greatestGain) {
				ggAttr = attr;
				greatestGain = gain;
			}
		}
			
		return ggAttr;
	}
	
	
}
