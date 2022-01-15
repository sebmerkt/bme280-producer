FROM maven:3.6.3-jdk-11

ENV TOPIC sensors.bme280
ENV BROKER1 localhost
ENV BROKER_PORT 9092
ENV SCHEMAREGISTRYIP localhost

COPY pom.xml pom.xml
COPY src/ src/
COPY checkstyle/ checkstyle/
COPY checkstyle.xml checkstyle.xml

RUN mvn clean install

CMD ["mvn", "exec:java", "-Dexec.mainClass=Producer"]