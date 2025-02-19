FROM tomcat:10-jdk11
COPY SimpleSearchEngine.war /usr/local/tomcat/webapps/SimpleSearchEngine.war
EXPOSE 8080
CMD ["catalina.sh", "run"]