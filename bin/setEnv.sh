#! /bin/bash

EASYPAY_WAR_FILE=~/codeone/oco2019/easypay/target/easypay.war
SMARTBANK_WAR_FILE=~/codeone/oco2019/smartbank/target/smartbank.war
MERCHANTBO_JAR_FILE=~/codeone/oco2019/merchantbo/target/merchantbo-1.0.jar

export HSQLDB_HOME=~/javatools/hsqldb-2.5.0/hsqldb
export PAYARA_MICRO_HOME=~/javatools/payara-micro-5.192
export PAYARA_HOME=~/javatools/payara-5.192
export DATA_HOME=~/javadev/data/hsqldb

export PATH=$PAYARA_HOME/glassfish/bin:$PATH
export PATH=.:$PATH
