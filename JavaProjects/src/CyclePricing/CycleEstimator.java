package CyclePricing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

import Model.Cycle;
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
import Model.Components.Frame.Recumbent;
import Model.Components.Frame.Steel;
import Model.Components.Frame.StepThrough;
import Model.Components.Frame.Titanium;
import Model.Components.HandleBar.DropBar;
import Model.Components.HandleBar.PursuitBar;
import Model.Components.HandleBar.RiserBar;
import Model.Components.Seating.ComfortSaddle;
import Model.Components.Seating.CruiserSaddle;
import Model.Components.Seating.RacingSaddle;
import Model.Components.Wheels.Rim;
import Model.Components.Wheels.Spokes;
import Model.Components.Wheels.Tube;
import Model.Components.Wheels.Tyre;

class CyclePriceCalculator extends Thread{
	
	Cycle cycle;
	BlockingQueue<Map<String, Object>> blockingQueue;
	
	private final String CYCLE = "cycle";
	private final String NAME = "name";
	private final String DATE_OF_PRICING = "date_of_pricing";
	private final String FRAME = "frame";
	private final String WHEELS = "wheels";
	private final String CHAIN_ASSEMBLY = "chainAssembly";
	private final String SEATING = "seating";
	private final String HANDLE_BAR = "handleBar";
	private final String YEAR = "year";
	private final String MONTH = "month";
	private final String YEAR_FLAG = "yearFlag";
	private final String CURRENT_INDEX = "currentIndex";
	private final String RATE = "rate";
	
	
	CyclePriceCalculator(Cycle cycle, BlockingQueue<Map<String, Object>> blockingQueue){
		this.cycle = cycle;
		this.blockingQueue = blockingQueue;
	}

	String getCurrentYear() {
		Calendar now = Calendar.getInstance();
		return String.valueOf(now.get(Calendar.YEAR));
	}

	String getCurrentMonth() {
		Format format = new SimpleDateFormat("MMM");
		String month = format.format(new Date());
		return month.toLowerCase();
	}

	Map<String, Object> cycleBuilder(Map<String, Object> cycleMap) {
		if (!cycleMap.containsKey(NAME))
			cycleMap.put(NAME, CYCLE);
		if (!cycleMap.containsKey(DATE_OF_PRICING)) {
			Map<String, String> dateMap = new HashMap<>();
			dateMap.put(YEAR, getCurrentYear());
			dateMap.put(MONTH, getCurrentMonth());
			cycleMap.put(DATE_OF_PRICING, dateMap);
		}
		if (!cycleMap.containsKey(FRAME))
			cycleMap.put(FRAME, "diamond, steel");
		if (!cycleMap.containsKey(WHEELS))
			cycleMap.put(WHEELS, "");
		if (!cycleMap.containsKey(CHAIN_ASSEMBLY))
			cycleMap.put(CHAIN_ASSEMBLY, "fixedGear");
		if (!cycleMap.containsKey(SEATING))
			cycleMap.put(SEATING, "comfort");
		if (!cycleMap.containsKey(HANDLE_BAR))
			cycleMap.put(HANDLE_BAR, "pursuit");
		return cycleMap;
	}
	
	Map<String, Integer> initialiseSubComponentMap(Map<String, Integer> subComponentMap) {
		subComponentMap.put(RATE, 0);
		subComponentMap.put(YEAR_FLAG, 0);
		subComponentMap.put(CURRENT_INDEX, 0);
		return subComponentMap;
	}

	Map<String, Integer> findRate(Map<String, Integer> subComponentMap, String year, String month, Map<String, Integer> costMap, int index) {
		if (String.valueOf(subComponentMap.get(YEAR)).equals(year)) {
			subComponentMap.put(YEAR_FLAG, 1);
			subComponentMap.put(RATE, costMap.get(month));
		}
		if (String.valueOf(subComponentMap.get(YEAR)).equals(getCurrentYear()))
			subComponentMap.put(CURRENT_INDEX, index);
		return subComponentMap;
	}
	
