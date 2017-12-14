#!/usr/bin/env bash

docker-compose stop &&
docker-compose rm -fv &&
docker-compose up -d mongo &&
sleep 1 &&
docker-compose run --rm mongo-seed &&
docker-compose up -d roller
