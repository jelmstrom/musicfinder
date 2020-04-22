FROM openjdk:10 as builder
WORKDIR build
RUN apt-get update && apt-get --assume-yes install maven
ADD pom.xml .
ADD src src/
RUN ls -l
RUN mvn package -Dmaven.test.skip=true


FROM openjdk:10-slim
WORKDIR app
COPY --from=builder /build/target/musicFinder-1.0-SNAPSHOT.jar .

CMD ["java", "-jar", "musicFinder-1.0-SNAPSHOT.jar"]
EXPOSE 8081
