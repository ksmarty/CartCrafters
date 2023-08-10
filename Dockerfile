FROM tomcat:9.0.78-jre17

RUN rm -rf /usr/local/tomcat/webapps/*

ADD backend/target/cart_crafters.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
CMD ["catalina.sh", "run"]
