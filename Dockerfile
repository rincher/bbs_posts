# Multi-stage build를 사용한 최적화된 Dockerfile

# Stage 1: Build stage
FROM eclipse-temurin:21-jdk as builder

WORKDIR /app

# Gradle wrapper와 설정 파일들을 먼저 복사
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# 의존성 다운로드
RUN chmod +x ./gradlew
RUN ./gradlew dependencies --no-daemon

# 소스 코드 복사
COPY src src

# 애플리케이션 빌드
RUN ./gradlew build --no-daemon -x test

# 빌드된 파일 확인
RUN ls -la /app/build/libs/

# Stage 2: Runtime stage
FROM eclipse-temurin:21-jre

WORKDIR /app

# 필요한 패키지 설치
RUN apt-get update && \
    apt-get install -y --no-install-recommends curl && \
    rm -rf /var/lib/apt/lists/*

# JAR 파일만 복사 (WAR 파일 제외)
COPY --from=builder /app/build/libs/*-SNAPSHOT.jar app.jar

# 파일 확인
RUN ls -la /app/

# 사용자 생성 및 권한 설정
RUN groupadd -r spring && useradd -r -g spring spring && \
    chown -R spring:spring /app

USER spring

ENV JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseG1GC"
EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]