FROM maven:3.8.5-openjdk-17 as build

# only copy pom to cache the dependencies
COPY pom.xml /usr/src/app/
RUN mvn -f /usr/src/app/pom.xml dependency:resolve
RUN mvn -f /usr/src/app/pom.xml clean package -Dmaven.test.skip

# then copy all sources and build the app but skip the tests
COPY src /usr/src/app/src
RUN mvn -f /usr/src/app/pom.xml clean package -Dmaven.test.skip

FROM openjdk:17 
copy --from=build /usr/src/app/target/donations-1.0-SNAPSHOT-jar-with-dependencies.jar /usr/app/app.jar
#EXPOSE 7000
ENTRYPOINT ["java", "-jar", "/usr/app/app.jar", "-Duser.timezone=UTC"]
