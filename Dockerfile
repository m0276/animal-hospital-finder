#build stage
FROM amazoncorretto:21 as Builder

WORKDIR /app

COPY . .

RUN chmod +x ./gradlew
RUN ./gradlew clean build --no-daemon -x test

#run stage
EXPOSE 80
FROM amazoncorretto:21

WORKDIR /app

ENV PROJECT_NAME=animal_Hospital_Service
ENV PROJECT_VERSION=0.0.1-SNAPSHOT
ENV JVM_OPTIONS="-Xms512m -Xmx1024m -XX:+UseG1GC"

COPY --from=builder /app/build/libs/${PROJECT_NAME}-${PROJECT_VERSION}.jar app.jar

ENTRYPOINT ["sh", "-c", "java $JVM_OPTIONS -jar app.jar"]

