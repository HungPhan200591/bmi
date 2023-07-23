# Maven build container 

FROM maven:3.8.3-openjdk-11 AS build
RUN mkdir -p /root/.m2
COPY .m2 /root/.m2

WORKDIR /project
COPY pom.xml .

COPY src/ /project/src

RUN mvn -f /project/pom.xml install -Dmaven.test.skip=true

FROM openjdk:11

RUN mkdir /app

COPY --from=build /project/target/bmi-1.0.jar /app/bmi.jar

EXPOSE 8080

ENTRYPOINT [ "sh","-c","java -jar /app/bmi.jar"]