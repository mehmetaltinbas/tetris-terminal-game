FROM eclipse-temurin:25-jre

RUN apt-get update && apt-get install -y wget && \
    wget https://github.com/tsl0922/ttyd/releases/download/1.7.7/ttyd.aarch64 -O /usr/local/bin/ttyd && \
    chmod +x /usr/local/bin/ttyd && \
    apt-get clean

WORKDIR /app

COPY target/tetris-terminal-game-1.0-SNAPSHOT.jar app.jar

EXPOSE 7681

CMD ["ttyd", "--writable", "java", "-jar", "app.jar"]
