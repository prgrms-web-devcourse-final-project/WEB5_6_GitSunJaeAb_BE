FROM openjdk:21
WORKDIR /app
COPY build/libs/mapick-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-Duser.timezone=Asia/Seoul", "-jar", "app.jar"]