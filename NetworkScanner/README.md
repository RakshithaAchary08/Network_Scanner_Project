# Network Monitoring and Device Scanner

## Phase 1: Setup Project

### Recommended software

1. Install Java JDK 17 or above
2. Install an IDE: NetBeans or IntelliJ IDEA

### Project structure

- `pom.xml` - Maven build file
- `src/main/java/com/networkscanner/App.java` - starter application
- `src/main/java/com/networkscanner/DeviceInfo.java` - device model
- `src/main/java/com/networkscanner/NetworkScanner.java` - scanner logic
- `src/main/java/com/networkscanner/ScannerGUI.java` - GUI stub

### Run the project

From the project root:

```sh
mvn clean compile exec:java -Dexec.mainClass=com.networkscanner.App
```

If you use NetBeans or IntelliJ IDEA, open the project folder and import the Maven project.
