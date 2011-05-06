#!/bin/bash

/etc/init.d/clothod stop
/etc/init.d/tcloudd stop
/etc/init.d/reportclientd stop
sleep 5
 rm -fr /opt/claudia/ClaudiaDB/
/etc/init.d/clothod start
/etc/init.d/tcloudd start
sleep 7
/etc/init.d/reportclientd start
