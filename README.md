# Backend – FEP: Financement entre particuliers
---

## Tools
- **JAVA 17** (`--no-standalone`)
- **Spring Boot 3**

---

## ⚙️ Docker

1.**Run MYSQL container only (DEV environment)**
```bash
docker-compose -f docker-compose.dev.yml up -d
```

2.**Run all project**
```bash
./gradlew clean bootJar
docker-compose -f docker-compose.prod.yml up -d
```

