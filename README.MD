# PageIndex


### Description
Application downloads web-page via URL and calculates the frequency of words on the page. 
Statistics are saved in the database and output to the console.


### How to use
Requires Java 11 or greater.  
To start application run command:  
`./gradlew bootRun -q --console=plain`

Enter URL in format "https://www.example.com/" to process page.  
Statistics will be printed in console and stored in a database. 
You can retrieve statistics from an earlier processed page.  
Enter command `find` and then URL to find it in the database. The result will be printed in console.  
Enter command `exit` to terminate the application. 

Database console: `http://localhost:8800/h2-console`  
JDBC URL: `jdbc:h2:./pageindexdb`  
User Name: `sa`  
Password: `password`
