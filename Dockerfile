FROM tomcat:9.0.78-jre17

RUN rm -rf /usr/local/tomcat/webapps/*

ADD backend/target/cart_crafters.war /usr/local/tomcat/webapps/ROOT.war

RUN curl -fsSL https://deb.nodesource.com/setup_18.x | bash - && apt-get install -y nodejs

RUN mkdir -p /usr/local/next
ADD frontend/.next/standalone /usr/local/next
ADD frontend/.next/static /usr/local/next/.next/static

EXPOSE 8080
EXPOSE 3000

ENV PORT 3000
ENV HOSTNAME localhost

CMD ["/bin/sh", "-c", "catalina.sh run & node /usr/local/next/server.js"]