	String frameRateCalculator(String subComponent, String year, String month, Cycle cycle) {
		int materialrate = 0, frameTypeRate = 0;
		Map<String, Integer> subComponentMap = new HashMap<>();
		subComponentMap = initialiseSubComponentMap(subComponentMap);
		if(subComponent.contains("step")) {
			StepThrough[] stepThrough = cycle.getFrame().getStepThrough();
			for(int i = 0; i < stepThrough.length; i++) {
				subComponentMap.put(YEAR, stepThrough[i].getYear());
				subComponentMap = findRate(subComponentMap, year, month, stepThrough[i].getCostMap(), i);
				frameTypeRate = subComponentMap.get(RATE);
			}
			if (subComponentMap.get(YEAR_FLAG) == 0) 
				frameTypeRate = stepThrough[subComponentMap.get(CURRENT_INDEX)].getCostMap().get(getCurrentMonth());
		}else if(subComponent.contains("contilever")) {
			Contilever[] contilever = cycle.getFrame().getContilever();
			for(int i = 0; i < contilever.length; i++) {
				subComponentMap.put(YEAR, contilever[i].getYear());
				subComponentMap = findRate(subComponentMap, year, month, contilever[i].getCostMap(), i);
				frameTypeRate = subComponentMap.get(RATE);
			}
			if (subComponentMap.get(YEAR_FLAG) == 0) 
				frameTypeRate = contilever[subComponentMap.get(CURRENT_INDEX)].getCostMap().get(getCurrentMonth());
		}else if(subComponent.contains("recumbent")) {
			Recumbent[] recumbent = cycle.getFrame().getRecumbent();
			for(int i = 0; i < recumbent.length; i++) {
				subComponentMap.put(YEAR, recumbent[i].getYear());
				subComponentMap = findRate(subComponentMap, year, month, recumbent[i].getCostMap(), i);
				frameTypeRate = subComponentMap.get(RATE);
			}
			if (subComponentMap.get(YEAR_FLAG) == 0) 
				frameTypeRate = recumbent[subComponentMap.get(CURRENT_INDEX)].getCostMap().get(getCurrentMonth());
		}else {
			Diamond[] diamond = cycle.getFrame().getDiamond();
			for(int i = 0; i < diamond.length; i++) {
				subComponentMap.put(YEAR, diamond[i].getYear());
				subComponentMap = findRate(subComponentMap, year, month, diamond[i].getCostMap(), i);
				frameTypeRate = subComponentMap.get(RATE);
			}
			if (subComponentMap.get(YEAR_FLAG) == 0) 
				frameTypeRate = diamond[subComponentMap.get(CURRENT_INDEX)].getCostMap().get(getCurrentMonth());
		}
		subComponentMap.put(YEAR_FLAG, 0);
		if(subComponent.contains("aluminium")) {
			Aluminium[] aluminium = cycle.getFrame().getAluminium();
			for(int i = 0; i < aluminium.length; i++) {
				subComponentMap.put(YEAR, aluminium[i].getYear());
				subComponentMap = findRate(subComponentMap, year, month, aluminium[i].getCostMap(), i);
				materialrate = subComponentMap.get(RATE);
			}
			if (subComponentMap.get(YEAR_FLAG) == 0) 
				materialrate = aluminium[subComponentMap.get(CURRENT_INDEX)].getCostMap().get(getCurrentMonth());
		}else if(subComponent.contains("titanium")) {
			Titanium[] titanium = cycle.getFrame().getTitanium();
			for(int i = 0; i < titanium.length; i++) {
				subComponentMap.put(YEAR, titanium[i].getYear());
				subComponentMap = findRate(subComponentMap, year, month, titanium[i].getCostMap(), i);
				materialrate = subComponentMap.get(RATE);
			}
			if (subComponentMap.get(YEAR_FLAG) == 0) 
				materialrate = titanium[subComponentMap.get(CURRENT_INDEX)].getCostMap().get(getCurrentMonth());
		}else{
			Steel[] steel = cycle.getFrame().getSteel();
			for(int i = 0; i < steel.length; i++) {
				subComponentMap.put(YEAR, steel[i].getYear());
				subComponentMap = findRate(subComponentMap, year, month, steel[i].getCostMap(), i);
				materialrate = subComponentMap.get(RATE);
			}
			if (subComponentMap.get(YEAR_FLAG) == 0) 
				materialrate = steel[subComponentMap.get(CURRENT_INDEX)].getCostMap().get(getCurrentMonth());
		}
		return String.valueOf(materialrate + frameTypeRate);
	}

