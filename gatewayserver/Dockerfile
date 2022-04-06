FROM openjdk:11-slim
LABEL maintainer="Tomas Krejcar <krejcar.tomas@seznam.cz>"
ARG JAR_FILE
COPY ${JAR_FILE} app.jar
COPY wait-for-it.sh .
RUN chmod +x ./wait-for-it.sh
ENTRYPOINT ["./wait-for-it.sh", "eurekaserver:8070", "-t", "45", "--", "java", "-jar", "/app.jar"]