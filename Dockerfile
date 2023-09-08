#################
#### PLUGINS ####
#################
FROM drewharris/mc-base AS plugins
WORKDIR /plugins

COPY ./pom.xml ./
COPY plugins/ ./plugins/
COPY libraries ./libraries

RUN mvn clean package


#################
#### SERVER #####
#################
FROM drewharris/mc-base

WORKDIR /server

ARG base_world_url=https://minecraft-hgl-drew.s3.amazonaws.com/
ARG world_file_name=backup.zip
ARG conveyor_url=https://minecraft-hgl-drew.s3.amazonaws.com/conveyor.zip
ARG spigot_url=https://minecraft-hgl-drew.s3.amazonaws.com/spigot.jar
ARG worldedit_url=https://minecraft-hgl-drew.s3.amazonaws.com/worldedit-bukkit-7.2.14.jar
ARG motd="The Humin Game Lab Minecraft Server"

# Install wget
RUN apt-get update && apt-get install -y wget

# Install git
RUN apt-get update && apt-get install -y git

# Install unzip
RUN apt-get update && apt-get install -y unzip

# Download spigot.jar
RUN wget -O ./spigot.jar $spigot_url

# RUN wget -O BuildTools.jar https://hub.spigotmc.org/jenkins/job/BuildTools/lastSuccessfulBuild/artifact/target/BuildTools.jar
#
# RUN java -jar BuildTools.jar

# rename the jar file to spigot.jar
# RUN mv spigot-*.jar spigot.jar

EXPOSE 25565/udp
EXPOSE 25565/tcp
EXPOSE 25575/tcp

RUN ["java", "-Xms1024M", "-Xmx2048M", "-Dfile.encoding=UTF-8", "-jar", "spigot.jar", "--world-dir", "./worlds", "nogui"]

# Change eula to true
RUN sed -i 's/false/true/g' eula.txt 

# Set the MOTD in server.properties
RUN sed -i "s/motd=A Minecraft Server/motd=${motd}/g" server.properties

# Disable online mode
RUN sed -i "s/online-mode=true/online-mode=false/g" server.properties

# Change to peaceful mode
RUN sed -i "s/difficulty=easy/difficulty=peaceful/g" server.properties


## PLUGIN SETUP ##
# Copy plugins from plugins stage
COPY --from=plugins /plugins/plugins/Recycler/target/Recycler-1.0-SNAPSHOT.jar ./plugins/Recycler.jar
COPY --from=plugins /plugins/plugins/SplitterNode/target/splitter-1.0-SNAPSHOT.jar ./plugins/SplitterNode.jar
COPY --from=plugins /plugins/plugins/contraption/target/Contraption-1.0-SNAPSHOT.jar ./plugins/Contraption.jar
COPY --from=plugins /plugins/plugins/HuMInGameLabsPlugin/target/HuMInLabPlugin-1.0-SNAPSHOT.jar ./plugins/HuMInGameLabsPlugin.jar

COPY ./spigot/spigot.yml ./

RUN wget -O ./plugins/ProtocolLib.jar "https://github.com/dmulloy2/ProtocolLib/releases/download/5.0.0/ProtocolLib.jar"

RUN wget -O ./plugins/worldedit.jar $worldedit_url ;

## Datapacks Setup
# Download conveyor datapack
RUN curl ${conveyor_url} --create-dirs -o ./worlds/world/datapacks/conveyor.zip

# Download world files
ADD "https://www.random.org/cgi-bin/randbyte?nbytes=10&format=h" skipcache
RUN curl ${base_world_url}${world_file_name} --create-dirs -o ./worlds/backup.zip

# Unzip world files
RUN unzip ./worlds/backup.zip -d ./worlds/


COPY ./scripts/entrypoint.sh .
RUN chmod +x entrypoint.sh

COPY ./static /static

ENTRYPOINT ["./entrypoint.sh"]

CMD ["java", "-Xms1024M", "-Xmx2048M", "-Dfile.encoding=UTF-8", "-jar", "spigot.jar", "--world-dir", "./worlds", "nogui"]
