#!/bin/sh

rm -fr stratuslab-claudia-lib/opt/claudia/lib/*

./createclotho.sh
./createtcloud.sh
./createclaudiac.sh

#./createrpm.sh

dpkg-deb --build stratuslab-claudia-lib
dpkg-deb --build stratuslab-claudia

mv  stratuslab-claudia-lib.deb  $CLAUDIADEBDIR/stratuslab-claudia-lib-0.1-3.deb
mv  stratuslab-claudia.deb $CLAUDIADEBDIR/stratuslab-claudia-0.1-2.deb


rm -fr stratuslab-clotho/opt
rm -fr stratuslab-claudia-lib/opt/claudia/lib/*
rm -fr stratuslab-claudia-client/opt
rm -fr stratuslab-tcloud-server/opt
