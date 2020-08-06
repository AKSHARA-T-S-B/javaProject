package family;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Family tree problem
 * @author Akshara
 */
class RelationHandler{

	/**
	 * Build default family tree
	 * @return head of the family 
	 */
	Person buildShanFamily(){
		Person root = new Person("Shan", Gender.MALE);
		root.addMarriage(new Person("Anga", Gender.FEMALE));
		root.addSon("Chit");
		root.addSon("Ish");
		root.addSon("Vich");
		root.addSon("Aras");
		root.addDaughter("Satya");
		Person chit = searchMember(root, "Chit");
		chit.addMarriage(new Person("Amba", Gender.FEMALE));
		chit.addDaughter("Dritha");
		chit.addDaughter("Tritha");
		chit.addSon("Vritha");
		Person dritha = searchMember(root, "Dritha");
		dritha.addMarriage(new Person("Jaya", Gender.MALE));
		dritha.addSon("Yodhan");
		Person vich = searchMember(root, "Vich");
		vich.addMarriage(new Person("Lika", Gender.FEMALE));
		vich.addDaughter("Vila");
		vich.addDaughter("Chika");
		Person aras = searchMember(root, "Aras");
		aras.addMarriage(new Person("Chitra", Gender.FEMALE));
		aras.addDaughter("Jnki");
		aras.addSon("Ahit");
		Person jnki = searchMember(root, "Jnki");
		jnki.addMarriage(new Person("Arit", Gender.MALE));
		jnki.addSon("Laki");
		jnki.addDaughter("Lavnya");
		Person satya = searchMember(root, "Satya");
		satya.addMarriage(new Person("Vyan", Gender.MALE));
		satya.addSon("Asva");
		satya.addSon("Vyas");
		satya.addDaughter("Atya");
		Person asva = searchMember(root, "Asva");
		asva.addMarriage(new Person("Satvy", Gender.FEMALE));
		asva.addSon("Vasa");
		Person vyas = searchMember(root, "Vyas");
		vyas.addMarriage(new Person("Krpi", Gender.FEMALE));
		vyas.addSon("Kriya");
		vyas.addDaughter("Krith");
		return root;
	}

	/**
	 * Search if the person is part of the family
	 * @param head
	 * @param name
	 * @return the searched person
	 */
	Person searchMember(Person head, String name) {
		if (head == null || head.getName().equalsIgnoreCase(name))
			return head;
		if(head.getSpouse() != null && head.getSpouse().getName().equalsIgnoreCase(name))
			return head.getSpouse();
		if (head.getChildren() != null) {
			Person temp = null;
			for (Person person : head.getChildren()) {
				temp = searchMember(person, name);
				if (temp != null)
					return temp;
			}
		}
		return null;
	}

	/**
	 * print members of the family
	 * @param root
	 * @param level
	 */
	void printMembers(Person root,int level)
	{
		if (root == null)
			return;
		String indent="";
		for(int i=level; i > 0; i--)
			indent +="\t";
		if(root.getSpouse() != null)
			System.out.println(indent+"->| "+root.getName()+"/"+root.getSpouse().getName());
		else
			System.out.println(indent+"->| "+root.getName());

		if (root.getChildren() != null) {
			level++;
			for (Person person : root.getChildren()) {
				printMembers(person,level);
			}
		}
	}

	/**
	 * Add a child to a family
	 * @param motherName
	 * @param child
	 * @param gender
	 * @param root
	 */
	public void addMember(String motherName, String child, String gender, Person root) {
		Person person = searchMember(root, child);
		if(person == null) {
			Person mother = searchMember(root, motherName);
			if(mother == null)
				System.out.println("PERSON_NOT_FOUND");
			else if(mother != null && mother.getGender().equals(Gender.FEMALE)) {
				Person spouse = mother.getSpouse();
				if(spouse == null)
					System.out.println("CHILD_ADDITION_FAILED");
				else {
					mother.addChild(child, gender.equals("Male") ? Gender.MALE : Gender.FEMALE);
					System.out.println("CHILD_ADDITION_SUCCEEDED");
				}
			}else
				System.out.println("CHILD_ADDITION_FAILED");
		}else
			System.out.println("Duplicate person found. CHILD_ADDITION_FAILED");
	}
	
	/**
	 * Print list of persons
	 * @param personList
	 */
	public void printList(List<Person> personList) {
		if(personList.isEmpty())
			System.out.println("NONE");
		else {
			personList.forEach(action -> {
				System.out.print(action.getName() + " ");
			});
			System.out.println();
		}
	}

	/**
	 * Print the kids of the mother
	 * @param person : mother's details
	 * @param gender : Gender of the kid
	 */
	public void getKids(Person person, Gender gender) {
		List<Person> childrenList = person.getChildren();
		if(childrenList.isEmpty())
			System.out.println("NONE");
		else {
			List<Person> kidsList = new ArrayList<>();
			kidsList.addAll(childrenList.stream().filter(action->action.getGender().equals(gender)).collect(Collectors.toList()));
			printList(kidsList);
		}
	}

