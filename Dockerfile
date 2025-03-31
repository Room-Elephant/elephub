FROM eclipse-temurin:21-jre

# Set working directory
WORKDIR /opt

# Copy application jar
COPY target/elephub-jar-with-dependencies.jar elephub-service/app.jar

# Copy entrypoint.sh script
COPY entrypoint.sh entrypoint.sh
RUN chmod +x entrypoint.sh

ENTRYPOINT ["./entrypoint.sh"]