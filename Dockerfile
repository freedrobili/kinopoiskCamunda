FROM gradle:8.2.1-jdk17 AS GRADLE_BUILD

MAINTAINER kuber

WORKDIR /home/gradle/project

COPY build.gradle settings.gradle /home/gradle/project/
COPY gradle /home/gradle/project/gradle

COPY . .

RUN ./gradlew build

FROM openjdk:17-alpine3.14

WORKDIR /app

COPY --from=GRADLE_BUILD /home/gradle/project/build/libs/kinopoiskCamunda-0.0.1-SNAPSHOT.jar /app/kinopoiskCamunda.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "kinopoiskCamunda.jar"]
