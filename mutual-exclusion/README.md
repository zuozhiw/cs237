# cs237

How to start the mutual exclusion module
---

1. Run `mvn clean install` to build your application
2. Start application with `java -jar target/cs237-1.0-SNAPSHOT.jar server config.yml`
3. To check that your application is running enter url `http://localhost:8180`

Health Check
---

To see your applications health enter url `http://localhost:8181/healthcheck`

Start the mutual exclusion module on a different port
---
1. Modify config.yml to change BOTH the application port and admin port
2. Run `mvn clean install` to build your application
3. Start application with `java -jar target/cs237-1.0-SNAPSHOT.jar server config.yml`
4. To check that your application is running enter url `http://localhost:[YOUR CHOSEN PORT NUMBER]`