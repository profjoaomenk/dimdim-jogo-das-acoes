# Stage 1: Build
FROM maven:3.9.5-eclipse-temurin-21 AS build

WORKDIR /app

# Copiar arquivos do projeto
COPY pom.xml .
COPY src ./src

# Build da aplicação
RUN mvn clean package -DskipTests

# Stage 2: Run
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copiar JAR do stage de build
COPY --from=build /app/target/dimdim-stock-game.jar app.jar

# Copiar imagem do logo se necessário
RUN mkdir -p /app/images

# Expor porta
EXPOSE 8080

# Variáveis de ambiente
ENV JAVA_OPTS="-Xms256m -Xmx512m"

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/ || exit 1

# Executar aplicação
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
