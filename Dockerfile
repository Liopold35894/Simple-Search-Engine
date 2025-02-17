FROM tomcat:10-jdk11
COPY simplesearch.war /usr/local/tomcat/webapps/simplesearch.war
EXPOSE 8080
CMD ["catalina.sh", "run"]