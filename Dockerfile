# ---------- Stage 1: build the JAR ----------
FROM eclipse-temurin:25-jdk AS build
WORKDIR /app

# Copy only what Maven needs to download dependencies first
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw && ./mvnw dependency:go-offline -B

# Then copy the source code and build
COPY src ./src
RUN ./mvnw package -DskipTests -B

# ---------- Stage 2: run the JAR ----------
FROM eclipse-temurin:25-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]