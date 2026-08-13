# 🧍 mga-people-service

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4%2F4.0-6DB33F?logo=springboot)](https://spring.io/)
[![Java Version](https://img.shields.io/badge/Java-25-007396?logo=openjdk)](https://openjdk.org/)
[![Build Tool](https://img.shields.io/badge/Build-Gradle%209.4.1-066DA5?logo=gradle)](https://gradle.org/)
[![GraalVM](https://img.shields.io/badge/GraalVM-Native%20Image-orange?logo=graalvm)](https://www.graalvm.org/)

## 📌 Service Overview

`mga-people-service` orchestrates all identity management business logic, user onboarding, JWT authentication, role-based access control (Admin, Trainer, Member), and profile updates.

It sits behind the Kubernetes Gateway (Porkbun DNS → Cloudflare → K8s Gateway) and communicates downstream to `mga-people-crud-service` for MongoDB operations.

### 🏛️ Architectural Features
* Edge Ingress & JWT Authentication: Handles public ingress traffic routed through K8s Gateway, issuing and validating JSON Web Tokens (JWT) for secure client sessions.
* CQRS Orchestration: Decouples business rules from raw persistence by delegating all data queries and writes down to `mga-people-crud-service` via internal HTTP RestClient exchanges.
* GraalVM Native Image Packaging: Compiled Ahead-Of-Time (AOT) using Java 25 & Gradle 9.4.1 for instant container startup and optimized Kubernetes cluster resource utilization.

---

## 🛠️ Technical Specifications
* Port: 8084 (Kubernetes Gateway Ingress)
* Language/Build: Java 25 | Gradle 9.4.1
* Image Runtime: GraalVM Native Image AOT
* Downstream Integration: mga-people-crud-service (Port 8080)
* Shared Contract: mga-all-models-jars

---

## 📡 Core API Controller Endpoints

### Auth Controller (`/api/v1/people/auth`)
* `POST /api/v1/people/auth/login` - Authenticate user credentials and issue JWT access token
* `POST /api/v1/people/auth/refresh` - Refresh expired user JWT session token
* `POST /api/v1/people/auth/logout` - Invalidate active session and logout user

### Member Controller (`/api/v1/people/members`)
* `POST /api/v1/people/members/register` - Onboard new member profile and trigger account setup
* `GET /api/v1/people/members/{id}` - Retrieve member profile details by ID
* `GET /api/v1/people/members/me` - Retrieve authenticated member's own profile summary
* `PUT /api/v1/people/members/me` - Update authenticated member profile details
* `PUT /api/v1/people/members/{id}/assign-trainer` - Assign dedicated trainer to a member

### Admin & Trainer Management Controller (`/api/v1/people/staff`)
* `GET /api/v1/people/staff/trainers` - List all active registered trainers
* `POST /api/v1/people/staff/trainers/approve/{trainerId}` - Approve pending trainer application
* `GET /api/v1/people/staff/admins/pending` - List pending admin access requests

---

## ⚙️ Build & Deployment Instructions
# Standard JVM Run
./gradlew bootRun

# GraalVM Native Image Compilation
./gradlew nativeCompile

# Paketo OCI Native Container Image
./gradlew bootBuildImage
