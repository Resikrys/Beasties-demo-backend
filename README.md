# 🐾 Beasties Demo: Creature and Quest Management Backend

This project is a demonstration of a Spring Boot-based game backend
designed to simulate the management of collectible creatures
("Beasties") that users can train through tasks and send on quests
across a map. The application implements design patterns
(SRP, DTOs, Mapper) and modern security (JWT).

## 💻 Technologies & Stack
- **Language**: Java 21
- **Framework**: Spring boot
- **Package manager**: Maven 3.9+
- **DDBB**: MySQL & H2
- **Security**: spring security
- **Data access**: JPA
- **Documentation**: Swagger/OpenAPI
- **Docker & Docker Compose**

### Additional dependencies:
- **Lombok**: Simplifies the creation of getters, setters, constructors,
  and `toString()`.
- **JWT (JSON Web Token)**: Required for generating and validating
  security tokens.
  - io.jsonwebtoken:jjwt-api:0.12.5
  - io.jsonwebtoken:jjwt-impl:0.12.5
  - io.jsonwebtoken:jjwt-jackson:0.12.5 (Scope runtime)

- **Validation**: jakarta.validation:jakarta.validation-api and the
  Spring Boot validation starter.
- **OpenAPI/Swagger UI**: For documenting and testing endpoints
  (if included in your Spring Boot 3+ pom.xml).

---

## Project structure
- MVC (Entity - DTO - Mappers)
- Contexts:
  - **Auth**
  - **Beastie**
  - **Config**
  - **Exception**
  - **Item**
  - **Map**
  - **Quest**
  - **Task**
  - **User**

## 🧪 How to Test the Backend (Step by Step)
To test the full functionality of the backend (Authentication,
Beasties, Tasks, Quests), follow these steps using a tool like
Postman or by accessing the Swagger documentation.

1. 🚀 Start the Server
   Open the project in your IDE (IntelliJ IDEA).

Run the main Spring Boot class (```BestiesDemoApplication.java```).

The server will start, typically at ```http://localhost:8080```.

2. 📝 Access Postman / Swagger (Optional)
   You can view all the endpoints and test them directly at:
   ```http://localhost:8080/swagger-ui/index.html```

3. The first step is to register to obtain a JWT token.
- **User Registration**:
  - Method: ```POST```
  - URL: ```/api/v1/auth/register```
  - Body (JSON):
```
{
  "username": "tester",
  "password": "password"
}
``` 
- Result: You will receive a ```jwtToken```. **Save this token**.
- **Login** (If you want to obtain a new token):
  - Method: ```POST```
  - URL: ```/api/v1/auth/login```
  - Body (JSON): (same credentials)
  - Result: New ```jwtToken```.

**IMPORTANT**: For all subsequent requests (except ```/auth```), you must add
the JWT token to the header: ```Authorization: Bearer <YOUR_JWT_TOKEN```>

