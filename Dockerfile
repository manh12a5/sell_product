#Build stage

FROM gradle:latest AS BUILD
WORKDIR /usr/app/
COPY . .
RUN gradle clean build

# Package stage

#FROM openjdk:latest
#ENV JAR_NAME=sell_product-master-0.0.1-SNAPSHOT.jar
#ENV APP_HOME=/usr/app
#WORKDIR $APP_HOME
#COPY --from=BUILD $APP_HOME .
#EXPOSE 8080
#ENTRYPOINT exec java -jar $APP_HOME/build/libs/$JAR_NAME

FROM openjdk:latest
ARG JAR_FILE=build/libs/sell_product-master-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]