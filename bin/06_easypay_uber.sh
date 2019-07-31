#! /bin/bash

clear

. ./setEnv.sh

EASYPAY_PORT=8080

echo "Starting EasyPay with Payara Micro Uber Jar. Port $EASYPAY_PORT"

echo "1 involved file:"
du -h easypay-uber.jar

echo "Running SmartBank on port $EASYPAY_PORT:"

CMD="java --add-opens java.base/jdk.internal.loader=ALL-UNNAMED 
-Xshareclasses:name=easypay-uber,verboseIO -Xscmx128m
-jar easypay-uber.jar"

echo "Running command:"
echo $CMD
echo "Strike any key when ready"
read

$CMD &> eaypay_uberjar.log
