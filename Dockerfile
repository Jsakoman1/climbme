FROM node:22-alpine AS frontend-build
WORKDIR /frontend
COPY frontend ./
RUN node scripts/build.mjs

FROM maven:3.9.9-eclipse-temurin-21 AS backend-build
WORKDIR /build
COPY backend/pom.xml backend/pom.xml
RUN mvn -q -f backend/pom.xml dependency:go-offline
COPY backend backend
COPY --from=frontend-build /frontend/dist backend/src/main/resources/static
RUN mvn -q -f backend/pom.xml -DskipTests package

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=backend-build /build/backend/target/*.jar app.jar
EXPOSE 8080
HEALTHCHECK --interval=30s --timeout=5s --start-period=30s --retries=3 CMD wget -qO- "http://localhost:${PORT:-8080}/actuator/health" || exit 1
CMD ["sh", "-c", "java -Dserver.port=${PORT:-8080} -jar app.jar"]
