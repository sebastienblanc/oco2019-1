#! /bin/bash

clear

. ./setEnv.sh

echo "Starting EasyPay with Payara Server. Thin war deployment"

echo "War file :"
du -h $EASYPAY_WAR_FILE

cat /dev/null > $PAYARA_HOME/glassfish/domains/oco/logs/server.log
echo "Deploying on Payara Server Full 5.192:"
du -sh $PAYARA_HOME

echo "Starting Payara Server on ports 4848 (admin) and 8080 (apps)"

START_SERVER_CMD="asadmin start-domain oco"

echo "Running command"
echo $START_SERVER_CMD
echo "Strike any key when ready"
read
time $START_SERVER_CMD

echo "Deploying EasyPay War file"
DEPLOY_APP_CMD="asadmin deploy $EASYPAY_WAR_FILE"
echo "Running command"
echo $DEPLOY_APP_CMD
echo "Strike any key when ready"
read
time $DEPLOY_APP_CMD
