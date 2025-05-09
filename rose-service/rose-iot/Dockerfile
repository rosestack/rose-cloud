FROM eclipse-temurin:8-jre-jammy

COPY target/*.jar app.jar

ENTRYPOINT ["java", "-Xms128m", "-Xmx256m", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]

EXPOSE 7010