	/**
	 * Print the siblings of the person
	 * @param person
	 */
	public void getSiblings(Person person) {
		List<Person> siblingsList = person.getSiblings();
		if(siblingsList.isEmpty())
			System.out.println("NONE");
		else {
			siblingsList.forEach(action -> {
				System.out.print(action.getName() + " ");
			});
		}
		System.out.println();
	}

	/**
	 * Print the maternal or paternal aunts or uncles
	 * @param parent
	 * @param gender
	 */
	public void getAuntUncle(Person parent, Gender gender) {
		List<Person> siblingsList = parent.getSiblings();
		if(siblingsList.isEmpty())
			System.out.println("NONE");
		else {
			List<Person> auntUncleList = new ArrayList<>();
			auntUncleList.addAll(siblingsList.stream().filter(action->action.getGender().equals(gender)).collect(Collectors.toList()));
			printList(auntUncleList);
		}
	}

	/**
	 * Print the sister-in-law or brother-in-law of the person 
	 * @param person
	 * @param gender
	 */
	public void getInLaws(Person person, Gender gender) {
		List<Person> inLawList = new ArrayList<>();
		Person spouse = person.getSpouse();
		if(spouse != null) {
			List<Person> siblingsList = spouse.getSiblings();
			if(!siblingsList.isEmpty()) {

				inLawList.addAll(siblingsList.stream()
						.filter(action -> action.getGender().equals(gender))
						.collect(Collectors.toList()));
			}
		}
		List<Person> siblings = person.getSiblings();
		if(!siblings.isEmpty()) {
			List<Person> spouseList = new ArrayList<>();
			spouseList.addAll(siblings.stream()
					.map(action -> action.getSpouse())
					.filter(result -> result != null)
					.collect(Collectors.toList()));
			inLawList.addAll(spouseList.stream()
					.filter(action -> action.getGender().equals(gender))
					.collect(Collectors.toList()));
		}
		printList(inLawList);
	}

	/**
	 * Decode the relationship and invoke the corresponding relation function call
	 * @param name
	 * @param relation
	 * @param root
	 */
	public void getRelation(String name, String relation, Person root) {
		Person person = searchMember(root, name);
		if(person == null)
			System.out.println("PERSON_NOT_FOUND");
		else { 
			if(relation.equalsIgnoreCase("son")) 
				getKids(person, Gender.MALE);
			if(relation.equalsIgnoreCase("daughter")) 
				getKids(person, Gender.FEMALE);
			if(relation.equalsIgnoreCase("siblings")) 
				getSiblings(person);
			if(relation.equalsIgnoreCase("paternal-uncle")) 
				getAuntUncle(person.getFather(), Gender.MALE);
			if(relation.equalsIgnoreCase("paternal-aunt")) 
				getAuntUncle(person.getFather(), Gender.FEMALE);
			if(relation.equalsIgnoreCase("maternal-uncle")) 
				getAuntUncle(person.getMother(), Gender.MALE);
			if(relation.equalsIgnoreCase("maternal-aunt")) 
				getAuntUncle(person.getMother(), Gender.FEMALE);
			if(relation.equalsIgnoreCase("sister-in-law")) 
				getInLaws(person, Gender.FEMALE);
			if(relation.equalsIgnoreCase("brother-in-law")) 
				getInLaws(person, Gender.MALE);
		}
	}
}
/**
 * @author Akshara
 * Main program to run the Family tree application
 */
public class Geektrust {
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		boolean flag = false;
		RelationHandler relationHandler = new RelationHandler();
		Person root = relationHandler.buildShanFamily();
		System.out.println("Enter the location for the test file");
		Scanner scanner = new Scanner(System.in);
		String filePath = scanner.nextLine();
		try {
			FileInputStream fileReader = new FileInputStream(filePath);   
			scanner = new Scanner(fileReader);
			while(scanner.hasNextLine()) {
				String input = scanner.nextLine();
				String[] splitString = input.split("\\s+");
				if(splitString.length > 0 && splitString[0].contains("ADD_CHILD")) {
					flag = true;
					if(splitString.length != 4)
						System.out.println(input.trim() + " - Insufficient details provided to add a child");	
					else
						relationHandler.addMember(splitString[1], splitString[2], splitString[3], root);                                         
				}
				if(splitString.length > 0 && splitString[0].contains("GET_RELATIONSHIP")) {
					flag = true;
					if(splitString.length != 3)
						System.out.println(input + " - Insufficient details provided to get a relationship");
					else
						relationHandler.getRelation(splitString[1], splitString[2], root);
				}
			}
			if(!flag) {
				System.out.println("Input file is empty!!!");
			}
		}catch(FileNotFoundException exception) {
			System.out.println("File not found. Incorrect file path specified !!! Try again with correct file path...");
		}
	}

}
