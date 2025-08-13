# 개선된 Dockerfile
FROM eclipse-temurin:21-jre

WORKDIR /app

# GitHub Actions에서 빌드된 JAR 복사
COPY build/libs/*.jar app.jar

RUN groupadd -r spring && useradd -r -g spring spring && \
    chown -R spring:spring /app

USER spring

ENV JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseG1GC"
EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]