	String wheelsRateCalculator(String subComponent, String year, String month, Cycle cycle) {
		Map<String, Integer> subComponentMap = new HashMap<>();
		subComponentMap = initialiseSubComponentMap(subComponentMap);
		int rimRate = 0, spokeRate = 0, tubeRate = 0, tyreRate = 0, rate = 0;
		Rim[] rim = cycle.getWheels().getRim();
		for(int i=0; i<rim.length; i++) {
			subComponentMap.put(YEAR, rim[i].getYear());
			subComponentMap = findRate(subComponentMap, year, month, rim[i].getCostMap(), i);
			rimRate = subComponentMap.get(RATE);
		}
		if (subComponentMap.get(YEAR_FLAG) == 0) 
			rimRate = rim[subComponentMap.get(CURRENT_INDEX)].getCostMap().get(getCurrentMonth());
		Spokes[] spokes = cycle.getWheels().getSpokes();
		for(int i=0; i<spokes.length; i++) {
			subComponentMap.put(YEAR, spokes[i].getYear());
			subComponentMap = findRate(subComponentMap, year, month, spokes[i].getCostMap(), i);
			spokeRate = subComponentMap.get(RATE);
		}
		if (subComponentMap.get(YEAR_FLAG) == 0) 
			spokeRate = spokes[subComponentMap.get(CURRENT_INDEX)].getCostMap().get(getCurrentMonth());
		Tube[] tube = cycle.getWheels().getTube();
		for(int i=0; i<tube.length; i++) {
			subComponentMap.put(YEAR, tube[i].getYear());
			subComponentMap = findRate(subComponentMap, year, month, tube[i].getCostMap(), i);
			tubeRate = subComponentMap.get(RATE);
		}
		if (subComponentMap.get(YEAR_FLAG) == 0) 
			tubeRate = tube[subComponentMap.get(CURRENT_INDEX)].getCostMap().get(getCurrentMonth());
		Tyre[] tyre =  cycle.getWheels().getTyre();
		for(int i=0; i<tyre.length; i++) {
			subComponentMap.put(YEAR, tyre[i].getYear());
			subComponentMap = findRate(subComponentMap, year, month, tyre[i].getCostMap(), i);
			tyreRate = subComponentMap.get(RATE);
		}
		if (subComponentMap.get(YEAR_FLAG) == 0) 
			tyreRate = tyre[subComponentMap.get(CURRENT_INDEX)].getCostMap().get(getCurrentMonth());
		rate = rimRate + spokeRate + tubeRate + tyreRate;
		if(subComponent.contains("tubeless")) 
			rate = rate - tubeRate;
		if(subComponent.contains("rimless"))
			rate = rate - rimRate;
		if(subComponent.contains("spokeless"))
			rate = rate - spokeRate;
		return String.valueOf(rate);
	}

