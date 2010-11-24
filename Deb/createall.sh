#!/bin/sh

rm -fr stratuslab-claudia-lib/opt/claudia/lib/*

#dpkg-deb --build stratuslab-jms2rest
#dpkg-deb --build stratuslab-ovf-manager
#dpkg-deb --build stratuslab-wasup
#dpkg-deb --build stratuslab-ezweb
#dpkg-deb --build stratuslab-claudia

#mv  stratuslab-claudia-lib.deb finaldeb/stratuslab-claudia-lib-0.1-3.deb
#mv  stratuslab-jms2rest.deb finaldeb/stratuslab-jms2rest-0.1-1.deb
#mv  stratuslab-ovf-manager.deb finaldeb/stratuslab-ovf-manager-0.1-1.deb
#mv  stratuslab-wasup.deb finaldeb/stratuslab-wasup-0.1-1.deb
#mv  stratuslab-ezweb.deb finaldeb/stratuslab-ezweb-0.1-1.deb
#mv  stratuslab-claudia.deb finaldeb/stratuslab-claudia-0.1-2.deb

#./createdrivers.sh
./createclotho.sh
#./createacd.sh
#./createtcloud.sh
#./createclaudiac.sh

#./createrpm.sh

dpkg-deb --build stratuslab-claudia-lib


rm -fr stratuslab-clotho/opt
rm -fr stratuslab-claudia-lib/opt/claudia/lib/*
