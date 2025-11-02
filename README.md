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

### Notes (features to be implemented):
- User login system (ROL_USER / ROL_ADMIN)
- Creatures: adopt, modify details, view collection and delete
- Quests: assign, complete, rewards, experience
- Map: grid, quest randomizer
- Tasks: training, modify stats
- Eat: stat modifier