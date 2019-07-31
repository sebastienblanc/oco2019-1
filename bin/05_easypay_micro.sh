#! /bin/bash

clear

. ./setEnv.sh

EASYPAY_PORT=8080

echo "Starting EasyPay with Payara Micro. Hollow jar deployment. Port $EASYPAY_PORT"

echo "2 involved files:"
du -h $EASYPAY_WAR_FILE
du -h $PAYARA_MICRO_HOME/payara-micro-5.192.jar

echo "Running SmartBank on port $EASYPAY_PORT:"

# -Xshareclasses:name=smartbank -Xquickstart -Xmx250m -Xms250m

CMD="java --add-opens java.base/jdk.internal.loader=ALL-UNNAMED 
-jar $PAYARA_MICRO_HOME/payara-micro-5.192.jar
--noCluster --addJars $HSQLDB_HOME/lib/hsqldb.jar --port $EASYPAY_PORT
--deploy $EASYPAY_WAR_FILE"

echo "Running command:"
echo $CMD

echo "Type any key when ready"
read

$CMD
