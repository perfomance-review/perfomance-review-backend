FROM maven:3.6.3-jdk-11 as build

COPY performance-review-backend/ ./

RUN mvn clean install -DskipTests

FROM adoptopenjdk/openjdk11:jre-11.0.6_10-alpine

COPY --from=build \
    target/performance-review-backend.jar \
    config/prod/service.properties \
    config/prod/hibernate.properties ./


CMD java \
    -DsettingsDir=./ \
    -DP=PROD \
    -DdbHost=${PG_HOST} \
    -DdbUser=${PG_USER} \
    -DdbPass=${PG_PASSWORD} \
    -jar performance-review-backend.jar

