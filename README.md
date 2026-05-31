Book E-commerce Project

This is a simple book e-commerce project.
The backend is built with Spring Boot, and the frontend is built with React.
The database used in the project is PostgreSQL.

Frameworks and tools used:
Spring Boot
Spring Data JPA
Spring Security with JWT
React
Vite
Material UI
PostgreSQL
Docker

Database name:
book_ecommerce

Tables:
users
categories
books
orders
order_items
reviews

Main run commands:

1. Start PostgreSQL:
docker compose up -d

2. Run the backend:
.\mvnw.cmd spring-boot:run

3. Go to the frontend folder:
cd frontend

4. Install frontend packages:
npm install

5. Run the frontend:
npm run dev

Backend URL:
http://localhost:8080/api

Frontend URL:
http://localhost:5173

Notes:
Create a .env file in the root folder beside pom.xml.
The home page shows books for all visitors.
New registered users are created as CUSTOMER.
Admin users can be set manually from the database by changing the role to ADMIN.

Postman collection:
postman/book-ecommerce-backend.postman_collection.json
