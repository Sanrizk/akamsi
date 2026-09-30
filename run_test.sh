#!/bin/bash
./mvnw clean package -DskipTests
java -jar target/akamsi-0.0.1-SNAPSHOT.jar &
PID=$!
sleep 15
curl -v http://localhost:8080/login
kill $PID
