#!/bin/bash

if [[ -n "$MAX_PLAYERS" ]]; then
    sed -i "s|max-players=20|max-players=$MAX_PLAYERS|g" server.properties
fi

if [[ -n "$MOTD" ]]; then
    sed -i "s|^.*motd.*$|motd=$MOTD|g" server.properties
fi

exec "$@"
