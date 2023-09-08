#!/bin/bash

if [ ! -f "./plugins/HuMInGameLabsPlugin.jar" ]; then
    ./build.sh;
fi

exec "$@"
