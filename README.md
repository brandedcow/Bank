Christopher Kang 2017

Bank System utilizing Java, JDBC, DB2

# Installation

```sh
$ cd Bank
$ javac programlauncher.java
$ java -cp ":db2jcc4.jar" ProgramLauncher db.properties
```
# Files
--------------------------
##### BankingSystem.java
- Contains db2 Queries

##### BankingUI.java
- Contains Menu
- Contains User IO

##### BatchInputProcessor.java
- Contains Batch Input for Seeding Data

##### db.properties
- Contains Database Connection Info

##### db2jcc4.jar
- Driver for db2

##### p1.sql
- Contains Table Creation Statements

##### ProgramLauncher.java
- Contains Main Method
