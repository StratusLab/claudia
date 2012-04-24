#!/bin/bash

## Change the next line if the property files is any other file
PLACEMENT_FILE=config/placement.properties

function help() {
   echo "
  Edit the file placement.properties and set the url, user and password 
properties the correct values. This script, will create a Mysql database 
according to the echo parameters in that file.
This script is intended to be executed just once before using the 
Placement service or after any of the jdbc properties in the configuration
file are modified for any reason.

usage: PrepareDatabase.sh <RootPassword>

example: PrepareDatabase.sh root"
}

[ $1 == "--help" ] && help && exit 0

cd $(dirname $0)/..

rootUser=root
rootPassword=$1

echo $rootUser
echo $rootPassword


#Get the user from the file.
user=`awk 'BEGIN {FS="="} /placement.jdbc.user/ {print $2}' $PLACEMENT_FILE`
password=`awk 'BEGIN {FS="="} /placement.jdbc.password/ {print $2}'  $PLACEMENT_FILE`
url=`awk 'BEGIN {FS="="} /placement.jdbc.url/ {print $2}'  $PLACEMENT_FILE`

### Parse the URL
host_port=`echo $url | sed 's.^jdbc:mysql://..g' | awk 'BEGIN {FS="/"} {print $1}'`
param_db=`echo $url | sed 's.^jdbc:mysql://..g' | awk 'BEGIN {FS="/"} {print $2}'`
host=`echo $host_port | awk 'BEGIN {FS=":"} {print $1}'`
port=`echo $host_port | awk 'BEGIN {FS=":"} {print $2}'`
database=`echo $param_db | awk 'BEGIN {FS="&"} {print $1}'`

mysqlcommand="`type -p mysql` -u $rootUser -h$host"
echo "$mysqlcommand"
[ "x"$rootPassword != "x" ] && mysqlcommand=`echo $mysqlcommand -p$rootPassword`
[ "x"$port != "x" ] && mysqlcommand=`echo $mysqlcommand -P$port`

echo "$mysqlcommand"

$mysqlcommand << EOT
create database $database;
grant all on $database.* to $user@'$host' identified by '$password';
flush privileges;
EOT

err=$?
[ $err -ne 0 ] && help

