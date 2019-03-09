- Start the followind containers: gateway, zookeeper, kafka, jobmanager, taskmanager, visualise

  ```sh
     make up 
  ```

- Ensure all containers are running

  ```sh
   docker container ls 
   ```
  
  Note, if any code changes are made to the content of the gateway or visualise folder, rebuild the docker before creating the containers.

  ```sh
    cd <folder_name> 
  ```
  where <folder_name> is either `gateway` or `visualise`

  ```sh
   docker build .
  ``` 

- Ensure the gateway is running at <a>http://localhost:8080</a>. Read more about the [gateway](gateway) 

- Run the max pipeline using either a direct runner or a flink runner

    1. Direct runner
    ```sh
       make direct-run 
    ```

    2. Flink runner

    ```sh
       make flink-compile 
    ```

    ```sh
       make fllink-run 
    ```

The dashboard for the running Flink job is accessible via <a>http://localhost:8081</a>

To access logs in the flink container, get a shell to the container using
  ```sh
    docker exec -it jobmanager /bin/bash 
  ```

- Stream live data from wiki api continously to the gateway via a python script

    ```sh
     python wiki_stream.py 
    ```

- Check if the running max pipeline processed the data and wrote the result to output topic

    ```sh
     make read_topic TOPIC=output 
    ```

- Visualise the data in the output topic via visualise docker running a jupyter notebook at <a>http://localhost:8088</a>. Read more about [visualisation](visualise).

  
- Stop all dockers

  ```sh
   make down 
  ```
