package CyclePricing;


import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.Spy;

import Model.Cycle;
import Model.Components.Frame.Diamond;
import Model.Components.Frame.Frame;
import Model.Components.Frame.Steel;

public class TestCycleEstimator {
	CycleEstimator cycleEstimator = new CycleEstimator();
	Cycle cycle = new Cycle();
	BlockingQueue<Map<String, Object>> blockingQueue = new ArrayBlockingQueue<>(10);
	CyclePriceCalculator cyclePriceCalculator = new CyclePriceCalculator(cycle, blockingQueue);
	CyclePriceCalculator cyclePriceCalculatorSpy;
	
	//@Spy
	//CyclePriceCalculator cyclePriceCalculatorSpy = new CyclePriceCalculator(cycle, blockingQueue);

	Map<String, Integer> setCostMap() {
		Map<String, Integer> costMap = new HashMap<>();
		costMap.put("year", 2020);
		costMap.put("jan", 250);
		costMap.put("feb", 250);
		costMap.put("mar", 250);
		costMap.put("apr", 260);
		costMap.put("may", 260);
		costMap.put("jun", 260);
		costMap.put("jul", 260);
		costMap.put("aug", 270);
		costMap.put("sep", 270);
		costMap.put("oct", 280);
		costMap.put("nov", 280);
		costMap.put("dec", 280);
		return costMap;
	}

	@Test
	public void testcyclePriceCalculator() {
		Map<String, String> cyclePriceMap = new HashMap<>();
		cyclePriceMap.put("frame", "1200");
		cyclePriceMap.put("seating", "580");
		cyclePriceMap.put("wheels", "895");
		cyclePriceMap.put("chainAssembly", "800");
		cyclePriceMap.put("handleBar", "620");
		String expected = "4095";
		String actual = cyclePriceCalculator.cyclePriceCalculator(cyclePriceMap);
		assertEquals(expected, actual);
	}

	@Test
	public void testGetCurrentYear() {
		String expected = "2020";
		String actual = cyclePriceCalculator.getCurrentYear();
		assertEquals(expected, actual);
	}

	@Test
	public void testGetCurrentMonth() {
		String expected = "jul";
		String actual = cyclePriceCalculator.getCurrentMonth();
		assertEquals(expected, actual);
	}

	@Test
	public void testCycleBuilder() {
		Map<String, Object> cycleMap = new HashMap<>();
		cycleMap.put("name", "cycle");
		Map<String, String> dateMap = new HashMap<>();
		dateMap.put("year", "2020");
		dateMap.put("month", "jul");
		cycleMap.put("date_of_pricing", dateMap);
		cycleMap.put("frame", "diamond, steel");
		cycleMap.put("wheels", "");
		cycleMap.put("chainAssembly", "fixedGear");
		cycleMap.put("seating", "comfort");
		cycleMap.put("handleBar", "pursuit");
		Map<String, Object> cycleMap1 = new HashMap<>();
		cycleMap1.put("name", "cycle");
		cycleMap.put("wheels", "");
		assertEquals(cycleMap.toString(), cyclePriceCalculator.cycleBuilder(cycleMap1).toString());
	}
	
	@Before
	public void setUp(){
		cyclePriceCalculatorSpy = Mockito.mock(CyclePriceCalculator.class);
	}
	
	@Test
	public void testFrameRateCalculator1() {
		String expected = "0";
		Frame frame = new Frame();
		Diamond[] diamond = new Diamond[1];
		diamond[0] = new Diamond();
		diamond[0].setYear(2020);
		diamond[0].setCostMap(setCostMap());
		frame.setDiamond(diamond);
		Steel[] steel = new Steel[1];
		steel[0] = new Steel();
		steel[0].setYear(2020);
		steel[0].setCostMap(setCostMap());
		frame.setSteel(steel);
		cycle.setFrame(frame);
		Map<String, Integer> subComponentMap = new HashMap<>();
		subComponentMap.put("yearFlag", 1);
		subComponentMap.put("rate", 520);
		subComponentMap.put("currentIndex", 0);
		subComponentMap.put("year", 2020);
		//CyclePriceCalculator cyclePriceCalculatorSpy = Mockito.mock(CyclePriceCalculator.class);
		Mockito.when(cyclePriceCalculatorSpy.findRate(Mockito.anyMap(), 
				Mockito.anyString(), Mockito.anyString(), Mockito.anyMap(), Mockito.anyInt()))
		.thenReturn(subComponentMap);
		String actual = cyclePriceCalculator.frameRateCalculator("diamond, steel", "2020", "jul", cycle);
		assertEquals(expected, actual);
	}

	@Test
	public void testFindRate() {
		Map<String, Integer> subComponentMap = new HashMap<>();
		subComponentMap.put("rate", 250);
		subComponentMap.put("yearFlag", 1);
		subComponentMap.put("currentIndex", 0);
		subComponentMap.put("year", 2020);
		Map<String, Integer> subComponentMap1 = new HashMap<>();
		subComponentMap1.put("year", 2020);
		Map<String, Integer> costMap = new HashMap<>();
		costMap.put("jul", 250);
		Map<String, Integer> subComponentMap2 = cyclePriceCalculator.findRate(subComponentMap1, "2020", "jul", costMap,
				0);
		assertEquals(subComponentMap, subComponentMap2);
	}

	
}