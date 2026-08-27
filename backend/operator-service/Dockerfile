FROM golang:1.23-alpine AS builder

WORKDIR /src
COPY go.mod ./
COPY . .
RUN go mod download && CGO_ENABLED=0 GOOS=linux go build -o /bin/operator ./cmd/server

FROM alpine:3.21

RUN apk add --no-cache ca-certificates wget
COPY --from=builder /bin/operator /operator

EXPOSE 8080

ENTRYPOINT ["/operator"]
