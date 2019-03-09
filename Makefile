ENTRY_CLASS?=com.example.pipelines.CountPipeline
APP=pipeline-0.1

up:
	docker-compose up

read_topic:
	docker-compose exec kafka kafka-console-consumer \
			--bootstrap-server localhost:9092 \
			--topic $(TOPIC) \
			--new-consumer \
			--from-beginning
			
## DIRECT RUNNER
direct-run:
	docker-compose run beam mvn compile exec:java \
		-Dexec.mainClass=$(ENTRY_CLASS) \
		-Pdirect-runner

direct-compile:
	docker-compose run beam mvn compile

## FLINK RUNNER
flink-compile:
	docker-compose run beam mvn package \
		-Pflink-runner \
		-DfinalName=$(APP)

flink-run:
	docker-compose exec jobmanager flink run \
		--class $(ENTRY_CLASS) /target/$(APP).jar \
		--runner=FlinkRunner

flink-full:
	@$(MAKE) flink-compile
	@$(MAKE) flink-run

down:
	docker-compose down