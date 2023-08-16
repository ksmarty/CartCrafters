# Base image
FROM alpine:latest

# Install dependencies
RUN apk update && apk add --no-cache openjdk17 nodejs

# Set environment variables
ENV JAVA_HOME=/usr/lib/jvm/java-17-openjdk
ENV PATH="$JAVA_HOME/bin:${PATH}"

# Install Tomcat
ENV TOMCAT_MAJOR=9
ENV TOMCAT_VERSION=9.0.78
ENV CATALINA_HOME=/opt/tomcat

RUN wget -q https://mirror.navercorp.com/apache/tomcat/tomcat-${TOMCAT_MAJOR}/v${TOMCAT_VERSION}/bin/apache-tomcat-${TOMCAT_VERSION}.tar.gz && \
    tar -xf apache-tomcat-${TOMCAT_VERSION}.tar.gz && \
    rm apache-tomcat-${TOMCAT_VERSION}.tar.gz && \
    mv apache-tomcat-${TOMCAT_VERSION} ${CATALINA_HOME}

# Copy Frontend Files
RUN mkdir -p /usr/local/next
ADD frontend/.next/standalone /usr/local/next
ADD frontend/.next/static /usr/local/next/.next/static

# Copy Backend Files
RUN rm -rf ${CATALINA_HOME}/webapps/*
ADD backend/target/cart_crafters.war ${CATALINA_HOME}/webapps/ROOT.war

# Expose ports
EXPOSE 8080
EXPOSE 3000

ENV PORT 3000
ENV HOSTNAME localhost

# Start Tomcat
CMD ["/bin/sh", "-c", "/opt/tomcat/bin/catalina.sh run & node /usr/local/next/server.js"]