	String chainAssemblyRateCalculator(String subComponent, String year, String month, Cycle cycle) {
		Map<String, Integer> subComponentMap = new HashMap<>();
		subComponentMap = initialiseSubComponentMap(subComponentMap);
		if (subComponent.contains("3")) {
			Gear3[] gear3 = cycle.getChainAssembly().getGear3();
			for(int i = 0; i < gear3.length; i++) {
				subComponentMap.put(YEAR, gear3[i].getYear());
				subComponentMap = findRate(subComponentMap, year, month, gear3[i].getCostMap(), i);
			}
			if (subComponentMap.get(YEAR_FLAG) == 0)
				subComponentMap.put(RATE, gear3[subComponentMap.get(CURRENT_INDEX)].getCostMap().get(getCurrentMonth()));
		} else if (subComponent.contains("4")) {
			Gear4[] gear4 = cycle.getChainAssembly().getGear4();
			for(int i = 0; i < gear4.length; i++) {
				subComponentMap.put(YEAR, gear4[i].getYear());
				subComponentMap = findRate(subComponentMap, year, month, gear4[i].getCostMap(), i);
			}
			if (subComponentMap.get(YEAR_FLAG) == 0)
				subComponentMap.put(RATE, gear4[subComponentMap.get(CURRENT_INDEX)].getCostMap().get(getCurrentMonth()));
		} else if (subComponent.contains("5")) {	
			Gear5[] gear5 = cycle.getChainAssembly().getGear5();
			for(int i = 0; i < gear5.length; i++) {
				subComponentMap.put(YEAR, gear5[i].getYear());
				subComponentMap = findRate(subComponentMap, year, month, gear5[i].getCostMap(), i);
			}
			if (subComponentMap.get(YEAR_FLAG) == 0)
				subComponentMap.put(RATE, gear5[subComponentMap.get(CURRENT_INDEX)].getCostMap().get(getCurrentMonth()));
		} else if (subComponent.contains("6")) {
			Gear6[] gear6 = cycle.getChainAssembly().getGear6();
			for(int i = 0; i < gear6.length; i++) {
				subComponentMap.put(YEAR, gear6[i].getYear());
				subComponentMap = findRate(subComponentMap, year, month, gear6[i].getCostMap(), i);
			}
			if (subComponentMap.get(YEAR_FLAG) == 0)
				subComponentMap.put(RATE, gear6[subComponentMap.get(CURRENT_INDEX)].getCostMap().get(getCurrentMonth()));
		} else if (subComponent.contains("7")) {
			Gear7[] gear7 = cycle.getChainAssembly().getGear7();
			for(int i = 0; i < gear7.length; i++) {
				subComponentMap.put(YEAR, gear7[i].getYear());
				subComponentMap = findRate(subComponentMap, year, month, gear7[i].getCostMap(), i);
			}
			if (subComponentMap.get(YEAR_FLAG) == 0)
				subComponentMap.put(RATE, gear7[subComponentMap.get(CURRENT_INDEX)].getCostMap().get(getCurrentMonth()));
		} else if (subComponent.contains("8")) {
			Gear8[] gear8 = cycle.getChainAssembly().getGear8();
			for(int i = 0; i < gear8.length; i++) {
				subComponentMap.put(YEAR, gear8[i].getYear());
				subComponentMap = findRate(subComponentMap, year, month, gear8[i].getCostMap(), i);
			}
			if (subComponentMap.get(YEAR_FLAG) == 0)
				subComponentMap.put(RATE, gear8[subComponentMap.get(CURRENT_INDEX)].getCostMap().get(getCurrentMonth()));
		} else {
			FixedGear[] fixedGear = cycle.getChainAssembly().getFixedGear();
			for(int i = 0; i < fixedGear.length; i++) {
				subComponentMap.put(YEAR, fixedGear[i].getYear());
				subComponentMap = findRate(subComponentMap, year, month, fixedGear[i].getCostMap(), i);
			}
			if (subComponentMap.get(YEAR_FLAG) == 0)
				subComponentMap.put(RATE, fixedGear[subComponentMap.get(CURRENT_INDEX)].getCostMap().get(getCurrentMonth()));
		}
		return String.valueOf(subComponentMap.get(RATE));
	}

	String seatingRateCalculator(String subComponent, String year, String month, Cycle cycle) {
		Map<String, Integer> subComponentMap = new HashMap<>();
		subComponentMap = initialiseSubComponentMap(subComponentMap);
		if (subComponent.contains("cruiser")) {
			CruiserSaddle[] cruiserSaddle = cycle.getSeating().getCruiserSaddle();
			for (int i = 0; i < cruiserSaddle.length; i++) {
				subComponentMap.put(YEAR, cruiserSaddle[i].getYear());
				subComponentMap = findRate(subComponentMap, year, month, cruiserSaddle[i].getCostMap(), i);
			}
			if (subComponentMap.get(YEAR_FLAG) == 0) 
				subComponentMap.put(RATE, cruiserSaddle[subComponentMap.get(CURRENT_INDEX)].getCostMap().get(getCurrentMonth()));
		} else if (subComponent.contains("racing")) {
			RacingSaddle[] racingSaddle = cycle.getSeating().getRacingSaddle();
			for (int i = 0; i < racingSaddle.length; i++) {
				subComponentMap.put(YEAR, racingSaddle[i].getYear());
				subComponentMap = findRate(subComponentMap, year, month, racingSaddle[i].getCostMap(), i);
			}
			if (subComponentMap.get(YEAR_FLAG) == 0) 
				subComponentMap.put(RATE, racingSaddle[subComponentMap.get(CURRENT_INDEX)].getCostMap().get(getCurrentMonth()));
		} else {
			ComfortSaddle[] comfortSaddle = cycle.getSeating().getComfortSaddle();
			for (int i = 0; i < comfortSaddle.length; i++) {
				subComponentMap.put(YEAR, comfortSaddle[i].getYear());
				subComponentMap = findRate(subComponentMap, year, month, comfortSaddle[i].getCostMap(), i);
			}
			if (subComponentMap.get(YEAR_FLAG) == 0) 
				subComponentMap.put(RATE, comfortSaddle[subComponentMap.get(CURRENT_INDEX)].getCostMap().get(getCurrentMonth()));
		}
		return String.valueOf(subComponentMap.get(RATE));
	}

