FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw mvnw.cmd pom.xml ./

# Fix permission issue - make this more explicit
RUN chmod +x mvnw
RUN ls -la mvnw  # Verify permissions

COPY src ./src

# Run Maven with the wrapper
RUN ./mvnw clean package -DskipTests

EXPOSE 8080

CMD ["java", "-jar", "target/strategicLudo-0.0.1-SNAPSHOT.war"]