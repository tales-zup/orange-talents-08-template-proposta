FROM openjdk:11
COPY ./target/proposta-service.jar proposta-service.jar
ENTRYPOINT ["java", "-jar", "proposta-service.jar"]