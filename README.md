## Account-Management Test

### Tools:
    - Java 11 : it should be installed before running script
    - Spring boot 2.7.3
    - Maven 3+ : it should be installed before running script
    - Ms Access : Import database into MS ACCESS from this folder `database/accountsdb.accdb`

### Project Description
- When you run frontend the login page will show the all username and password so you can use whatever you want
- You can search only with admin credential 
- You must import database because I bind user column in the statement table

### Configuration for database:

- Go to this file of the below Project & File : 
  - config-server\src\main\resources\config\application.properties
- Change the path where you put accountsdb.accdb file in your system
- Replace C:/code/test/JavaTest with your drive path & make sure to use slash. Below is the example
- db.path=C:/code/test/JavaTest/accountsdb.accdb

### **Build Project to create Jar for all applications**
- Go to build-script.bat file for the root project 
- Open in the text editor and set the root path of all project in the below line
  - set project_path=C:/code/test/user-account-project/
  - Run the bat file 

- If you want to run project manually, go to specific folder in the below sequence and build with the mentioned commands

  - eureka-server
    - mvn clean package
  - config-server
    - mvn clean package
  - common-module
    - mvn clean package
  - user-ws
    - mvn clean package
  - account-ws
    - mvn clean package
  - account-statement-ws
    - mvn clean package
  - api-gateway-ws
    - mvn clean package


### **Run Jar in the below sequence**
#### **Go to project folder of the specific project and & Run below command**

- eureka-server:
    - java -jar target\eureka-server-0.0.1-SNAPSHOT.jar

- config-server:
    - java -jar target\config-server-0.0.1-SNAPSHOT.jar

- user-ws
    - java -jar target\user-ws-0.0.1-SNAPSHOT.jar

- account-ws
    - java -jar target\config-server-0.0.1-SNAPSHOT.jar

- account-statement-ws
    - java -jar target\account-statement-ws-0.0.1-SNAPSHOT.jar

- api-gateway-ws
    - java -jar target\api-gateway-ws-0.0.1-SNAPSHOT.jar


### **See the Sonar Report**
- I take screenshot of the projects because export in the pdf require paid version, I might be wrong to find export in the UI
- Go to reports folder to see the project report
- I do not cover too much test coverage because It will require too much time and its just test :)

### **Frontend Project**
- For Build & Run, Go to `front-end` folder, use below command
- npm i
- npm start



    

	
	
	

