FROM openjdk:11.0-jre as runtime
COPY target/sportlimacenter-backend-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java -jar app.jar"]
