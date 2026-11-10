# ---------- Etapa 1: build com Maven + JDK 21 ----------
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /build
COPY pom.xml .
COPY src ./src
RUN mvn -B -q clean package -DskipTests

# ---------- Etapa 2: imagem final enxuta (somente JRE 21) ----------
FROM eclipse-temurin:21-jre
ENV TZ=America/Sao_Paulo \
    JAVA_OPTS="-XX:MaxRAMPercentage=75 -XX:+UseSerialGC -Xss512k"
WORKDIR /app
COPY --from=build /build/target/app.jar /app/app.jar
EXPOSE 8080
# O Render injeta a variável PORT; application.properties usa server.port=${PORT:8080}
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
