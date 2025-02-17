FROM amazoncorretto:17

WORKDIR /app

COPY target/capital-gains-0.0.1.jar app.jar

# Comando para executar o JAR
CMD ["java", "-jar", "app.jar"]

