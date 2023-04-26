# Software Testing Project (Spring 2023)

Team members: Lu Wu, Thomas Yu, James Yu

## Project Intro
### Subject Application
- 2,610 LOC with 27 methods and 15 frontend pages. Link: https://github.com/jaygajera17/E-commerce-project-springBoot
- ⚙️Spring Mvc, JDBC , Jsp, Servlet
- 📝ADMIN MODULE + USER MODULE, with CRUD OPERATION
### Testing Project
- Testing code: 9 test classes, 2035 lines of code, 88 test methods for b/w testing, 65 test methods for GUI testing.
- Testing approach: blackbox, whitebox, GUI (using Selenium).
- B/W testing classes: AdminControllerTest and UserControllerTest
- GUI testing classes: BuyGUI, CategoryGUI, CustomerGUI, LoginGUI, ProductGUI, ProfileGUI, RegisterGUI

## How To Run tests?

(1) Pre requirement:- java, Idea IDE, xampp installed

(2) For xampp, download from https://www.apachefriends.org/download.html. Open it and start runnning apache and mysql.
 
<img width="60%" alt="image" src="https://i.postimg.cc/QVLNgVVk/xampp.jpg">
 
To check it work, go to 'http://localhost/dashboard' (Mac), or 'http://localhost:80/dashboard' (port may vary based on your setting)

<img width="60%" alt="image" src="https://i.postimg.cc/3xJHDgxC/xampp-home.jpg">


(3) Set up database
```sh
 Click 'phpMyAdmin' and make database name :- springproject 
 ```
```sh
 import springproject.sql file in database to Create all table and insert original data 
 ```

(4) Run the application -> JtSpringProjectApplication.java
```sh
import the project to Idea and load it as a Maven project. Download all maven dependencies.
```
```sh
Use maven plugin -> springboot-boot:run to run the application first
```
<img width="40%" alt="image" src="https://i.postimg.cc/3JcfB6jR/run.jpg">

(5) Keep the application running, and run tests class. Make sure to drop and re-run the sql file before running the test file.

<img width="40%" alt="image" src="https://i.postimg.cc/cJLL7vdw/tests.jpg">


## Other information

### Project proposal
https://docs.google.com/document/d/16xoSUQ0kyuVDpPBt6RZNdpRW7gpzLQhMeK8E7Wvmcj8/edit?usp=sharing

### Log in 
Admin Module (http://localhost:8080/admin) 
-  user name:- admin
-  password :- 123
-  Note(above default username and password , it can be change in the database)

  User module
-  user name:- jay 
-  password:- 123