	String handleBarRateCalculator(String subComponent, String year, String month, Cycle cycle) {
		Map<String, Integer> subComponentMap = new HashMap<>();
		subComponentMap = initialiseSubComponentMap(subComponentMap);
		if (subComponent.equals("drop")) {
			DropBar[] dropBar = cycle.getHandleBar().getDropBar();
			for (int i = 0; i < dropBar.length; i++) {
				subComponentMap.put(YEAR, dropBar[i].getYear());
				subComponentMap = findRate(subComponentMap, year, month, dropBar[i].getCostMap(), i);
			}
			if (subComponentMap.get(YEAR_FLAG) == 0)
				subComponentMap.put(RATE, dropBar[subComponentMap.get(CURRENT_INDEX)].getCostMap().get(getCurrentMonth()));
		} else if (subComponent.equals("riser")) {
			RiserBar[] riserBar = cycle.getHandleBar().getRiserBar();
			for (int i = 0; i < riserBar.length; i++) {
				subComponentMap.put(YEAR, riserBar[i].getYear());
				subComponentMap = findRate(subComponentMap, year, month, riserBar[i].getCostMap(), i);
			}
			if (subComponentMap.get(YEAR_FLAG) == 0)
				subComponentMap.put(RATE, riserBar[subComponentMap.get(CURRENT_INDEX)].getCostMap().get(getCurrentMonth()));
		} else {
			PursuitBar[] pursuitBar = cycle.getHandleBar().getPursuitBar();
			for (int i = 0; i < pursuitBar.length; i++) {
				subComponentMap.put(YEAR, pursuitBar[i].getYear());
				subComponentMap = findRate(subComponentMap, year, month, pursuitBar[i].getCostMap(), i);
			}
			if (subComponentMap.get(YEAR_FLAG) == 0)
				subComponentMap.put(RATE, pursuitBar[subComponentMap.get(CURRENT_INDEX)].getCostMap().get(getCurrentMonth()));
		}
		return String.valueOf(subComponentMap.get(RATE));
	}

	String cyclePriceCalculator(Map<String, String> cyclePriceMap) {
		int frameRate = Integer.parseInt(cyclePriceMap.get(FRAME));
		int wheelsRate = Integer.parseInt(cyclePriceMap.get(WHEELS));
		int chainAssemblyRate = Integer.parseInt(cyclePriceMap.get(CHAIN_ASSEMBLY));
		int seatingRate = Integer.parseInt(cyclePriceMap.get(SEATING));
		int handleBarRate = Integer.parseInt(cyclePriceMap.get(HANDLE_BAR));
		int rate = frameRate + wheelsRate + chainAssemblyRate + seatingRate + handleBarRate;
		return String.valueOf(rate);
	}

	@SuppressWarnings("unchecked")
	Map<String, String> priceEngine(Map<String, Object> cycleMap, Cycle cycle) {
		Map<String, String> dateMap = (Map<String, String>) cycleMap.get(DATE_OF_PRICING);
		String year = String.valueOf(dateMap.get(YEAR));
		String month = String.valueOf(dateMap.get(MONTH));
		Map<String, String> cyclePriceMap = new HashMap<>();
		cyclePriceMap.put(NAME, cycleMap.get(NAME).toString());
		cyclePriceMap.put(FRAME, frameRateCalculator(cycleMap.get(FRAME).toString(), year, month, cycle));
		cyclePriceMap.put(WHEELS, wheelsRateCalculator(cycleMap.get(WHEELS).toString(), year, month, cycle));
		cyclePriceMap.put(CHAIN_ASSEMBLY,
				chainAssemblyRateCalculator(cycleMap.get(CHAIN_ASSEMBLY).toString(), year, month, cycle));
		cyclePriceMap.put(SEATING, seatingRateCalculator(cycleMap.get(SEATING).toString(), year, month, cycle));
		cyclePriceMap.put(HANDLE_BAR,
				handleBarRateCalculator(cycleMap.get(HANDLE_BAR).toString(), year, month, cycle));
		cyclePriceMap.put(CYCLE, cyclePriceCalculator(cyclePriceMap));
		return cyclePriceMap; 
	}
	
