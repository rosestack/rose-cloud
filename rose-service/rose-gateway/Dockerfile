FROM eclipse-temurin:21.0.8_9-jre-jammy

COPY target/*.jar app.jar

ENTRYPOINT ["java", "-Xms128m", "-Xmx256m", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]

EXPOSE 9999
