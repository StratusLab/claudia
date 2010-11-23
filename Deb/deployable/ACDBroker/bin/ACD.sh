#!/bin/bash

###################################################################
## ACDServer Daemon
###################################################################

# Constant declarations
ERROR_USAGE=1
ERROR_JAVA_NOT_FOUND=2

# Depending on the arguments start, or stop the server
if [ $# -ne 1 ]; then

	echo -e "Starts or stop the ACDServer daemon: \n";
	echo -e "\t\t$0 {start|stop}\n";

	exit $ERROR_USAGE;
fi

if [ $1 == "start" ]; then

	# Check that a Java Runtime Environment is in the PATH
	WICH_RESULT=$(which java);
	if [ $? == 1 ]; then
		
		echo -e "Java command not found. Check there is a Java Runtime Environment present, and the executable is in the PATH\n";

		exit $ERROR_JAVA_NOT_FOUND;
	fi

	# Run the Server class
	cd "`dirname $0`";
	cd ..;

	echo "Initiating ACDServer";
	java -Djava.security.policy=conf/server.policy -jar lib/acd-broker-0.1-SNAPSHOT.jar;
	
	echo "Shutting down ACDServer";

elif [ $1 == "stop" ]; then 

	# TODO: too hard way to kill a server. Pending solution.
	ps -ef |grep acd-broker-0.1-SNAPSHOT.jar | grep -v grep |awk '{print $2}'|xargs -I '{}' kill -9 '{}'
	echo "Stopping ACDServer, please, wait";

fi 



