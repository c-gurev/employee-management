#!/bin/bash
cd ..
mvn clean package
docker compose up -d
./docker/init_oracle.sh
