# Multi-stage build를 사용한 최적화된 Dockerfile

# Stage 1: Build stage
FROM openjdk:21-jdk-slim as builder

WORKDIR /app

# Gradle wrapper와 설정 파일들을 먼저 복사 (캐싱 최적화)
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# 의존성 다운로드 (소스 코드 변경시에도 캐시 유지)
RUN chmod +x ./gradlew
RUN ./gradlew dependencies --no-daemon

# 소스 코드 복사
COPY src src

# 애플리케이션 빌드
RUN ./gradlew build --no-daemon -x test

# Stage 2: Runtime stage
FROM openjdk:21-jre-slim

# 보안을 위한 non-root 사용자 생성
RUN groupadd -r spring && useradd -r -g spring spring

# 작업 디렉토리 설정
WORKDIR /app

# 필요한 패키지 설치 및 정리
RUN apt-get update && \
    apt-get install -y --no-install-recommends \
    curl \
    && rm -rf /var/lib/apt/lists/* \
    && apt-get clean

# 빌드된 JAR 파일 복사
COPY --from=builder /app/build/libs/*.jar app.jar

# 애플리케이션 디렉토리 소유권 변경
RUN chown -R spring:spring /app

# non-root 사용자로 전환
USER spring

# JVM 메모리 설정을 위한 환경변수
ENV JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseG1GC -XX:MaxGCPauseMillis=200"

# 포트 노출
EXPOSE 8080

# Health check 추가
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# 애플리케이션 실행
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]