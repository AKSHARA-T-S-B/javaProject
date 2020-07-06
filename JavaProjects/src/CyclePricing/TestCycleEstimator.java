package CyclePricing;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.anyString;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.junit.Test;
import org.mockito.Mockito;

import Model.Cycle;
import Model.Components.ChainAssembly.ChainAssembly;
import Model.Components.ChainAssembly.FixedGear;
import Model.Components.ChainAssembly.Gear3;
import Model.Components.ChainAssembly.Gear4;
import Model.Components.ChainAssembly.Gear5;
import Model.Components.ChainAssembly.Gear6;
import Model.Components.ChainAssembly.Gear7;
import Model.Components.ChainAssembly.Gear8;
import Model.Components.Frame.Aluminium;
import Model.Components.Frame.Contilever;
import Model.Components.Frame.Diamond;
import Model.Components.Frame.Frame;
import Model.Components.Frame.Recumbent;
import Model.Components.Frame.Steel;
import Model.Components.Frame.StepThrough;
import Model.Components.Frame.Titanium;
import Model.Components.HandleBar.DropBar;
import Model.Components.HandleBar.HandleBar;
import Model.Components.HandleBar.PursuitBar;
import Model.Components.HandleBar.RiserBar;
import Model.Components.Seating.ComfortSaddle;
import Model.Components.Seating.CruiserSaddle;
import Model.Components.Seating.RacingSaddle;
import Model.Components.Seating.Seating;
import Model.Components.Wheels.Rim;
import Model.Components.Wheels.Spokes;
import Model.Components.Wheels.Tube;
import Model.Components.Wheels.Tyre;
import Model.Components.Wheels.Wheels;

public class TestCycleEstimator {
	CycleEstimator cycleEstimator = new CycleEstimator();
	Cycle cycle = new Cycle();
	BlockingQueue<Map<String, Object>> blockingQueue = new ArrayBlockingQueue<>(10);
	CyclePriceCalculator cyclePriceCalculator = new CyclePriceCalculator(cycle, blockingQueue);
	CyclePriceCalculator cyclePriceCalculatorSpy;

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

