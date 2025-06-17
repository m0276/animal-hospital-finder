FROM amazoncorretto:21

WORKDIR /app

COPY app/app.jar app.jar

ENV JVM_OPTIONS="-Xms512m -Xmx1024m -XX:+UseG1GC"

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JVM_OPTIONS -jar app.jar"]

