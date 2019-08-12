#! /bin/bash

clear

. ./setEnv.sh

PORT=8082

echo "Starting SmartBank with Payara Micro. Hollow jar deployment. Port $PORT"

echo "2 involved files:"
du -h $SMARTBANK_WAR_FILE
du -h $PAYARA_MICRO_HOME/payara-micro-5.192.jar

echo "Running SmartBank on port $PORT:"

# -Xshareclasses:name=smartbank -Xquickstart -Xmx250m -Xms250m

CMD="/home/sblanc/jdk/jdk-11.0.4/bin/java --add-opens java.base/jdk.internal.loader=ALL-UNNAMED
-jar $PAYARA_MICRO_HOME/payara-micro-5.192.jar
--noCluster --addJars $HSQLDB_HOME/lib/hsqldb.jar --port $PORT
--deploy $SMARTBANK_WAR_FILE"

echo "Running command:"
echo $CMD

echo "Type any key when ready"
read

$CMD
