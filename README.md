# Product Inventory Application

## Overview

The Product Inventory application is a RESTful API built using Spring Boot and Maven. It allows users to manage a product inventory with features such as creating, retrieving, updating, and deleting products. The application also supports advanced features like filtering, sorting, and JWT-based authentication.

## Features

### Core Endpoints

- **POST /products**: Create a new product.
    - Request body: Product details (name, description, price, quantity).
    - Example:
      ```json
      {
          "name": "Pikachu Plushie",
          "description": "Electric pokémon",
          "price": "9.99",
          "quantity": "10",
          "category": {
              "id": 1
          }
      }
    - URL: `http://localhost:8080/products`

- **POST /products/bulk**: Create products in bulk.
    - Request body: Product details (name, description, price, quantity).
    - Example:
      ```json
      [
        {
          "name": "Pikachu Plushie",
          "description": "Electric pokémon",
          "price": "9.99",
          "quantity": "10"
        },
        {
          "name": "Charmander Plushie",
          "description": "Fire pokémon",
          "price": "10.99",
          "quantity": "15"
        }
      ]

- **GET /products**: Retrieve a list of all products.
    - URL: `http://localhost:8080/products`
    - Optional with pagination: `http://localhost:8080/products?page=0&size=10`

- **GET /products/{id}**: Retrieve a specific product by ID.
    - URL: `http://localhost:8080/products/1`

- **PUT /products/{id}**: Update an existing product.
    - URL: `http://localhost:8080/products/1`
    - Example:
      ```json
      {
          "id": 1,
          "name": "Updated Pikachu Plushie",
          "description": "Electric pokémon - Updated",
          "price": "19.99",
          "quantity": "20",
          "version": 2
      }

- **DELETE /products/{id}**: Delete a product.
    - URL: `http://localhost:8080/products/1`




### Advanced Features

- **Filtering**: Allows filtering products (e.g., by name, price range).
- **Sorting**: Enables sorting products (e.g., by name, price).
    - URL: `http://localhost:8080/products/filter`
    - Example: `http://localhost:8080/products/filter?minPrice=9.99&maxPrice=19.99&page=0&size=5&sort=name,asc`
- **Relationships**: Introduces a Category entity with a one-to-many relationship to Product.
    - **POST /categories**: Create a new category.
    - URL: `http://localhost:8080/categories`
    - Example:
      ``` json
      {
          "name": "Toys"
      }

    - **GET /categories/{id}**: Retrieve a specific category by ID.
        - URL: `http://localhost:8080/categories/1`

    - **GET /categories**: Retrieve a listo of all categories.
        - URL: `http://localhost:8080/categories`
- **JWT & Roles**: Protects the endpoints with JWT and roles.
    - **POST /auth/login**: 
      ```json 
      {
          "username": "admin",
          "password": "password"
      }
    - This will generate JWT Token for access to protected endpoints, in order to test with postman select auth type "Bearer Token"
    - Protected endpoints by role:
      - USER: `http://localhost:8080/test/user`
      - ADMIN: `http://localhost:8080/test/admin`
      
- **Flyway**: Uses Flyway for DB initialization/population scripts.
    - Scripts will be loaded with API, in order to execute migration use Maven command: 
     ```bash
      mvn flyway:migrate
      

### Data Persistence

- Uses an embedded database (H2).

### Testing

- JUnit tests: Run with the command:
  ```bash
  mvn test
  
- Postman: Testing collection JSON is included with project, in order to use just import to postman

### Documentation

- API documentation using OpenAPI (Swagger): http://localhost:8080/swagger-ui/index.html

## Requirements

- Java (>= v17)
- Spring Boot
- Maven

## Setup Instructions

### Prerequisites

- Java 17 or higher
- Maven

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/domako86/product-inventory.git
   cd product-inventory

2. Build the project using Maven:
   ```bash
    mvn clean install

3. Run the application:
   ```bash
    mvn spring-boot:run
