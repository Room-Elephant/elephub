FROM eclipse-temurin:21-jre

RUN groupadd -r elephubGroup \
    && useradd -r -g elephubGroup elephubUser

# Set working directory
WORKDIR /opt

# Copy application jar
COPY target/elephub-0.0.1-SNAPSHOT.jar elephub-service/app.jar

# Copy entrypoint.sh script
COPY entrypoint.sh entrypoint.sh
RUN chmod +x entrypoint.sh

USER elephubUser
ENTRYPOINT ["./entrypoint.sh"]