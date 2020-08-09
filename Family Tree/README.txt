Family tree program is to model out the King Shan family so that :
	- Given a 'name' and a 'relationship', the people corresponding to that relationship in the order in which they were added to the family will be displayed asssuming the names of the family members are unique.
	- Program will enable us to add a child to any family in the tree through the mother.

System Requirements:
---------------------
	- System must have java-8 or higher version.
	- System must comply minimum requirements specified for jvm.

Libraries added in the application
-----------------------------------
	- junit-4.13.jar
		- Used for unit testing of the code. All Unit tests are written in TestGeekTrust.java, which can be run using TestRunner.java.
	- hamcrest-core-1.3.jar
		- Compile time dependency of Junit.
	- mockito-all-1.9.5.jar
		- Used to create and configure mock objects which simplifies the development of tests for classes with external dependencies.

Running in an IDE
---------------------
If you want to run the application in an IDE, such as Eclipse, copy the code and add the corresponding dependencies into classpath and then run FamilyTree.java to run application or TestFamilyTree.java to run the tests.

Execution instructions:
--------------------------
	- Program will take the location to the test file as the input paramter. 
	- Input will be read from a text file.
	- Corresponding output to the test file will be displayed in the console.
 
Relationships handled:
-----------------------
	Paternal-Uncle
	Maternal-Uncle
	Paternal-Aunt
	Maternal-Aunt
	Sister-In-Law
	Brother-In-Law
	Son
	Daughter
	Siblings 

Inputs to the application:
-------------------------
	- To add child
		ADD_CHILD Tritha Aarohi Female
		
	- To search a relation
		GET_RELATIONSHIP Vasa Siblings


Assumptions:
-------------
	- Existing family tree in the problem statement will be initialised during the start of the program.
	- Each person who is added in family tree must have parent associated with it except head of the family. 
	- All Persons should have a unique name in the tree.
	- A member can only be added with an existing member only.

