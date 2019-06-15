# cs237

How to start the mutual exclusion module
---

1. Run `mvn clean install` to build your application
2. Start application with `java -jar target/cs237-mutual-exclusion-1.0-SNAPSHOT.jar server config.yml`
3. To check that your application is running enter url `http://localhost:8180`


Procedures to create tokens:
---
1. Create token with an ID (any positive integer), `GET /api/token/create-token?id=1`
2. For each mutual exclusion server, inform the peer's address, `GET /api/token/add-peer?peer=http://peerhost:peerport`
3. To acquire a token, use `GET /api/token/acquire`
4. To release a token, use `GET /api/token/release`
