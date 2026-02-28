# start java container
```
docker compose -f docker-compose.yaml up
docker exec -u 0 -it openjdk-service bash
```

# In the container:
```
cd hw1/src
javac *.java
java -ea Test42
```
