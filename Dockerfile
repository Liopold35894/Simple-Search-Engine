FROM tomcat:10-jdk11
COPY simplesearch /usr/local/tomcat/webapps/simplesearch
EXPOSE 8080
CMD ["catalina.sh", "run"]