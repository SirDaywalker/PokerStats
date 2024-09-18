# define which jdk & os to use
FROM openjdk:17-jdk-alpine

# Copy the jar into the docker image
COPY target/*.jar app.jar

#Run the jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
