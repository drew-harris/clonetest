# Humin Game Lab Minecraft Server

## Local Development Guide

Requirements:

- Docker and Docker Compose

Starting the container: `docker compose up --detach`

## How to use the container

### Interacting within the running container

Run `docker ps` to see the running container And then run
`docker attach CONTAINER ID HERE`

### Starting the minecraft server

`./start.sh`

### Stopping the minecraft server

Use CTRL + C when running `start.sh`

### Rebuilding the plugins

`./update.sh`
