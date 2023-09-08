#!/bin/bash

echo "Rebuilding All Plugins"

cd /plugins
CLICOLOR_FORCE=1

if [[ $1 = "nocache" ]]
then
    echo "Rebuilding without cache..."
    mvn clean package -T 4 -Dmaven.build.cache.enabled=false
else
    mvn clean package -T 4
fi

cp /plugins/plugins/Recycler/target/Recycler-1.0-SNAPSHOT.jar /server/plugins
cp /plugins/plugins/SplitterNode/target/splitter-1.0-SNAPSHOT.jar /server/plugins
cp /plugins/plugins/HuMInGameLabsPlugin/target/HuMInLabPlugin-1.0-SNAPSHOT.jar /server/plugins
cp /plugins/plugins/contraption/target/Contraption-1.0-SNAPSHOT.jar /server/plugins
