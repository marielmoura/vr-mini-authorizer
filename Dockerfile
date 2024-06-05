# Use the official maven/Java 17 image to create a build artifact.
# https://hub.docker.com/_/maven
FROM maven:3.8.4 AS build

# Copy local code to the container image.
WORKDIR /app
COPY pom.xml .
COPY src ./src

# Build a release artifact.
RUN mvn package -DskipTests

# Use OpenJDK 17 to run the build artifact
FROM openjdk:17-jdk-alpine

# Refer to Maven build -> finalName
ARG JAR_FILE=app/target/*.jar

# cd /opt/$APP_NAME
WORKDIR /opt/app

# cp target/spring-boot-web.jar /opt/$APP_NAME/app.jar
COPY --from=build ${JAR_FILE} app.jar

# java -jar /opt/$APP_NAME/app.jar
ENTRYPOINT ["java","-jar","app.jar"]