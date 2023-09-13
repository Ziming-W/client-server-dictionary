# Purpose
A multi-threaded client-server dictionary that supports words CRUD.  
Project work for COMP90015 (Distributed Systems) at Unimelb. 
+ The client is designed with SWING GUI.  
+ The server uses JSON to store dictionary entries.  
+ The server has a hand-crafted worker thread pool.  
+ Communication protocol is TCP. 

# More details
`./comp90015-a1-ZimingWang-report.pdf` contains all the implementation details, protocols, and highlights. 

# Gradings
+ 14.5/15  
+ only lose marks due to "missing class diagrams in the report". I didn't include detailed class diagram because of the limitation on pages... 

# HOW TO RUN
## run from compiled jar
1. go to `/submission-jars`
2. run server  
```java -jar DictionaryServer.jar <port> <dic json file>```   
for example  
```java -jar DictionaryServer.jar 8080 test.json```
3. run client  
```java -jar DictionaryClient.jar <server IP> <server port>```  
for example  
```java -jar DictionaryClient.jar 127.0.0.1 8080```

## run from main class
server main is located at `/Server/Server.java`  
client main is located at `/Client/Client.java`  
Use the same command line argument for jar to run them
