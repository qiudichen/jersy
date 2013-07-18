This project demo how to use log4j to log message to relational database asynchronously.

1. create logger table by running sql.ddl

2. configure log4j
   In log4j.xml, DatabaseAppender tells log4j to log message to sql server database
   ASYNC tells log4j to log message asynchronously and it will pipe the message to DatabaseAppender.