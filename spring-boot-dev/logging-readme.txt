1. use logging.config args to specify externalized logback config file location. ex
	--logging.config=./config/logback.xml
	
2. use scanPeriod in logback.xml to scan confing. ex
	scanPeriod="10 seconds"

3. use -Dspring.profiles.active=dev or add it to application.properties to specify spring profile

4. spring profile and scanPeriod cannot work together