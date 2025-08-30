# Backend – FEP: Financement entre particuliers
---

## Tools
- **JAVA 17** (`--no-standalone`)
- **Spring Boot 3**

---

## ⚙️ Docker

1.**Run all project (without changes)**
```bash
docker-compose -f docker-compose.prod.yml up -d
```

2.**Relaunch prod (with latest changes)**
```bash
./gradlew clean bootJar
docker compose -f docker-compose.prod.yml down -v 
docker compose -f docker-compose.prod.yml up --build -d           
```

3.**Launch dev**
```bash
docker compose -f docker-compose.dev.yml up --build -d          
```