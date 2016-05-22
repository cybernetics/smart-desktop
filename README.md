# JK-Smart-Desktop
My baby framework ,I started to implement it since 2005, and it has been used to build more than 40 applications for different sectors(Education,Banking and Government) .

**Important:** JK-Smart-Desktop is free for Personal use only, for quotation , kindly contact me at Kiswanij@GMail.com 
  
![JK-Smart-Desktop](https://raw.githubusercontent.com/kiswanij/smart-desktop/master/doc/screenshots/4.PNG "JK-Smart-Desktop")  
## Prerequisites:
  1-Install MySql database (e.g. `root` as username , `123456` as password)  
  2-On mysql , create database name (e.g. `jk-smart-desktop-db`)  
  **Optional**: for Feedback and Support functionality `SMTP` mail server settings will be needed

## Usage:
1-	Create Maven project
2-	Add smart-desktop dependency  

	<dependency>
		<groupId>com.jalalkiswani</groupId>
		<artifactId>smart-desktop</artifactId>
		<version>0.0.8</version>
	</dependency>

3- Be sure to set the minimum JDK level in your pom file to 1.7 and tell maven to ignore web.xml by adding the following sections to your pom file:

	<build>
		<plugins>
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-compiler-plugin</artifactId>
				<version>3.3</version>
				<configuration>
					<!-- http://maven.apache.org/plugins/maven-compiler-plugin/ -->
					<source>1.7</source>
					<target>1.7</target>
				</configuration>
			</plugin>
		</plugins>
	</build>

4- Create config files named `system.config` in your project root folder:

	db-host=localhost
	db-user=root
	db-password=123456
	db-port=3306
	db-name=jk-smart-desktop-db
	
	encoded=false
	tablemeta.dynamic.generate=true
	
	jk-support-mail-from=XYZ@Domain.com
	jk-support-mail-to=XYZ2@Domain2.com
	
	jk-mail-user=Username
	jk-mail-password=Password
	
5- Create your application main class , as follows:

	package com.jalalkiswani.demo;
	
	import com.fs.commons.application.ApplicationManager;
	
	public class SmartDesktopDemo {
		public static void main(String[] args)  {
			ApplicationManager.getInstance().start();
		}
	}
	 
6- Now run your main class, to check your installation:  
![Application Console](https://raw.githubusercontent.com/kiswanij/smart-desktop/master/doc/screenshots/1.PNG "JK-Smart-Desktop Application console")

7- If this was your first installation, a confirmation dialog will apear to ask you installing the base script on the specified database in the configuration file:   
![Database script confirmation dialog](https://raw.githubusercontent.com/kiswanij/smart-desktop/master/doc/screenshots/2.PNG "JK-Smart-Desktop script confirmation")  
Click `Yes`  

7- The appliction login-dialog will appear , Enter `admin` as username , `123456` as password:  
![Application Login Dialog](https://raw.githubusercontent.com/kiswanij/smart-desktop/master/doc/screenshots/3.PNG "JK-Smart-Desktop login dialog")  

8- Now you have the application and the framework up and running :  
![Application home page](https://raw.githubusercontent.com/kiswanij/smart-desktop/master/doc/screenshots/4.PNG "JK-Smart-Desktop")  

## Features
There is a very long long of features which you can't expect, be patient until i have some time to summarize it. 
Coming soon ...

## Example 1 : Simple Add new CRUD page
1- In the database, create sample table , for example:

	CREATE TABLE `employees` (
	  `id` int(11) NOT NULL AUTO_INCREMENT,
	  `name` varchar(255) DEFAULT NULL,
	  `salary` double DEFAULT NULL,
	  PRIMARY KEY (`id`)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8;

2- Create file named name `menu.xml` in `src/main/resources` folder , and add the below to it: 

	<main-menu>
		<menu name="HR">
			<menu-item name="Employees Management">
				<properties>
					<property name="table-meta" value="employees" />
				</properties>
			</menu-item>
		</menu>
	</main-menu>
 
3- Run your application , login , on the top of the windows, click on the `Application` module , click on `Hr` menu , then `Employees` menu-item,
To enter new record , click on `Add`  on the top left on the windows , enter the information , then click add:
 ![Example CRUD Views](https://raw.githubusercontent.com/kiswanij/smart-desktop/master/doc/screenshots/5.PNG "JK-Smart-Desktop")

4- To edit any record , double record on the record in the Data table : 
 ![Edit mode](https://raw.githubusercontent.com/kiswanij/smart-desktop/master/doc/screenshots/6.PNG "JK-Smart-Desktop")
 
## Example 2 : 
Advanced CRUD with Master Details (One-To-One,One-Many,Many-To-Many)    
## Example 3 : 
Database UI Components   
## Example 4 : 
Build Simple UI   
## Example 5 : 
Build Advanced UI   
## Example 6 : 
Reports    
And many others...
 
<http://wwww.JalalKiswani.com>