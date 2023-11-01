# Start with a base image containing Java runtime
FROM openjdk:17-jdk-slim

# Make port 8080 available to the world outside this container
EXPOSE 8080

# The application's JAR file
ARG JAR_FILE=build/libs/bs-cassandra-1.0-SNAPSHOT.jar

# Add the application's JAR to the container
COPY ${JAR_FILE} app.jar

# Run the JAR file
ENTRYPOINT ["java","-jar","/app.jar"]