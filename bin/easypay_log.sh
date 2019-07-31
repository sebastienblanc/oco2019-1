#! /bin/bash

. setEnv.sh

tail -f  $PAYARA_HOME/glassfish/domains/oco/logs/server.log
