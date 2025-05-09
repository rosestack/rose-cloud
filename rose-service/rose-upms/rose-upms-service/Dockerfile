FROM library/alpine-jdk:8

EXPOSE 8010

COPY target/*.jar app.jar

ENTRYPOINT ["java","-Xms128m","-Xmx256m", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