	public void run() {
		try {
			while(!blockingQueue.isEmpty()) {
				Map<String, Object> cycleMap = cycleBuilder(blockingQueue.take());
				Map<String, String> cyclePriceMap = priceEngine(cycleMap, cycle);
				System.out.println("Name : " + cyclePriceMap.get(NAME) + "\n" +
						   "Cycle : Rs." + cyclePriceMap.get(CYCLE) + "\n" + 
						   "Frame : Rs." + cyclePriceMap.get(FRAME) + "\n" +
						   "Wheels : Rs." + cyclePriceMap.get(WHEELS) + "\n" + 
						   "Chain Assembly : Rs." + cyclePriceMap.get(CHAIN_ASSEMBLY) + "\n" + 
						   "Seating : Rs." + cyclePriceMap.get(SEATING) + "\n" + 
						   "Handle Bar : Rs." + cyclePriceMap.get(HANDLE_BAR) + "\n");
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}

public class CycleEstimator{
	@SuppressWarnings({ "unchecked", "resource" })
	public static void main(String[] args) {
		try {
			Scanner scanner = new Scanner(System.in);
			System.out.println("Specify the file path for price list of cycle parts and its components");
			String cyclePriceJSON = scanner.next();
			ObjectMapper objectMapper = new ObjectMapper();
			Cycle cycle = objectMapper.readValue(new File(cyclePriceJSON), Cycle.class);
			System.out.println("Specify the file path for the list of cycles for which the price has to be calculated");
			String input = scanner.next();
			Object object = new JSONParser().parse(new FileReader(input));
			JSONObject jsonObject = (JSONObject) object;
			JSONArray jsonArray = (JSONArray) jsonObject.get("cycle");
			BlockingQueue<Map<String, Object>> blockingQueue = new ArrayBlockingQueue<>(jsonArray.size());
			if (jsonArray != null) {
				for (int i = 0; i < jsonArray.size(); i++) {
					blockingQueue.put((Map<String, Object>) jsonArray.get(i));
				}
			}
			CyclePriceCalculator cyclePriceCalculaorthread1 = new CyclePriceCalculator(cycle, blockingQueue);
			CyclePriceCalculator cyclePriceCalculaorthread2 = new CyclePriceCalculator(cycle, blockingQueue);
			CyclePriceCalculator cyclePriceCalculaorthread3 = new CyclePriceCalculator(cycle, blockingQueue);
			CyclePriceCalculator cyclePriceCalculaorthread4 = new CyclePriceCalculator(cycle, blockingQueue);
			CyclePriceCalculator cyclePriceCalculaorthread5 = new CyclePriceCalculator(cycle, blockingQueue);
			CyclePriceCalculator cyclePriceCalculaorthread6 = new CyclePriceCalculator(cycle, blockingQueue);
			CyclePriceCalculator cyclePriceCalculaorthread7 = new CyclePriceCalculator(cycle, blockingQueue);
			CyclePriceCalculator cyclePriceCalculaorthread8 = new CyclePriceCalculator(cycle, blockingQueue);
			CyclePriceCalculator cyclePriceCalculaorthread9 = new CyclePriceCalculator(cycle, blockingQueue);
			CyclePriceCalculator cyclePriceCalculaorthread10 = new CyclePriceCalculator(cycle, blockingQueue);
			cyclePriceCalculaorthread1.start();
			cyclePriceCalculaorthread2.start();
			cyclePriceCalculaorthread3.start();
			cyclePriceCalculaorthread4.start();
			cyclePriceCalculaorthread5.start();
			cyclePriceCalculaorthread6.start();
			cyclePriceCalculaorthread7.start();
			cyclePriceCalculaorthread8.start();
			cyclePriceCalculaorthread9.start();
			cyclePriceCalculaorthread10.start();
		} catch (FileNotFoundException exception) {
			System.out.println("File specified is not found in the mentioned path. Please check and try again");
			exception.printStackTrace();
		} catch (UnrecognizedPropertyException exception) {
			System.out.println("Fields mentioned in JSON doesn't match with the fields in model. "
					+ "Specify the fields properly in JSON. Please check and try again");
			exception.printStackTrace();
		} catch (JsonMappingException | JsonParseException exception) {
			System.out.println("Incorrect JSON syntax or the file is empty. Please check and try again");
			exception.printStackTrace();
		} catch (IOException exception) {
			System.out.println("Exception in IO occurs. Please check and try again");
			exception.printStackTrace();
		} catch (ParseException exception) {
			System.out.println("Exception occurs while parsing. Please check and try again");
			exception.printStackTrace();
		} catch (InterruptedException exception) {
			System.out.println("Current thread has been interrupted. Please check and try again");
			exception.printStackTrace();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
}