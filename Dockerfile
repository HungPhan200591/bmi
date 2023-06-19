# Maven build container 

FROM maven:3.8.3-openjdk-11 AS maven_build
WORKDIR /project
COPY pom.xml .

COPY src/ /project/src

RUN mvn -f /project/pom.xml install -Dmaven.test.skip=true

FROM adoptopenjdk/openjdk11:jre-11.0.15_10-alpine

RUN mkdir /app

COPY --from=build /project/target/bmi-1.0.jar /app/bmi.jar

ENTRYPOINT [ "sh","-c","java -jar /app/bmi.jar"]