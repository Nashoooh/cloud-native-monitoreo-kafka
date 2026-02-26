FROM openjdk:21-jdk-slim

# Configurar directorio de trabajo
WORKDIR /app

# Copiar Wallet de Oracle
COPY Wallet/ /app/wallet/

# Copiar JAR de la aplicación
COPY target/monitoreo-kafka-0.0.1-SNAPSHOT.jar app.jar

# Exponer puerto 9200
EXPOSE 9200

# Ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
