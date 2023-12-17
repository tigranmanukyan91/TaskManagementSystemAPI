# Task Management System

A simple Task Management System implemented in Java with Spring.

## Overview

This project is a Task Management System (API-only) developed in Java using Spring technologies. It provides functionality for creating, editing, deleting, and viewing tasks. Each task includes a title, description, status, priority, author, and assignee.

## Requirements

1. **Authentication and Authorization**
    - Users are authenticated and authorized using email and password.
    - API access requires JWT token authentication.

2. **Task Management**
    - Users can manage their tasks: create, edit, view, delete, change status, and assign tasks.
    - Users can view tasks from other users.
    - Task assignees can change the status of their tasks.

3. **Comments**
    - Users can leave comments on tasks.

4. **API Functionality**
    - Retrieve tasks by a specific author or assignee.
    - Retrieve all comments for tasks.
    - Provide filtering and pagination for output.

5. **Error Handling**
    - The service handles errors gracefully and returns clear messages.
    - Input data is validated.

6. **Documentation**
    - The service is well-documented.
    - API described using OpenAPI and Swagger.
    - Swagger UI is configured.

7. **Local Setup**
    - Instructions for local setup included in the README.
    - Development environment set up with Docker Compose.

8. **Testing**
    - Basic tests covering essential system functions.

## Technologies Used

- Java 17+
- Spring, Spring Boot
- Database: PostgreSQL or MySQL
- Spring Security for authentication and authorization
- Docker Compose for development environment

## Getting Started

### Prerequisites

- Java 17+
- Docker Compose

### Local Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/your/repository.git
   cd repository
