import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Car parking allocation problem
 * @author Akshara
 *
 */
class Car{
    String regNo;
    String colour;
    
    Car(){
        this.regNo = null;
        this.colour = null;
    }
    
    Car(String regNo, String colour){
        this.regNo = regNo;
        this.colour = colour;
    }
    
    String getRegNo(){
        return regNo;
    }
    
    String getColour(){
        return colour;
    }
    
    boolean isNull(){
        if(this.regNo == null && this.colour == null){
            return true;
        }
        return false;
    }
}

class ParkingSpace{
    
    //createParkingSlot is to create a parking slot based on user defined input
    int createParkingSlot() throws IOException{
        int parkingSlot = 0;
        System.out.println("Enter the number of slots to create a parking lot");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try{
            parkingSlot = Integer.parseInt(reader.readLine());    
        }catch(NumberFormatException exception){
            
        }
        return parkingSlot;       
    }
    
    //parkVehicles will allocate the available slots for the vehicles which needed to be parked
    Map<String, Object> parkVehicles(TreeMap<Integer, Car> parkingMap, Car car){
        Map<String, Object> resultMap = new HashMap<>();
        if(parkingMap.isEmpty()){
            parkingMap.put(1, car);
            resultMap.put("AllocatedSlot", 1);
        }else{
            List<Integer> availableSlotList = parkingMap.entrySet()
                                                        .stream()
                                                        .filter(record -> record.getValue().isNull())
                                                        .map(Map.Entry::getKey)
                                                        .collect(Collectors.toList());
            if(availableSlotList.isEmpty()){
                int lastFilledSlot = parkingMap.lastKey();
                parkingMap.put(++lastFilledSlot, car); 
                resultMap.put("AllocatedSlot", lastFilledSlot);
            }else{
                int slot = availableSlotList.get(0);
                parkingMap.put(slot, car); 
                resultMap.put("AllocatedSlot", slot);
            }
        }
        resultMap.put("ParkingMap", parkingMap);
        return resultMap;
    }
    
    //leave will depart the parked vehicles
    TreeMap<Integer, Car> leave(TreeMap<Integer, Car> parkingMap, int slot){
        if(parkingMap.containsKey(slot)){
            if(parkingMap.size() > 1){
                parkingMap.put(slot, new Car());    
            }else{
                parkingMap = new TreeMap<>();
            }
            System.out.println("Slot number " + slot + " is free"); 
        }else{
           System.out.println("Slot number " + slot + " is already free");  
        }
        return parkingMap;
    }
    
    //status is to show the available and filled slots in the parking
    void status(TreeMap<Integer, Car> parkingMap){
            System.out.print("\nSlot No.\tRegistration No.\tColour\n");
            parkingMap.entrySet()
                      .stream()
                      .forEach(record -> {
                                            if(!record.getValue().isNull()){
                                                System.out.print(record.getKey() + "\t\t"  
                                                                 + record.getValue().getRegNo() + "\t\t\t" 
                                                                 + record.getValue().getColour() + "\n");
                                            }}); 
    }
    
    //findRegistrationNumber will return the registration numbers of the cars based on the colour specified by the user
    void findRegistrationNumber(TreeMap<Integer, Car> parkingMap, String colour){
        List<String> regNoList = parkingMap.entrySet()
                                           .stream()
                                           .filter(record -> {
                                                    if(!record.getValue().isNull() && colour.equals(record.getValue().getColour())){
                                                        return true;
                                                    }
                                                return false;
                                            })
                                            .map(p -> p.getValue().getRegNo())
                                            .collect(Collectors.toList());
                                            
        if(regNoList.isEmpty()){
            System.out.println("Not found");    
        }else{
            System.out.println("Register numbers for the cars are :");
            regNoList.forEach(System.out::println);
        }
    }
    
    //findSlotNumber will return the slot numbers of the parked cars based on the colour specified by the user
    void findSlotNumber(TreeMap<Integer, Car> parkingMap, String colour){
        List<Integer> slotNoList = parkingMap.entrySet()
                                             .stream()
                                             .filter(record -> {
                                                    if(!record.getValue().isNull() && colour.equals(record.getValue().getColour())){
                                                        return true;
                                                    }
                                                return false;
                                                })
                                             .map(value -> value.getKey())
                                             .collect(Collectors.toList());
        if(slotNoList.isEmpty()){
            System.out.println("Not found");    
        }else{
            System.out.println("Slot numbers for the cars are :");
            slotNoList.forEach(System.out::println);
        }
    }
    
    //findByRegistrationNumber will return the slot number for the particular registration number of the car
    void findByRegistrationNumber(TreeMap<Integer, Car> parkingMap, String registrationNumber){
        int slot = 0;
        for (Map.Entry<Integer, Car> slotDetails : parkingMap.entrySet()) {
            if(!slotDetails.getValue().isNull()){
                if(registrationNumber.equals(slotDetails.getValue().getRegNo())){
                    slot = slotDetails.getKey();
                        break;
                }
            }
        }
        if(slot == 0){
            System.out.println("Not found");
        }else{
            System.out.println("Slot number : " + slot);
        }
    }
    
