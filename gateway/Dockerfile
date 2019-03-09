FROM golang:1.11

WORKDIR /go/src/github.com/gateway
ADD main.go /go/src/github.com/main.go
RUN go get -u github.com/segmentio/kafka-go
RUN go get -u github.com/julienschmidt/httprouter

CMD ["go", "run", "/go/src/github.com/main.go"]