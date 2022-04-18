<h4><i># The project excerpts is taken from <a href="www.geektrust.com">GeekTrust</a> Expense Management Project.
</h4>
# Pre-requisites

* Java 1.8/1.11/1.15
* Maven

# How to run the code

We have provided scripts to execute the code.

Use `run.sh` if you are Linux/Unix/macOS Operating systems and `run.bat` if you are on Windows.

Internally both the scripts run the following commands

* `mvn clean install -DskipTests assembly:single -q` - This will create a jar file `expense-management.jar`
  in the `target` folder.
* `java -jar target/expense-management.jar sample_input/input1.txt` - This will execute the jar file passing
  in the sample input file as the command line argument

Use the pom.xml provided along with this project. Please change the main class entry (<mainClass>
Main</mainClass>) in the pom.xml if your main class has changed.

# How to execute the unit tests

`mvn clean test` will execute the unit test cases.
