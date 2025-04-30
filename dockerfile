# FROM eclipse-temurin:17-jdk

# WORKDIR /app

# COPY .mvn/ .mvn
# COPY mvnw mvnw.cmd pom.xml ./

# # Fix permission issue
# RUN chmod +x ./mvnw

# COPY src ./src

# # Run Maven with the wrapper
# RUN ./mvnw clean package -DskipTests

# EXPOSE 8080

# # And update the CMD to use PORT environment variable
# CMD ["sh", "-c", "java -jar target/strategicLudo-0.0.1-SNAPSHOT.war --server.port=8080"]
# # docker run -p 8080:8080 -e PORT=8080 java-application

# Build stage
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

# Copy Maven wrapper and POM first (for better layer caching)
COPY .mvn/ .mvn/
COPY mvnw mvnw.cmd pom.xml ./

# Fix permission issue
RUN chmod +x ./mvnw

# Download dependencies (this layer will be cached if dependencies don't change)
RUN ./mvnw dependency:go-offline

# Copy source code
COPY src/ ./src/

# Build the application
RUN ./mvnw clean package -DskipTests

# Runtime stage (smaller image)
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copy the built artifact from the build stage
COPY --from=build /app/target/strategicLudo-0.0.1-SNAPSHOT.war app.war

# Expose the application port
EXPOSE 8080

# Set environment variables (optional - can also be set at runtime)
ENV PORT=8080

# Command to run the application
CMD ["sh", "-c", "java -jar app.war --server.port=${PORT}"]