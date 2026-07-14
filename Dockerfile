FROM eclipse-temurin:17-jre
WORKDIR /app
COPY build/libs/*.jar app.jar
RUN useradd -r -u 1001 appuser
USER appuser
ENTRYPOINT ["java", "-jar", "app.jar"]