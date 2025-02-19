FROM tomcat:10-jdk11
COPY SimpleSearchEngine /usr/local/tomcat/webapps/SimpleSearchEngine
EXPOSE 8080
CMD ["catalina.sh", "run"]