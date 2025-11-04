# 👾 BEASTIES BACKEND DEMO PROJECT 👾

Demo project for a collectible creature management game

## ⚙️ Requirements
- Java 21
- Maven 3.9+
- Docker & Docker Compose
- MySQL

---

## Project structure
- DDD
- MVC
- Entity - DTO - Mappers
```
src/main/java/com/beasties/beasties_backend
├─ config
├─ domain
│   ├─ model
│   └─ repository
├─ application
│   ├─ dto
│   └─ mapper
├─ security
│   ├─ jwt
│   └─ service
├─ web
│   └─ controller
└─ shared
└─ exception
```

---
### API ENDPOINTS REFERENCEConfiguration
```
docker-compose up -d
```

### API ENDPOINTS REFERENCE
**Controllers & endpoints**:
- **authController**
    - POST /api/auth/register    --> RegisterRequest -> UserDTO
    - POST /api/auth/login       --> LoginRequest -> AuthResponse

- **UserController**
  - GET  /api/users/{id}           -> UserDTO  (ADMIN puede ver cualquiera; USER solo suya)
  - GET  /api/users/me             -> UserDTO

- **BestieController**
  - POST   /api/beasties           -> Adopt (Create) BeastieRequest -> BeastieDTO
  - GET    /api/beasties           -> list owned (paged) -> List<BeastieDTO>
  - GET    /api/beasties/{id}      -> BeastieDTO (owner/ADMIN)
  - DELETE /api/beasties/{id}      -> libera (owner/ADMIN)
  - POST   /api/beasties/{id}/team -> toggle assign to team
  - POST   /api/beasties/{id}/eat  -> body { candyType } -> changes stats
  - POST   /api/beasties/{id}/tasks -> assign task to beastie
  - POST   /api/beasties/{id}/quests/{questId} -> send to quest

- **QuestController**
  - POST /api/quests
  - PUT  /api/quests/{id}
  - DELETE /api/quests/{id}
  - GET  /api/quests

- **Map Controller**
  - POST /api/map/spawn-random
  - GET  /api/map

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

---

### Notes (features to be implemented):
- User login system (ROL_USER / ROL_ADMIN)
- Creatures: adopt, modify details, view collection and delete
- Quests: assign, complete, rewards, experience
- Map: grid, quest randomizer
- Tasks: training, modify stats
- Eat: stat modifier

- **REFACTOR**:
  - .ENV 
  - JWT_SECRET (save in secret/vault in production --> more protection)

## 📚 Additional Resources
- [JWT + Spring boot](https://www.geeksforgeeks.org/springboot/spring-boot-3-0-jwt-authentication-with-spring-security-using-mysql-database/)
- [JWT](https://www.geeksforgeeks.org/web-tech/json-web-token-jwt/)

[Back to top](#top)