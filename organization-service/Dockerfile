# Start with a base image containing Java runtime
FROM openjdk:11-slim

# Add Maintainer info
LABEL maintainer="Illary Huaylupo <illaryhs@gmail.com>"

# The application's jar file
ARG JAR_FILE

# Add the application's jar to the container
COPY ${JAR_FILE} app.jar

COPY wait-for-it.sh .

RUN chmod +x ./wait-for-it.sh

# execute the application
ENTRYPOINT ["./wait-for-it.sh", "configserver:8071", "--", "java", "-jar", "/app.jar"]
 
