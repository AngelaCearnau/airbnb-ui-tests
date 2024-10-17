
Pre requisites for running tests:  
- Java (JDK) 17 and Apache Maven 3.9.9 - install and configure environment variables  
- Chrome driver 114 - download and save locally. Path for chromedriver.exe is hardcoded in the project as c:\webdrivers\


----------------------------------------
1. **Build project**  
In a command prompt, in project folder run command:  
mvn clean install -DskipTests
----------------------------------------
2. Change directory to target  
cd target 
----------------------------------------
3. **Run tests**  
In target folder run:  
java -cp airbnbtests-1.0-SNAPSHOT.jar;airbnbtests-1.0-SNAPSHOT-tests.jar;libs/* org.testng.TestNG ../XML-Test-Suites/SearchTestSuite.xml

----------------------------------------
To check a test report already generated, download entire folder
https://github.com/AngelaCearnau/airbnb-ui-tests/tree/master/test-output, and open emailable-report.html file 
