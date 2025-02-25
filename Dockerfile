FROM tomcat:10-jdk11
COPY SimpleSearchEngine.war /usr/local/tomcat/webapps/SimpleSearchEngine.war
COPY lib /usr/local/tomcat/lib/
EXPOSE 8080
CMD ["catalina.sh", "run"]