#!/bin/sh


DEST=$1

ssh  root@$DEST 'yum install stratuslab-clotho'
ssh  root@$DEST 'yum install stratuslab-tcloud-server'
ssh  root@$DEST 'yum install stratuslab-claudia-client'


