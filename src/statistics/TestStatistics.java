package statistics;
import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import data.DataManager;
import data.TableManager;

/**
 * This class tests the static functions from Statistics class
 * using the examples view in the laboratory of 'Aprendizaje Computacional'.
 *  
 * @author Cabero
 *
 */
public class TestStatistics {

	private static TableManager tm;
	private static double eps = 0.00001;
	
	@BeforeClass
	public static void loadData() {
		tm = new DataManager("example.csv");
		System.out.println(tm.toString());
	}
	
	@Test 
	public void testProbability() {
		assertEquals("Probability of AF=Si",   0.714285,     tm.getProbability("AF", "S�"),   eps);
		assertEquals("Probability of AF=No",   1 - 0.714285, tm.getProbability("AF", "No"),   eps);
		assertEquals("Probability of PA=Alta", 0.42857,      tm.getProbability("PA", "Alta"), eps);
	}

	@Test
	public void testEntropy() {
		assertEquals("Entropy of AF must be 0.863121", 0.863121, Statistics.entropy(tm, "AF"), eps);
	}

	@Test
	public void testConditionalEntropy() {
		assertEquals("Conditional entropy of AF=Alta must be 0.918296",  0.918296, Statistics.conditionalEntropy(tm, "PA", "Alta"), eps);
		assertEquals("Conditional entropy of AF=Media must be 0.918296", 0.918296, Statistics.conditionalEntropy(tm, "PA", "Media"), eps);
		assertEquals("Conditional entropy of AF=Media must be 0", 		 0, 	   Statistics.conditionalEntropy(tm, "PA", "Baja"), eps);

		assertEquals("Conditional entropy of AA=No must be 0.721928", 0.721928, Statistics.conditionalEntropy(tm, "AA", "No"), eps);
		assertEquals("Conditional entropy of OA=Si must be 0.991076", 0.991076, Statistics.conditionalEntropy(tm, "OA", "S�"), eps);
	}
	
	@Test
	public void testGain() {
		assertEquals("Gain of PA must be 0.272788", 0.272788, Statistics.gain(tm, "PA"), eps);
		assertEquals("Gain of AS must be 0",        0,        Statistics.gain(tm, "AS"), eps);
	}
}
