# Stage 1: Build
FROM maven:3.9.5-eclipse-temurin-17 AS build

WORKDIR /app

# Copiar apenas pom.xml primeiro para cache de dependências
COPY pom.xml .

# Baixar dependências (será cacheado se pom.xml não mudar)
RUN mvn dependency:go-offline -B

# Copiar código fonte
COPY src ./src

# Build da aplicação
RUN mvn clean package

# Stage 2: Run
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Criar usuário não-privilegiado
RUN addgroup -S spring && adduser -S spring -G spring

# Copiar JAR do stage de build
COPY --from=build /app/target/dimdim-stock-game.jar app.jar

# Criar diretório de imagens com permissões corretas
RUN mkdir -p /app/images && chown -R spring:spring /app

# Instalar curl para healthcheck
RUN apk add --no-cache curl

# Mudar para usuário não-privilegiado
USER spring:spring

# Expor porta
EXPOSE 8080

# Variáveis de ambiente
ENV JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Executar aplicação
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
