#! /bin/bash

clear

. ./setEnv.sh

echo "Buildling EasyPay UberJar with Payara Micro:"

PORT=8080

CMD="java --add-opens java.base/jdk.internal.loader=ALL-UNNAMED 
-jar $PAYARA_MICRO_HOME/payara-micro-5.192.jar
--noCluster --addJars $HSQLDB_HOME/lib/hsqldb.jar
--port $PORT
--deploy $EASYPAY_WAR_FILE
--outputUberJar easypay-uber.jar"

echo "Running command "
echo $CMD

echo "Strike any key when ready"
read

$CMD
