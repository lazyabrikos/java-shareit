mvn clean install
docker rm -f shareit-gateway shareit-server postgres
docker image rm shareit-gateway
docker image rm shareit-server
docker compose up -d
