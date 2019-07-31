#! /bin/bash

. setEnv.sh

echo "Undeploying EasyPay application"
asadmin undeploy easypay

echo "Stopping Payara Server"
asadmin stop-domain oco
