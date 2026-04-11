# start java container
```
docker compose -f docker-compose.yaml up
docker exec -u 0 -it openjdk-service bash
```

# In the container:
```
cd src
javac *.java
java -ea Test5
```