4. Basic Game Flux:

   | Step |   Module |     Description | Method | URL                                                                              |
      |-----|----------|-----------------|-------|----------------------------------------------------------------------------------|
   |   A. | Adopt Beastie |    Create your first creature. | ```POST```  | ```/api/v1/beasties``` ```Body (JSON): {"name": "Sparky", "type": "EXPLORER"}``` |
   |   B. |  View Beasties |   Verify that your creature has been created. | ```GET```   | ```/api/v1/beasties```                                                           |
   |   C. |   Assign Task | Tasks Send Sparky on a task to improve stats. (You need to know Sparky's ID and the ID of a task, e.g., 1) | ```POST```  | ```/api/v1/tasks/assign/{beastieId}/{taskId}```                                  |
   |   D. | Complete Task |  Tasks Wait X minutes and check if the task has finished. | ```PATCH``` | ```api/v1/tasks/complete/{beastieId}```                                          |

### API ENDPOINTS

| **Context**          | **Method**   | **Endpoint (Path)**                                  | **Description**                                                 | **Permissions** |
|----------------------|--------------|------------------------------------------------------|-----------------------------------------------------------------|-----------------| 
| **AUTHENTICATION**   | ```POST```   | ```/auth/register```                                 | "Registers a new user (username, password, optional role)."     | PUBLIC          |
|                      | ```POST```   | ```/auth/login```                                    | Authenticates the user and returns the JWT.                     | PUBLIC          |
| **BEASTIES (USER)**  | ```POST```   | ```/beasties```                                      | Creates and adopts a new creature.                              | ROLE_USER       |
|                      | ```GET```    | ```/beasties```                                      | Gets the list of Beasties from the logged-in user.              | ROLE_USER       |
|                      | ```PATCH```  | ```/beasties/{id}/team```                            | Toggles the state of a Beastie for the active team.             | ROLE_USER       |
|                      | ```PATCH```  | ```/beasties/{id}/rename```                          | [TIP] Allows the user to rename one of their own creatures.     | ROLE_USER       |
|                      | ```DELETE``` | ```/beasties/{id}```                                 | Releases (deletes) a creature.                                  | ROLE_USER       |
| **BEASTIES (ADMIN)** | ```GET```    | ```/admin/beasties```                                | Gets the list of ALL existing Beasties.                         | ROLE_ADMIN      |
|                      | ```PATCH```  | ```/admin/beasties/rename```                         | Renames any Beastie.                                            | ROLE_ADMIN      |
| **INVENTORY**        | ```GET```    | ```/inventory```                                     | Gets the logged-in user's inventory.                            | ROLE_USER       |
|                      | ```PATCH```  | ```/inventory/consume```                             | Consumes an item (e.g., candy) from the inventory.              | ROLE_USER       |
| **MAP**              | ```GET```    | ```/map```                                           | Gets the current state of the map with the Quests.              | ROLE_USER       |
|                      | ```POST```   | ```/map/randomize```                                 | Completely regenerates the map                                  | ROLE_ADMIN      |
|                      | ```DELETE``` | ```/map/clear/{x}/{y}```                             | Deletes a Quest from a specific cell on the map.                | ROLE_ADMIN      |
| **QUESTS (USER)**    | ```POST```   | ```/quests/start/{beastieId}/{x}/{y}```              | Sends a creature to start a Quest at coordinates.               | ROLE_USER       |
|                      | ```PATCH```  | ```/quests/complete/{beastieId}```                   | Checks the Quest status and completes it if it has finished.    | ROLE_USER       |
| **QUESTS (ADMIN)**   | ```POST```   | ```/admin/quests```                                  | Creates a new Quest (available to the map).                     | ROLE_ADMIN      |
|                      | ```GET```    | ```/admin/quests```                                  | Gets all defined Quests.                                        | ROLE_ADMIN      |
|                      | ```GET ```   | ```/admin/quests/{id}```                             | Gets a specific Quest.                                          | ROLE_ADMIN      |
|                      | ```PUT```    | ```/admin/quests/{id}```                             | Modifies an existing Quest.                                     | ROLE_ADMIN      |
|                      | ```DELETE``` | ```/admin/quests/{id},Deletes a Quest.,ROLE_ADMIN``` |
| **TASKS**            | ```GET```    | ```/tasks```                                         | Gets the List of available tasks.                               | ROLE_USER       |
|                      | ```POST```   | ```/tasks/assign/{beastieId}/{taskId}```             | Assigns a task to a specific creature.                          | ROLE_USER       |
|                      | ```PATCH```  | ```/tasks/complete/{beastieId```                     | Checks and marks the task as completed if the time has expired. | ROLE_USER       |
| **USERS (ADMIN)**    | ```GET```    | ```/admin/users```                                   | [TIP] Lists all logged-in users.                                | ROLE_ADMIN      |
|                      | ```DELETE``` | ```/admin/users/{id}```                              | [TIP] Deletes a user account.                                   | ROLE_ADMIN      |


### Docker compose commands:
- **Build:**
```
docker-compose build
```
- **Run:**
```
docker-compose up -d
```

---

### JWT
- **Password Encoder**: BCryptPasswordEncoder
- **JwtTokenProvider**: creates/verifies tokens, signs with HMAC
  secret (or RSA if desired).
- **JwtAuthenticationFilter**: extracts Authorization header,
  validates JWT, loads UserDetails via UserDetailsService.
- **CustomUserDetailsService**: loads user from database
  (UserRepository).
- **SecurityConfig**: configures HTTP security, public endpoints
  such as "/api/auth/**", Swagger, and H2 console if applicable;
  the rest require authentication.

### FEATURES:
- **Implemented**:
  - User login system (ROL_USER / ROL_ADMIN), user control (view and delete users by admin)
  - Beasties: adopt, view collection and delete, rename, stats modifiers, experience and level up
  - Quests: assign, complete, rewards & create, update and delete quests
  - Map: grid, quest randomizer
  - Tasks: assign task (training - modify stats)
  - Item: consume candy (stat modifier)

- **To be implemented**:
  - .ENV
  - JWT_SECRET (save in secret/vault in production --> more protection)
  - Beasties: modify details


## 📚 Additional Resources
- [JWT + Spring boot](https://www.geeksforgeeks.org/springboot/spring-boot-3-0-jwt-authentication-with-spring-security-using-mysql-database/)
- [JWT](https://www.geeksforgeeks.org/web-tech/json-web-token-jwt/)

[Back to top](#top)