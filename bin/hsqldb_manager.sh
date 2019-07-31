#! /bin/bash

. setEnv.sh

java -classpath $HSQLDB_HOME/lib/hsqldb.jar org.hsqldb.util.DatabaseManager &
