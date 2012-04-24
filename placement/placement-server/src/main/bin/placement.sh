#!/bin/bash

cd $(dirname $0)/..

CLASSPATH=$PWD/config
for a in `ls lib/*[jw]ar`; do CLASSPATH=$CLASSPATH:$a; done
export CLASSPATH

java  -Djava.util.logging.config.file=config/logging.properties com.telefonica.euro_iaas.placement.server.PlacementJettyServer config/placement.properties
