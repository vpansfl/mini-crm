# Render Web Service(Docker)용 이미지. 비밀값은 이미지에 넣지 않고 Render 환경변수로 주입한다.

# ---- build ----
FROM eclipse-temurin:21-jdk AS build
WORKDIR /workspace

# 의존성 레이어 캐시: Gradle 설정 파일을 먼저 복사한다.
COPY gradlew settings.gradle build.gradle ./
COPY gradle ./gradle
RUN chmod +x gradlew && ./gradlew --no-daemon dependencies > /dev/null

COPY src ./src
# 테스트는 DB 연결이 필요하므로 이미지 빌드에서는 제외한다.
RUN ./gradlew --no-daemon bootJar -x test \
    && cp "$(ls build/libs/*.jar | grep -v -- '-plain.jar$')" app.jar

# ---- run ----
FROM eclipse-temurin:21-jre
WORKDIR /app

RUN useradd --system --create-home spring
USER spring

COPY --from=build /workspace/app.jar app.jar

# Render는 PORT 환경변수를 주입한다. (server.port=${PORT:8080}, server.address=0.0.0.0)
ENV SPRING_PROFILES_ACTIVE=prod \
    JAVA_OPTS="-XX:MaxRAMPercentage=75 -XX:+UseSerialGC"
EXPOSE 8080

ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/app.jar"]
