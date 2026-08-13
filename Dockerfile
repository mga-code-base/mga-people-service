# --- Stage 1: Build the Linux Native Binary ---
FROM ghcr.io/graalvm/native-image-community:25 AS builder
WORKDIR /build

# Copy maven wrapper and pom file first to leverage Docker caching
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline

# Copy source code and build the native linux binary
COPY src ./src
RUN ./mvnw clean native:compile -Pnative -DskipTests

# --- Stage 2: Run the Application in a lightweight image ---
# Changed from ubuntu to distroless/base, which includes required C libraries (glibc) for native binaries
FROM gcr.io/distroless/base-debian12
WORKDIR /app

# CRUCIAL FIX: Explicitly copy the native binary file, NOT a jar file named app-native
COPY --from=builder /build/target/mga-people-service /app/mga-people-service

EXPOSE 8084

# Run the native binary directly
ENTRYPOINT ["./mga-people-service", "--spring.profiles.active=default"]
