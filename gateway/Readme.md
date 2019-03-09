
```sh
 cd dockers 
```

```sh
 docker-compose up 
```

If any changes in gateway folder, rerun the docker-compose command as

```sh
 cd gateway 
```

```sh
 docker-compose build .
```

send some data to the input topic using a curl request

```sh
 curl --data xxx http://localhost:8080/entry/input 
```

Check the input topic to see if the data arrived.

```sh
 docker-compose exec kafka kafka-console-consumer --bootstrap-server localhost:9092 --topic input --new-consumer --from-  beginning 
```        