    //findRegNoDuplicate is to find whether the car with the registration number is already parked
    boolean findRegNoDuplicate(TreeMap<Integer, Car> parkingMap, String regNo){
        if(parkingMap.isEmpty()){
            return true;
        }
        List<Integer> slotList = parkingMap.entrySet().stream()
                            .filter(record -> {
                                                if(!record.getValue().isNull() && regNo.equals(record.getValue().getRegNo())){
                                                    return true;
                                                }
                                                return false;
                                            })
                            .map(value -> value.getKey())
                            .collect(Collectors.toList());
        if(slotList.isEmpty()){
            return true;
        }
        return false;
    }
}

public class ParkingAllocation{
    
    public static void main(String []args) throws IOException{
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Scanner scanner = new Scanner(System.in);
        int parkingSlot, slot, availableSlotFlag;
        String regNo, colour;
        Map<String, Object> resultMap = new HashMap<>();
        TreeMap<Integer, Car> parkingMap = new TreeMap<>();
        ParkingSpace parkingSpace = new ParkingSpace();
        parkingSlot = parkingSpace.createParkingSlot();
        while(parkingSlot <= 0){
            System.out.println("Enter the valid number for parking slots. Slots should be greater than 0");
            parkingSlot = parkingSpace.createParkingSlot();
        }
        System.out.println("Created a parking lot with " + parkingSlot + " slots");
        availableSlotFlag = parkingSlot;
        int operation = 0;
        while(true){
            System.out.println("\n1 Park vehicles \n2 Leave \n3 Status \n4 Find \n\nNumber entered greater than 4 will exit the program\nEnter the operation to be performed");
            try{
                operation = Integer.parseInt(reader.readLine());
            }catch(NumberFormatException exception){
                operation = 0;
            }
            switch(operation){
                case 0 :System.out.println("Enter valid operation");
                        break;
                case 1 ://PARK VEHICLES
                        if(availableSlotFlag > 0){
                            System.out.println("Enter the registration number for car");
                            regNo = scanner.next();
                            boolean duplicateRegNo = parkingSpace.findRegNoDuplicate(parkingMap, regNo);
                            if(duplicateRegNo){
                                System.out.println("Enter the colour of the car");
                                colour = scanner.next();
                                Car car = new Car(regNo, colour);
                                resultMap = parkingSpace.parkVehicles(parkingMap, car);
                                availableSlotFlag = --availableSlotFlag;
                                parkingMap = (TreeMap<Integer, Car>)resultMap.get("ParkingMap");
                                System.out.println("Allocated slot number : " + (int)resultMap.get("AllocatedSlot"));
                            }else{
                                System.out.println("There is a car parked already with this register number " + regNo);
                            }
                        }else{
                           System.out.println("Sorry, parking lot is full");
                        }
                        break;
                case 2 ://LEAVE
                        System.out.println("Enter the slot number of the vehicle to depart");
                        try{
                            slot = Integer.parseInt(reader.readLine());
                        }catch(NumberFormatException exception){
                            slot = 0;
                            System.out.println("Enter valid slot");
                        }
                        if(slot > parkingSlot | slot <= 0){
                            System.out.println("There are totally " + parkingSlot 
                                + " slot/slots avaiable and slot number starts from 1. So enter valid slot");
                        }else{
                            parkingMap = parkingSpace.leave(parkingMap, slot);
                            ++availableSlotFlag;
                        }
                        break;
                case 3 ://STATUS
                        System.out.println("There are " + parkingSlot + " parking slot/slots in total");
                        if(parkingMap.isEmpty()){
                            System.out.print("All slots are free.");
                        }else{
                            parkingSpace.status(parkingMap);    
                        }
                        break;
                case 4 ://FIND
                        System.out.println("\n1 Find the registration numbers for the cars based on colour \n"
                                            + "2 Find the slot numbers for the cars based on colour \n"
                                            + "3 Find slot number for the registration number \nEnter the operation to be performed");
                        int findOption = 0;
                        try{
                            findOption = Integer.parseInt(reader.readLine());
                        }catch(NumberFormatException exception){
                            
                        }
                        switch(findOption){
                            case 1 ://FIND ALL REG NOs BASED ON CAR COLOUR
                                    System.out.println("Enter the colour");
                                    colour = scanner.next();
                                     if(parkingMap.isEmpty()){
                                        System.out.println("Not Found");
                                    }else{
                                        parkingSpace.findRegistrationNumber(parkingMap, colour);
                                    }
                                    break;
                            case 2 ://FIND ALL SLOT NOs BASED ON CAR COLOUR 
                                    System.out.println("Enter the colour");
                                    colour = scanner.next();
                                     if(parkingMap.isEmpty()){
                                        System.out.println("Not Found");
                                    }else{
                                        parkingSpace.findSlotNumber(parkingMap, colour);
                                    }
                                    break;
                            case 3 ://FIND THE SLOT NO BASED ON CAR REG NO
                                    System.out.println("Enter the registration number");
                                    regNo = scanner.next();
                                    if(parkingMap.isEmpty()){
                                        System.out.println("Not Found");
                                    }else{
                                        parkingSpace.findByRegistrationNumber(parkingMap, regNo);
                                    }
                                    break;
                            default: System.out.println("Enter valid option");
                        }
                        break;
                default:System.exit(0);
            }
        }
    }
}