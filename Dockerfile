FROM jboss/wildfly

COPY target/java-rest-api-sandbox-1.0.war /opt/jboss/wildfly/standalone/deployments/ROOT.war

EXPOSE 8080

CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]