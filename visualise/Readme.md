```sh
 cd dockers 
```

```sh
 docker-compose up 
```

If any changes in visualise folder, rerun the docker-compose command as

```sh
 cd visualise 
```

```sh
 docker-compose build .
```

Visualise the data in the output topic via visualise docker running a jupyter notebook at <a>http://localhost:8088</a>

Note: to get the password for the JupyterLab server, log into the docker container 

```sh
 docker exec -it visualise /bin/bash
```

and execute the following command

```sh
 jupyter notebook list 
```

The value of the token is the default password.

Run all the cells of the notebook graph.ipynb and see a live graph.







