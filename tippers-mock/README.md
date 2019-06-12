# cs237

How to start the cs237 mock server
---

1. Run `mvn clean install` to build your application
1. Start application with `java -jar target/cs237-1.0-SNAPSHOT.jar server config.yml`
1. To check that your application is running enter url `http://localhost:9080`

Health Check
---

To see your applications health enter url `http://localhost:9081/healthcheck`
