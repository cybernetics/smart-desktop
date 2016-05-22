# JK-Smart-Desktop
My baby framework ,I built it in 2005, and it has been used to build more than 40 applications for different sectors(Education,Banking and Government) .
 
## Prerequisites:
  1-Install `MySql` (e.g. `root` as username , `123456` as password)
  2-On mysql , create database name (e.g. `jk-smart-desktop-db`)
  3-**Optional**: for Feedback and Support functionality (`SMTP` mail server settings)

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

4- Create config files named `system.config` :

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
	 
6- Now run your main class:
Screenshot 1
Screenshot 2
Screenshot 3

7- Enter `admin` as username , `123456` as password , and here we go.
Screenshot 4

Now you have the framework up and running.

## Features
Coming soon

## Add new CRUD page
1- create your table in the database , for example

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
 
 3- Run your application , login ,  