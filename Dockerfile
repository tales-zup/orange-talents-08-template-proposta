FROM openjdk:11
COPY . /usr/src/myapp
WORKDIR /usr/src/myapp
ENTRYPOINT ["java", "-jar", "target/proposta-service.jar"]