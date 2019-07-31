#! /bin/bash

clear

. ./setEnv.sh

echo "Starting Merchant Back Office with KumuluzEE. UberJar deployment. Port 8081"

echo "1 file involved:"
du -h $MERCHANTBO_JAR_FILE

#CMD="java -Xshareclasses:name=kumuluzee,verboseIO -jar $MERCHANTBO_JAR_FILE"
CMD="java -jar $MERCHANTBO_JAR_FILE"
echo "Running command:"
echo $CMD
echo "Strike any key when ready"
read

#$CMD &> merchantbo.log

$CMD
