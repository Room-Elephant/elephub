FROM eclipse-temurin:21-jre

RUN addgroup -S nonroot \
    && adduser -S nonroot -G nonroot

WORKDIR /opt

COPY target/elephub-jar-with-dependencies.jar elephub-service/app.jar
COPY entrypoint.sh entrypoint.sh
RUN chmod +x entrypoint.sh

USER nonroot
ENTRYPOINT ["./entrypoint.sh"]

