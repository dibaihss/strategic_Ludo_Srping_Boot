FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw mvnw.cmd pom.xml ./

# Fix permission issue
RUN chmod +x ./mvnw

COPY src ./src

# Run Maven with the wrapper
RUN ./mvnw clean package -DskipTests

EXPOSE 8080

# And update the CMD to use PORT environment variable
CMD ["sh", "-c", "java -jar target/strategicLudo-0.0.1-SNAPSHOT.war --server.port=8080"]
# docker run -p 8080:8080 -e PORT=8080 java-application