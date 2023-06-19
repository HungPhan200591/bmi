FROM maven:3.8.3-jdk-11-slim AS build
RUN chmod +x mvnw

WORKDIR /project

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src/ /project/src

RUN mvn package

FROM adoptopenjdk/openjdk11:jre-11.0.15_10-alpine

RUN mkdir /app

COPY --from=build /project/target/bmi-1.0.jar /app/bmi.jar

CMD ["java","-jar","/app/bmi.jar"]