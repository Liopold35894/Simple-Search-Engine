FROM tomcat:10.1.34-jdk17-openjdk-slim
COPY simplesearch.war /usr/local/tomcat/webapps/simplesearch.war
EXPOSE 8080
CMD ["catalina.sh", "run"]