	@Test
	public void testInitialiseSubComponentMap() {
		Map<String, Integer> subComponentMap = new HashMap<>();
		subComponentMap.put("rate", 0);
		subComponentMap.put("yearFlag", 0);
		subComponentMap.put("currentIndex", 0);
		assertEquals(subComponentMap, cyclePriceCalculator.initialiseSubComponentMap(new HashMap<String, Integer>()));
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

	@SuppressWarnings("unchecked")
	@Test
	public void testFrameRateCalculator1() {
		String expected = "520";
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
		CyclePriceCalculator cyclePriceCalculator1 = new CyclePriceCalculator(cycle, blockingQueue);
		CyclePriceCalculator cyclePriceCalculatorSpy = Mockito.spy(cyclePriceCalculator1);
		Mockito.when(cyclePriceCalculatorSpy.findRate(anyMap(), anyString(), anyString(), anyMap(), anyInt()))
				.thenReturn(subComponentMap);
		String actual = cyclePriceCalculatorSpy.frameRateCalculator("diamond, steel", "2020", "jul", cycle);
		assertEquals(expected, actual);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testFrameRateCalculator2() {
		String expected = "500";
		Frame frame = new Frame();
		StepThrough[] stepThrough = new StepThrough[1];
		stepThrough[0] = new StepThrough();
		stepThrough[0].setYear(2020);
		stepThrough[0].setCostMap(setCostMap());
		frame.setStepThrough(stepThrough);
		Steel[] steel = new Steel[1];
		steel[0] = new Steel();
		steel[0].setYear(2020);
		steel[0].setCostMap(setCostMap());
		frame.setSteel(steel);
		cycle.setFrame(frame);
		Map<String, Integer> subComponentMap = new HashMap<>();
		subComponentMap.put("yearFlag", 1);
		subComponentMap.put("rate", 500);
		subComponentMap.put("currentIndex", 0);
		subComponentMap.put("year", 2020);
		CyclePriceCalculator cyclePriceCalculator1 = new CyclePriceCalculator(cycle, blockingQueue);
		CyclePriceCalculator cyclePriceCalculatorSpy = Mockito.spy(cyclePriceCalculator1);
		Mockito.when(cyclePriceCalculatorSpy.findRate(anyMap(), anyString(), anyString(), anyMap(), anyInt()))
				.thenReturn(subComponentMap);
		String actual = cyclePriceCalculatorSpy.frameRateCalculator("stepthrough, steel", "2020", "jul", cycle);
		assertEquals(expected, actual);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testFrameRateCalculator3() {
		String expected = "330";
		Frame frame = new Frame();
		Contilever[] contilever = new Contilever[1];
		contilever[0] = new Contilever();
		contilever[0].setYear(2020);
		contilever[0].setCostMap(setCostMap());
		frame.setContilever(contilever);
		Steel[] steel = new Steel[1];
		steel[0] = new Steel();
		steel[0].setYear(2020);
		steel[0].setCostMap(setCostMap());
		frame.setSteel(steel);
		cycle.setFrame(frame);
		Map<String, Integer> subComponentMap = new HashMap<>();
		subComponentMap.put("yearFlag", 1);
		subComponentMap.put("rate", 330);
		subComponentMap.put("currentIndex", 0);
		subComponentMap.put("year", 2020);
		CyclePriceCalculator cyclePriceCalculator1 = new CyclePriceCalculator(cycle, blockingQueue);
		CyclePriceCalculator cyclePriceCalculatorSpy = Mockito.spy(cyclePriceCalculator1);
		Mockito.when(cyclePriceCalculatorSpy.findRate(anyMap(), anyString(), anyString(), anyMap(), anyInt()))
				.thenReturn(subComponentMap);
		String actual = cyclePriceCalculatorSpy.frameRateCalculator("contilever, steel", "2020", "jul", cycle);
		assertEquals(expected, actual);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testFrameRateCalculator4() {
		String expected = "300";
		Frame frame = new Frame();
		Recumbent[] recumbent = new Recumbent[1];
		recumbent[0] = new Recumbent();
		recumbent[0].setYear(2020);
		recumbent[0].setCostMap(setCostMap());
		frame.setRecumbent(recumbent);
		Steel[] steel = new Steel[1];
		steel[0] = new Steel();
		steel[0].setYear(2020);
		steel[0].setCostMap(setCostMap());
		frame.setSteel(steel);
		cycle.setFrame(frame);
		Map<String, Integer> subComponentMap = new HashMap<>();
		subComponentMap.put("yearFlag", 1);
		subComponentMap.put("rate", 300);
		subComponentMap.put("currentIndex", 0);
		subComponentMap.put("year", 2020);
		CyclePriceCalculator cyclePriceCalculator1 = new CyclePriceCalculator(cycle, blockingQueue);
		CyclePriceCalculator cyclePriceCalculatorSpy = Mockito.spy(cyclePriceCalculator1);
		Mockito.when(cyclePriceCalculatorSpy.findRate(anyMap(), anyString(), anyString(), anyMap(), anyInt()))
				.thenReturn(subComponentMap);
		String actual = cyclePriceCalculatorSpy.frameRateCalculator("recumbent, steel", "2020", "jul", cycle);
		assertEquals(expected, actual);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testFrameRateCalculator5() {
		String expected = "900";
		Frame frame = new Frame();
		Contilever[] contilever = new Contilever[1];
		contilever[0] = new Contilever();
		contilever[0].setYear(2020);
		contilever[0].setCostMap(setCostMap());
		frame.setContilever(contilever);
		Titanium[] titanium = new Titanium[1];
		titanium[0] = new Titanium();
		titanium[0].setYear(2020);
		titanium[0].setCostMap(setCostMap());
		frame.setTitanium(titanium);
		cycle.setFrame(frame);
		Map<String, Integer> subComponentMap = new HashMap<>();
		subComponentMap.put("yearFlag", 1);
		subComponentMap.put("rate", 900);
		subComponentMap.put("currentIndex", 0);
		subComponentMap.put("year", 2020);
		CyclePriceCalculator cyclePriceCalculator1 = new CyclePriceCalculator(cycle, blockingQueue);
		CyclePriceCalculator cyclePriceCalculatorSpy = Mockito.spy(cyclePriceCalculator1);
		Mockito.when(cyclePriceCalculatorSpy.findRate(anyMap(), anyString(), anyString(), anyMap(), anyInt()))
				.thenReturn(subComponentMap);
		String actual = cyclePriceCalculatorSpy.frameRateCalculator("titanium, contilever", "2020", "jul", cycle);
		assertEquals(expected, actual);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testFrameRateCalculator6() {
		String expected = "900";
		Frame frame = new Frame();
		Aluminium[] aluminium = new Aluminium[1];
		aluminium[0] = new Aluminium();
		aluminium[0].setYear(2020);
		aluminium[0].setCostMap(setCostMap());
		frame.setAluminium(aluminium);
		Diamond[] diamond = new Diamond[1];
		diamond[0] = new Diamond();
		diamond[0].setYear(2020);
		diamond[0].setCostMap(setCostMap());
		frame.setDiamond(diamond);
		cycle.setFrame(frame);
		Map<String, Integer> subComponentMap = new HashMap<>();
		subComponentMap.put("yearFlag", 1);
		subComponentMap.put("rate", 900);
		subComponentMap.put("currentIndex", 0);
		subComponentMap.put("year", 2020);
		CyclePriceCalculator cyclePriceCalculator1 = new CyclePriceCalculator(cycle, blockingQueue);
		CyclePriceCalculator cyclePriceCalculatorSpy = Mockito.spy(cyclePriceCalculator1);
		Mockito.when(cyclePriceCalculatorSpy.findRate(anyMap(), anyString(), anyString(), anyMap(), anyInt()))
				.thenReturn(subComponentMap);
		String actual = cyclePriceCalculatorSpy.frameRateCalculator("aluminium, diamond", "2020", "jul", cycle);
		assertEquals(expected, actual);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testFrameRateCalculator7() {
		String expected = "550";
		Frame frame = new Frame();
		Recumbent[] recumbent = new Recumbent[1];
		recumbent[0] = new Recumbent();
		recumbent[0].setYear(2020);
		recumbent[0].setCostMap(setCostMap());
		frame.setRecumbent(recumbent);
		Steel[] steel = new Steel[1];
		steel[0] = new Steel();
		steel[0].setYear(2020);
		steel[0].setCostMap(setCostMap());
		frame.setSteel(steel);
		cycle.setFrame(frame);
		Map<String, Integer> subComponentMap = new HashMap<>();
		subComponentMap.put("yearFlag", 1);
		subComponentMap.put("rate", 550);
		subComponentMap.put("currentIndex", 0);
		subComponentMap.put("year", 2020);
		CyclePriceCalculator cyclePriceCalculator1 = new CyclePriceCalculator(cycle, blockingQueue);
		CyclePriceCalculator cyclePriceCalculatorSpy = Mockito.spy(cyclePriceCalculator1);
		Mockito.when(cyclePriceCalculatorSpy.findRate(anyMap(), anyString(), anyString(), anyMap(), anyInt()))
				.thenReturn(subComponentMap);
		String actual = cyclePriceCalculatorSpy.frameRateCalculator("steel, recumbent", "2020", "jul", cycle);
		assertEquals(expected, actual);
	}

	@Test
	public void testWheelsRateCalculator1() {
		String expected = "1650";
		Wheels wheels = new Wheels();
		Rim[] rim = new Rim[1];
		rim[0] = new Rim();
		rim[0].setYear(2020);
		rim[0].setCostMap(setCostMap());
		wheels.setRim(rim);
		Spokes[] spokes = new Spokes[1];
		spokes[0] = new Spokes();
		spokes[0].setYear(2020);
		spokes[0].setCostMap(setCostMap());
		wheels.setSpokes(spokes);
		Tube[] tube = new Tube[1];
		tube[0] = new Tube();
		tube[0].setYear(2020);
		tube[0].setCostMap(setCostMap());
		wheels.setTube(tube);
		Tyre[] tyre = new Tyre[1];
		tyre[0] = new Tyre();
		tyre[0].setYear(2020);
		tyre[0].setCostMap(setCostMap());
		wheels.setTyre(tyre);
		cycle.setWheels(wheels);
		Map<String, Integer> subComponentMap = new HashMap<>();
		subComponentMap.put("yearFlag", 1);
		subComponentMap.put("rate", 550);
		subComponentMap.put("currentIndex", 0);
		subComponentMap.put("year", 2020);
		CyclePriceCalculator cyclePriceCalculator1 = new CyclePriceCalculator(cycle, blockingQueue);
		CyclePriceCalculator cyclePriceCalculatorSpy = Mockito.spy(cyclePriceCalculator1);
		Mockito.when(cyclePriceCalculatorSpy.findRate(anyMap(), anyString(), anyString(), anyMap(), anyInt()))
				.thenReturn(subComponentMap);
		String actual = cyclePriceCalculatorSpy.wheelsRateCalculator("tubeless", "2020", "jul", cycle);
		assertEquals(expected, actual);
	}

	@Test
	public void testWheelsRateCalculator2() {
		String expected = "1500";
		Wheels wheels = new Wheels();
		Rim[] rim = new Rim[1];
		rim[0] = new Rim();
		rim[0].setYear(2020);
		rim[0].setCostMap(setCostMap());
		wheels.setRim(rim);
		Spokes[] spokes = new Spokes[1];
		spokes[0] = new Spokes();
		spokes[0].setYear(2020);
		spokes[0].setCostMap(setCostMap());
		wheels.setSpokes(spokes);
		Tube[] tube = new Tube[1];
		tube[0] = new Tube();
		tube[0].setYear(2020);
		tube[0].setCostMap(setCostMap());
		wheels.setTube(tube);
		Tyre[] tyre = new Tyre[1];
		tyre[0] = new Tyre();
		tyre[0].setYear(2020);
		tyre[0].setCostMap(setCostMap());
		wheels.setTyre(tyre);
		cycle.setWheels(wheels);
		Map<String, Integer> subComponentMap = new HashMap<>();
		subComponentMap.put("yearFlag", 1);
		subComponentMap.put("rate", 500);
		subComponentMap.put("currentIndex", 0);
		subComponentMap.put("year", 2020);
		CyclePriceCalculator cyclePriceCalculator1 = new CyclePriceCalculator(cycle, blockingQueue);
		CyclePriceCalculator cyclePriceCalculatorSpy = Mockito.spy(cyclePriceCalculator1);
		Mockito.when(cyclePriceCalculatorSpy.findRate(anyMap(), anyString(), anyString(), anyMap(), anyInt()))
				.thenReturn(subComponentMap);
		String actual = cyclePriceCalculatorSpy.wheelsRateCalculator("rimless", "2020", "jul", cycle);
		assertEquals(expected, actual);
	}

	@Test
	public void testWheelsRateCalculator3() {
		String expected = "1200";
		Wheels wheels = new Wheels();
		Rim[] rim = new Rim[1];
		rim[0] = new Rim();
		rim[0].setYear(2020);
		rim[0].setCostMap(setCostMap());
		wheels.setRim(rim);
		Spokes[] spokes = new Spokes[1];
		spokes[0] = new Spokes();
		spokes[0].setYear(2020);
		spokes[0].setCostMap(setCostMap());
		wheels.setSpokes(spokes);
		Tube[] tube = new Tube[1];
		tube[0] = new Tube();
		tube[0].setYear(2020);
		tube[0].setCostMap(setCostMap());
		wheels.setTube(tube);
		Tyre[] tyre = new Tyre[1];
		tyre[0] = new Tyre();
		tyre[0].setYear(2020);
		tyre[0].setCostMap(setCostMap());
		wheels.setTyre(tyre);
		cycle.setWheels(wheels);
		Map<String, Integer> subComponentMap = new HashMap<>();
		subComponentMap.put("yearFlag", 1);
		subComponentMap.put("rate", 400);
		subComponentMap.put("currentIndex", 0);
		subComponentMap.put("year", 2020);
		CyclePriceCalculator cyclePriceCalculator1 = new CyclePriceCalculator(cycle, blockingQueue);
		CyclePriceCalculator cyclePriceCalculatorSpy = Mockito.spy(cyclePriceCalculator1);
		Mockito.when(cyclePriceCalculatorSpy.findRate(anyMap(), anyString(), anyString(), anyMap(), anyInt()))
				.thenReturn(subComponentMap);
		String actual = cyclePriceCalculatorSpy.wheelsRateCalculator("spokeless", "2020", "jul", cycle);
		assertEquals(expected, actual);
	}

	@Test
	public void testChainAssemblyRateCalculator1() {
		String expected = "520";
		ChainAssembly chainAssembly = new ChainAssembly();
		FixedGear[] fixed = new FixedGear[1];
		fixed[0] = new FixedGear();
		fixed[0].setYear(2020);
		fixed[0].setCostMap(setCostMap());
		chainAssembly.setFixedGear(fixed);
		cycle.setChainAssembly(chainAssembly);
		Map<String, Integer> subComponentMap = new HashMap<>();
		subComponentMap.put("yearFlag", 1);
		subComponentMap.put("rate", 520);
		subComponentMap.put("currentIndex", 0);
		subComponentMap.put("year", 2020);
		CyclePriceCalculator cyclePriceCalculator1 = new CyclePriceCalculator(cycle, blockingQueue);
		CyclePriceCalculator cyclePriceCalculatorSpy = Mockito.spy(cyclePriceCalculator1);
		Mockito.when(cyclePriceCalculatorSpy.findRate(anyMap(), anyString(), anyString(), anyMap(), anyInt()))
				.thenReturn(subComponentMap);
		String actual = cyclePriceCalculatorSpy.chainAssemblyRateCalculator("fixed", "2020", "jul", cycle);
		assertEquals(expected, actual);
	}

	@Test
	public void testChainAssemblyRateCalculator2() {
		String expected = "600";
		ChainAssembly chainAssembly = new ChainAssembly();
		Gear3[] gear3 = new Gear3[1];
		gear3[0] = new Gear3();
		gear3[0].setYear(2020);
		gear3[0].setCostMap(setCostMap());
		chainAssembly.setGear3(gear3);
		cycle.setChainAssembly(chainAssembly);
		Map<String, Integer> subComponentMap = new HashMap<>();
		subComponentMap.put("yearFlag", 1);
		subComponentMap.put("rate", 600);
		subComponentMap.put("currentIndex", 0);
		subComponentMap.put("year", 2020);
		CyclePriceCalculator cyclePriceCalculator1 = new CyclePriceCalculator(cycle, blockingQueue);
		CyclePriceCalculator cyclePriceCalculatorSpy = Mockito.spy(cyclePriceCalculator1);
		Mockito.when(cyclePriceCalculatorSpy.findRate(anyMap(), anyString(), anyString(), anyMap(), anyInt()))
				.thenReturn(subComponentMap);
		String actual = cyclePriceCalculatorSpy.chainAssemblyRateCalculator("gear3", "2020", "jul", cycle);
		assertEquals(expected, actual);
	}

	@Test
	public void testChainAssemblyRateCalculator3() {
		String expected = "650";
		ChainAssembly chainAssembly = new ChainAssembly();
		Gear4[] gear4 = new Gear4[1];
		gear4[0] = new Gear4();
		gear4[0].setYear(2020);
		gear4[0].setCostMap(setCostMap());
		chainAssembly.setGear4(gear4);
		cycle.setChainAssembly(chainAssembly);
		Map<String, Integer> subComponentMap = new HashMap<>();
		subComponentMap.put("yearFlag", 1);
		subComponentMap.put("rate", 650);
		subComponentMap.put("currentIndex", 0);
		subComponentMap.put("year", 2020);
		CyclePriceCalculator cyclePriceCalculator1 = new CyclePriceCalculator(cycle, blockingQueue);
		CyclePriceCalculator cyclePriceCalculatorSpy = Mockito.spy(cyclePriceCalculator1);
		Mockito.when(cyclePriceCalculatorSpy.findRate(anyMap(), anyString(), anyString(), anyMap(), anyInt()))
				.thenReturn(subComponentMap);
		String actual = cyclePriceCalculatorSpy.chainAssemblyRateCalculator("gear4", "2020", "jul", cycle);
		assertEquals(expected, actual);
	}

	@Test
	public void testChainAssemblyRateCalculator4() {
		String expected = "670";
		ChainAssembly chainAssembly = new ChainAssembly();
		Gear5[] gear5 = new Gear5[1];
		gear5[0] = new Gear5();
		gear5[0].setYear(2020);
		gear5[0].setCostMap(setCostMap());
		chainAssembly.setGear5(gear5);
		cycle.setChainAssembly(chainAssembly);
		Map<String, Integer> subComponentMap = new HashMap<>();
		subComponentMap.put("yearFlag", 1);
		subComponentMap.put("rate", 670);
		subComponentMap.put("currentIndex", 0);
		subComponentMap.put("year", 2020);
		CyclePriceCalculator cyclePriceCalculator1 = new CyclePriceCalculator(cycle, blockingQueue);
		CyclePriceCalculator cyclePriceCalculatorSpy = Mockito.spy(cyclePriceCalculator1);
		Mockito.when(cyclePriceCalculatorSpy.findRate(anyMap(), anyString(), anyString(), anyMap(), anyInt()))
				.thenReturn(subComponentMap);
		String actual = cyclePriceCalculatorSpy.chainAssemblyRateCalculator("gear5", "2020", "jul", cycle);
		assertEquals(expected, actual);
	}

	@Test
	public void testChainAssemblyRateCalculator5() {
		String expected = "680";
		ChainAssembly chainAssembly = new ChainAssembly();
		Gear6[] gear6 = new Gear6[1];
		gear6[0] = new Gear6();
		gear6[0].setYear(2020);
		gear6[0].setCostMap(setCostMap());
		chainAssembly.setGear6(gear6);
		cycle.setChainAssembly(chainAssembly);
		Map<String, Integer> subComponentMap = new HashMap<>();
		subComponentMap.put("yearFlag", 1);
		subComponentMap.put("rate", 680);
		subComponentMap.put("currentIndex", 0);
		subComponentMap.put("year", 2020);
		CyclePriceCalculator cyclePriceCalculator1 = new CyclePriceCalculator(cycle, blockingQueue);
		CyclePriceCalculator cyclePriceCalculatorSpy = Mockito.spy(cyclePriceCalculator1);
		Mockito.when(cyclePriceCalculatorSpy.findRate(anyMap(), anyString(), anyString(), anyMap(), anyInt()))
				.thenReturn(subComponentMap);
		String actual = cyclePriceCalculatorSpy.chainAssemblyRateCalculator("gear6", "2020", "jul", cycle);
		assertEquals(expected, actual);
	}

	@Test
	public void testChainAssemblyRateCalculator6() {
		String expected = "680";
		ChainAssembly chainAssembly = new ChainAssembly();
		Gear7[] gear7 = new Gear7[1];
		gear7[0] = new Gear7();
		gear7[0].setYear(2020);
		gear7[0].setCostMap(setCostMap());
		chainAssembly.setGear7(gear7);
		cycle.setChainAssembly(chainAssembly);
		Map<String, Integer> subComponentMap = new HashMap<>();
		subComponentMap.put("yearFlag", 1);
		subComponentMap.put("rate", 680);
		subComponentMap.put("currentIndex", 0);
		subComponentMap.put("year", 2020);
		CyclePriceCalculator cyclePriceCalculator1 = new CyclePriceCalculator(cycle, blockingQueue);
		CyclePriceCalculator cyclePriceCalculatorSpy = Mockito.spy(cyclePriceCalculator1);
		Mockito.when(cyclePriceCalculatorSpy.findRate(anyMap(), anyString(), anyString(), anyMap(), anyInt()))
				.thenReturn(subComponentMap);
		String actual = cyclePriceCalculatorSpy.chainAssemblyRateCalculator("gear7", "2020", "jul", cycle);
		assertEquals(expected, actual);
	}

	@Test
	public void testChainAssemblyRateCalculator7() {
		String expected = "680";
		ChainAssembly chainAssembly = new ChainAssembly();
		Gear8[] gear8 = new Gear8[1];
		gear8[0] = new Gear8();
		gear8[0].setYear(2020);
		gear8[0].setCostMap(setCostMap());
		chainAssembly.setGear8(gear8);
		cycle.setChainAssembly(chainAssembly);
		Map<String, Integer> subComponentMap = new HashMap<>();
		subComponentMap.put("yearFlag", 1);
		subComponentMap.put("rate", 680);
		subComponentMap.put("currentIndex", 0);
		subComponentMap.put("year", 2020);
		CyclePriceCalculator cyclePriceCalculator1 = new CyclePriceCalculator(cycle, blockingQueue);
		CyclePriceCalculator cyclePriceCalculatorSpy = Mockito.spy(cyclePriceCalculator1);
		Mockito.when(cyclePriceCalculatorSpy.findRate(anyMap(), anyString(), anyString(), anyMap(), anyInt()))
				.thenReturn(subComponentMap);
		String actual = cyclePriceCalculatorSpy.chainAssemblyRateCalculator("gear8", "2020", "jul", cycle);
		assertEquals(expected, actual);
	}

	@Test
	public void testSeatingRateCalculator1() {
		String expected = "560";
		Seating seating = new Seating();
		CruiserSaddle[] cruiser = new CruiserSaddle[1];
		cruiser[0] = new CruiserSaddle();
		cruiser[0].setYear(2020);
		cruiser[0].setCostMap(setCostMap());
		seating.setCruiserSaddle(cruiser);
		cycle.setSeating(seating);
		Map<String, Integer> subComponentMap = new HashMap<>();
		subComponentMap.put("yearFlag", 1);
		subComponentMap.put("rate", 560);
		subComponentMap.put("currentIndex", 0);
		subComponentMap.put("year", 2020);
		CyclePriceCalculator cyclePriceCalculator1 = new CyclePriceCalculator(cycle, blockingQueue);
		CyclePriceCalculator cyclePriceCalculatorSpy = Mockito.spy(cyclePriceCalculator1);
		Mockito.when(cyclePriceCalculatorSpy.findRate(anyMap(), anyString(), anyString(), anyMap(), anyInt()))
				.thenReturn(subComponentMap);
		String actual = cyclePriceCalculatorSpy.seatingRateCalculator("cruiser", "2020", "jul", cycle);
		assertEquals(expected, actual);
	}

	@Test
	public void testSeatingRateCalculator2() {
		String expected = "590";
		Seating seating = new Seating();
		RacingSaddle[] racing = new RacingSaddle[1];
		racing[0] = new RacingSaddle();
		racing[0].setYear(2020);
		racing[0].setCostMap(setCostMap());
		seating.setRacingSaddle(racing);
		cycle.setSeating(seating);
		Map<String, Integer> subComponentMap = new HashMap<>();
		subComponentMap.put("yearFlag", 1);
		subComponentMap.put("rate", 590);
		subComponentMap.put("currentIndex", 0);
		subComponentMap.put("year", 2020);
		CyclePriceCalculator cyclePriceCalculator1 = new CyclePriceCalculator(cycle, blockingQueue);
		CyclePriceCalculator cyclePriceCalculatorSpy = Mockito.spy(cyclePriceCalculator1);
		Mockito.when(cyclePriceCalculatorSpy.findRate(anyMap(), anyString(), anyString(), anyMap(), anyInt()))
				.thenReturn(subComponentMap);
		String actual = cyclePriceCalculatorSpy.seatingRateCalculator("racing", "2020", "jul", cycle);
		assertEquals(expected, actual);
	}

	@Test
	public void testSeatingRateCalculator3() {
		String expected = "650";
		Seating seating = new Seating();
		ComfortSaddle[] comfort = new ComfortSaddle[1];
		comfort[0] = new ComfortSaddle();
		comfort[0].setYear(2020);
		comfort[0].setCostMap(setCostMap());
		seating.setComfortSaddle(comfort);
		cycle.setSeating(seating);
		Map<String, Integer> subComponentMap = new HashMap<>();
		subComponentMap.put("yearFlag", 1);
		subComponentMap.put("rate", 650);
		subComponentMap.put("currentIndex", 0);
		subComponentMap.put("year", 2020);
		CyclePriceCalculator cyclePriceCalculator1 = new CyclePriceCalculator(cycle, blockingQueue);
		CyclePriceCalculator cyclePriceCalculatorSpy = Mockito.spy(cyclePriceCalculator1);
		Mockito.when(cyclePriceCalculatorSpy.findRate(anyMap(), anyString(), anyString(), anyMap(), anyInt()))
				.thenReturn(subComponentMap);
		String actual = cyclePriceCalculatorSpy.seatingRateCalculator("comfort", "2020", "jul", cycle);
		assertEquals(expected, actual);
	}

	@Test
	public void testHandlBarRateCalculator1() {
		String expected = "700";
		HandleBar handleBar = new HandleBar();
		DropBar[] dropBar = new DropBar[1];
		dropBar[0] = new DropBar();
		dropBar[0].setYear(2020);
		dropBar[0].setCostMap(setCostMap());
		handleBar.setDropBar(dropBar);
		cycle.setHandleBar(handleBar);
		Map<String, Integer> subComponentMap = new HashMap<>();
		subComponentMap.put("yearFlag", 1);
		subComponentMap.put("rate", 700);
		subComponentMap.put("currentIndex", 0);
		subComponentMap.put("year", 2020);
		CyclePriceCalculator cyclePriceCalculator1 = new CyclePriceCalculator(cycle, blockingQueue);
		CyclePriceCalculator cyclePriceCalculatorSpy = Mockito.spy(cyclePriceCalculator1);
		Mockito.when(cyclePriceCalculatorSpy.findRate(anyMap(), anyString(), anyString(), anyMap(), anyInt()))
				.thenReturn(subComponentMap);
		String actual = cyclePriceCalculatorSpy.handleBarRateCalculator("drop", "2020", "jul", cycle);
		assertEquals(expected, actual);
	}

	@Test
	public void testHandlBarRateCalculator2() {
		String expected = "700";
		HandleBar handleBar = new HandleBar();
		PursuitBar[] pursuitBar = new PursuitBar[1];
		pursuitBar[0] = new PursuitBar();
		pursuitBar[0].setYear(2020);
		pursuitBar[0].setCostMap(setCostMap());
		handleBar.setPursuitBar(pursuitBar);
		cycle.setHandleBar(handleBar);
		Map<String, Integer> subComponentMap = new HashMap<>();
		subComponentMap.put("yearFlag", 1);
		subComponentMap.put("rate", 700);
		subComponentMap.put("currentIndex", 0);
		subComponentMap.put("year", 2020);
		CyclePriceCalculator cyclePriceCalculator1 = new CyclePriceCalculator(cycle, blockingQueue);
		CyclePriceCalculator cyclePriceCalculatorSpy = Mockito.spy(cyclePriceCalculator1);
		Mockito.when(cyclePriceCalculatorSpy.findRate(anyMap(), anyString(), anyString(), anyMap(), anyInt()))
				.thenReturn(subComponentMap);
		String actual = cyclePriceCalculatorSpy.handleBarRateCalculator("pursuit", "2020", "jul", cycle);
		assertEquals(expected, actual);
	}

	@Test
	public void testHandlBarRateCalculator3() {
		String expected = "700";
		HandleBar handleBar = new HandleBar();
		RiserBar[] riserBar = new RiserBar[1];
		riserBar[0] = new RiserBar();
		riserBar[0].setYear(2020);
		riserBar[0].setCostMap(setCostMap());
		handleBar.setRiserBar(riserBar);
		cycle.setHandleBar(handleBar);
		Map<String, Integer> subComponentMap = new HashMap<>();
		subComponentMap.put("yearFlag", 1);
		subComponentMap.put("rate", 700);
		subComponentMap.put("currentIndex", 0);
		subComponentMap.put("year", 2020);
		CyclePriceCalculator cyclePriceCalculator1 = new CyclePriceCalculator(cycle, blockingQueue);
		CyclePriceCalculator cyclePriceCalculatorSpy = Mockito.spy(cyclePriceCalculator1);
		Mockito.when(cyclePriceCalculatorSpy.findRate(anyMap(), anyString(), anyString(), anyMap(), anyInt()))
				.thenReturn(subComponentMap);
		String actual = cyclePriceCalculatorSpy.handleBarRateCalculator("riser", "2020", "jul", cycle);
		assertEquals(expected, actual);
	}

	@Test
	public void testCyclePriceCalculator() {
		String expected = "5000";
		Map<String, String> cyclePriceMap = new HashMap<>();
		cyclePriceMap.put("frame", "1000");
		cyclePriceMap.put("wheels", "1000");
		cyclePriceMap.put("seating", "1000");
		cyclePriceMap.put("chainAssembly", "1000");
		cyclePriceMap.put("handleBar", "1000");
		String actual = cyclePriceCalculator.cyclePriceCalculator(cyclePriceMap);
		assertEquals(expected, actual);
	}

}