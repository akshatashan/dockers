package main

import (
	"context"
	"io/ioutil"
	"log"
	"net/http"
	"os"
	"time"

	"github.com/julienschmidt/httprouter"
	"github.com/segmentio/kafka-go"
)

// Default values if no environment variables were set
const (
	EntryPort    = "8080"
	KafkaAddress = "kafka:9093"
)

func writeToTopic(w http.ResponseWriter, r *http.Request, ps httprouter.Params) {

	var ok bool
	var rconn *kafka.Conn
	var kafkaAddress string

	if kafkaAddress, ok = os.LookupEnv("KAFKA_ADDRESS"); !ok {
		kafkaAddress = KafkaAddress
	}

	topic := ps.ByName("topic")
	partition := 0

	// Connect to Kafka topic.If the topic does not exist, it will be created.
	rconn, err := kafka.DialLeader(context.Background(), "tcp", kafkaAddress, topic, partition)
	if err != nil {
		panic(err)
	}

	body, err := ioutil.ReadAll(r.Body)
	//log.Printf("Body %s, topic %s", body, topic)
	if err != nil {
		panic(err)
	}

	//log.Printf("Writing to kafka topic %s and %s", kafkaAddress, topic)
	rconn.SetWriteDeadline(time.Now().Add(10 * time.Second))
	if _, err := rconn.WriteMessages(
		kafka.Message{Value: body},
	); err != nil {
		log.Printf("Could not write message %s", err)
	}
}

func main() {
	var entryPort string
	var ok bool
	if entryPort, ok = os.LookupEnv("ENTRY_PORT"); !ok {
		entryPort = EntryPort
	}

	log.Printf("Listening on port %s ", entryPort)

	router := httprouter.New()

	//usage: /entry/input - to write to input topic.
	router.POST("/entry/:topic", writeToTopic)

	log.Fatal(http.ListenAndServe(":"+entryPort, router))
}
