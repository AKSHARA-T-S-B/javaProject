package family;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Model to store the details of a person and his/her family
 * @author Akshara
 *
 */
public class Person {
	private String name;
	private Gender Gender;
	private Family family;
	private Family parentFamily;
	
	public synchronized Family getFamily() {
		return family;
	}

	public synchronized Family getParentFamily() {
		return parentFamily;
	}

	public Person(String name, Gender Gender){
		this.name = name;
		this.Gender = Gender;
	}
	
	public Person(String name, Gender Gender, Family parentFamily) {
		this.name = name;
		this.Gender = Gender;
		this.parentFamily = parentFamily;
	}

	public synchronized String getName() {
		return name;
	}
	public synchronized void setName(String name) {
		this.name = name;
	}
	public synchronized Gender getGender() {
		return Gender;
	}
	public synchronized void setGender(Gender Gender) {
		this.Gender = Gender;
	}
	
	/**
	 * To get the person's father
	 * @return the reference of the father
	 */
	public Person getFather()
	{
		return  (this.parentFamily != null)?this.parentFamily.getHusband():null;
	}
	
	/**
	 * To get the person's mother
	 * @return the reference of the mother
	 */
	public Person getMother()
	{
		return  (this.parentFamily != null)?this.parentFamily.getWife():null;
	}
	
	/**
	 * To get the eprson's spouse
	 * @return the refrence of the spouse
	 */
	public Person getSpouse()
	{
		if(this.family != null)
		{
			return (this.Gender.equals(Gender.MALE))?this.family.getWife():this.family.getHusband();
		}
		return null;
	}
	
	/**
	 * To get the siblings 
	 * @return the list of siblings
	 */
	public List<Person> getSiblings()
	{
		List<Person> result = new ArrayList<>();
		if (this.parentFamily != null && this.parentFamily.getChildren() != null) {
			result.addAll(this.parentFamily.getChildren().stream().filter(child -> (!child.getName().equalsIgnoreCase(this.getName())))
					.collect(Collectors.toList()));
		}
		return result;
	}
	
	public Person addSon(String name)
	{
		addChild(name, Gender.MALE);
		return this;
	}
	
	public Person addDaughter(String name)
	{
		addChild(name, Gender.FEMALE);
		return this;
	}	
	
	/**
	 * To add a child to a family
	 * @param childName
	 * @param Gender
	 * @return the refernce of a child
	 */
	public Person addChild(String childName, Gender Gender)
	{
		if(this.family == null)
			System.out.println("There is no such family!!!");
		Person child = null;;
		if(childName != null && !childName.trim().isEmpty() && this.family != null)
		{
				child = new Person(childName, Gender, this.family);
				if(this.family.getChildren() == null)
					this.family.setChildren(new ArrayList<>());
				this.family.getChildren().add(child);
		}
		return child;
	}
	
	public List<Person> getChildren()
	{
		List<Person> result = new ArrayList<>();
		if(this.family != null && this.family.getChildren() != null)
			result.addAll(this.family.getChildren());
		return result;
	}
	
	/**
	 * To add husband or wife to the member of family
	 * @param spouse : Bride/Groom
	 * @return : reference Groom/Bride
	 */
	public Person addMarriage(Person spouse)
	{
		if(spouse != null)
		{
				this.family = new Family(this, spouse);
				spouse.family = this.family;
				return spouse;
		}
		return null;
	}
	
	/**
	 * print members of the family
	 * @param root
	 * @param level
	 */
	public void printMembers(Person root,int level)
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

}
