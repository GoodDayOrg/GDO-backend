FROM maven:latest
WORKDIR /app
COPY . /app
RUN mvn clean install -DskipTests=true
EXPOSE 8080
ARG DB_HOST
ENV DB_HOST ${DB_HOST}
ARG DB_NAME
ENV DB_NAME ${DB_NAME}
ARG DB_USERNAME
ENV DB_USERNAME ${DB_USERNAME}
ARG DB_PASSWORD
ENV DB_PASSWORD ${DB_PASSWORD}
CMD ["java", "-jar", "/app/target/java-dropwizard-flyway-starter-1.0-SNAPSHOT.jar", "server", "/app/config.yml"]
