FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY build/libs/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
MAINTAINER "Thijnmens"
LABEL org.opencontainers.image.source = "https://github.com/Adosarfai/Backend" 