package family;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mockito.Mockito;

/**
 * Testcases to test Geektrust.java
 * @author Akshara
 */
public class TestGeekTrust {
	RelationHandler relationHandler = new RelationHandler();
	Geektrust geekTrustFamily = new Geektrust();
	RelationHandler relationHandlerMock = Mockito.mock(RelationHandler.class);
	Person head = new Person("Shan", Gender.MALE);
	
	/**
	 * Tests method buildShanFamily()
	 */
	@Test
	public void testBuildShanFamily() {
		Person expectedPerson = new Person("Shan", Gender.MALE);
		Person actualPerson = relationHandler.buildShanFamily();
		assertEquals(expectedPerson.getName(), actualPerson.getName());
		assertEquals(expectedPerson.getGender(), actualPerson.getGender());
	}
	
	/**
	 * tests method searchMember()
	 */
	@Test
	public void testSearchMember() {
		Person expectedPerson = new Person("Shan", Gender.MALE);
		Person actualPerson = relationHandler.searchMember(head, "Shan");
		assertEquals(expectedPerson.getName(), actualPerson.getName());
		assertEquals(expectedPerson.getGender(), actualPerson.getGender());
		assertEquals(expectedPerson.getFamily(), actualPerson.getFamily());
		assertEquals(expectedPerson.getParentFamily(), actualPerson.getParentFamily());
	}
	
	/**
	 * tests method addMember()
	 */
	@Test
	public void testAddMember() {
		Mockito.doNothing().when(relationHandlerMock)
			.addMember("Anga", "Shah", "Male", head);
		relationHandlerMock.addMember("Anga", "Shah", "Male", head);
	    Mockito.verify(relationHandlerMock, times(1)).addMember("Anga", "Shah", "Male", head);
	}
	
	/**
	 * tests method printList()
	 */
	@Test
	public void testPrintList() {
		Mockito.doNothing().when(relationHandlerMock).printList(new ArrayList<>());
		relationHandlerMock.printList(new ArrayList<>());
		Mockito.verify(relationHandlerMock, times(1)).printList(new ArrayList<>());
	}

	/**
	 * Tests method getKids() for son 
	 */
	@Test
	public void testGetKidsSon() {
		List<Person> sonsList = new ArrayList<>();
		head.addMarriage(new Person("Anga", Gender.FEMALE));
		head.addSon("Chit");
		head.addDaughter("Satya");
		sonsList.add(relationHandler.searchMember(head, "Chit"));
		List<Person> actualSonList = relationHandler.getKids(head, Gender.MALE);
		assertEquals(sonsList, actualSonList);
	}
	
	/**
	 * Tests method getKids() for daughter
	 */
	@Test
	public void testGetKidsDaughter() {
		List<Person> daughtersList = new ArrayList<>();
		head.addMarriage(new Person("Anga", Gender.FEMALE));
		head.addSon("Chit");
		head.addDaughter("Satya");
		daughtersList.add(relationHandler.searchMember(head, "Satya"));
		List<Person> actualDaughtersList = relationHandler.getKids(head, Gender.FEMALE);
		assertEquals(daughtersList, actualDaughtersList);
	}
	
	/**
	 * Tests the result if there is no children
	 */
	@Test
	public void testGetKidsNone() {
		List<Person> kidsList = new ArrayList<>();
		head.addMarriage(new Person("Anga", Gender.FEMALE));
		List<Person> actualDaughtersList = relationHandler.getKids(head, Gender.FEMALE);
		assertEquals(kidsList, actualDaughtersList);
	}

	/**
	 * Tests method getAuntUncle for Paternal-uncle
	 */
	@Test
	public void testGetUncle() {
		List<Person> uncleList = new ArrayList<>();
		head.addMarriage(new Person("Anga", Gender.FEMALE));
		head.addSon("Chit");
		head.addDaughter("Satya");
		Person satya = relationHandler.searchMember(head, "Satya");
		satya.addMarriage(new Person("Vyan", Gender.MALE));
		satya.addSon("Asva");
		uncleList.add(relationHandler.searchMember(head, "Chit"));
		List<Person> actualUncleList = relationHandler.getAuntUncle(satya, Gender.MALE);
		assertEquals(uncleList, actualUncleList);
	}
	
	/**
	 * Tests method getAuntUncle for Maternal-aunt
	 */
	@Test
	public void testGetAunt() {
		List<Person> auntList = new ArrayList<>();
		head.addMarriage(new Person("Anga", Gender.FEMALE));
		head.addSon("Chit");
		head.addDaughter("Satya");
		Person chit = relationHandler.searchMember(head, "Chit");
		chit.addMarriage(new Person("Amba", Gender.FEMALE));
		chit.addDaughter("Dritha");
		auntList.add(relationHandler.searchMember(head, "Satya"));
		List<Person> actualAuntList = relationHandler.getAuntUncle(chit, Gender.FEMALE);
		assertEquals(auntList, actualAuntList);
	}

	/**
	 * Tests method testGetBrotherInLaw() for bother-in-law
	 */
	@Test
	public void testGetBrotherInLaw() {
		List<Person> brotherInLawList = new ArrayList<>();
		head.addMarriage(new Person("Anga", Gender.FEMALE));
		head.addSon("Chit");
		head.addDaughter("Satya");
		Person satya = relationHandler.searchMember(head, "Satya");
		satya.addMarriage(new Person("Vyan", Gender.MALE));
		brotherInLawList.add(relationHandler.searchMember(head, "Chit"));
		Person vyan = relationHandler.searchMember(head, "Vyan");
		List<Person> actualBrotherInLawList = relationHandler.getInLaws(vyan, Gender.MALE);
		assertEquals(brotherInLawList, actualBrotherInLawList);
	}
	
	/**
	 * Tests method testGetBrotherInLaw() for sister-in-law
	 */
	@Test
	public void testGetSisterInLaw() {
		List<Person> sisterInLawList = new ArrayList<>();
		head.addMarriage(new Person("Anga", Gender.FEMALE));
		head.addSon("Chit");
		head.addDaughter("Satya");
		Person chit = relationHandler.searchMember(head, "Chit");
		chit.addMarriage(new Person("Amba", Gender.FEMALE));
		sisterInLawList.add(relationHandler.searchMember(head, "Satya"));
		Person amba = relationHandler.searchMember(head, "Amba");
		List<Person> actualSisterInLawList = relationHandler.getInLaws(amba, Gender.FEMALE);
		assertEquals(sisterInLawList, actualSisterInLawList);
	}

	/**
	 * Tests method testGetRelation() 
	 */
	@Test
	public void testGetRelation() {
		Mockito.doNothing().when(relationHandlerMock).getRelation("", "", head);
		relationHandlerMock.getRelation("", "", head);
		Mockito.verify(relationHandlerMock, times(1)).getRelation("", "", head);
	}
}
