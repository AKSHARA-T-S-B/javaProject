package family;

import java.util.List;

/**
 * Model to store the family details of the person
 * @author Akshara
 */
public class Family {
	private Person husband;
	private Person wife;
	private List<Person> children;
	
	public Family(Person member1, Person member2) {
		super();
		this.husband = (member1.getGender().equals(Gender.MALE))?member1:member2;
		this.wife = (member1.getGender().equals(Gender.FEMALE))?member1:member2;;
	}
	
	public synchronized Person getHusband() {
		return husband;
	}
	public synchronized void setHusband(Person husband) {
		this.husband = husband;
	}
	public synchronized Person getWife() {
		return wife;
	}
	public synchronized void setWife(Person wife) {
		this.wife = wife;
	}
	public synchronized List<Person> getChildren() {
		return children;
	}
	public synchronized void setChildren(List<Person> children) {
		this.children = children;
	}
}
