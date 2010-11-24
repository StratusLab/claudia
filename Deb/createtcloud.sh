#!/bin/sh
echo "------------------------------------------"
echo "Creating Tcloud-server Package"
echo "------------------------------------------"


cd  $CLAUDIAWORKDIR/tcloud-server/target/
rm -fr $CLAUDIAWORKDIR/Deb/stratuslab-tcloud-server/opt/claudia/*

mkdir -p $CLAUDIAWORKDIR/Deb/stratuslab-tcloud-server/opt/claudia/
cp -R $CLAUDIAWORKDIR/tcloud-server/target/tcloud-server-0.1.1-environment.dir/tcloud-server/* $CLAUDIAWORKDIR/Deb/stratuslab-tcloud-server/opt/claudia/

cd $CLAUDIAWORKDIR/Deb
mv $CLAUDIAWORKDIR/Deb/stratuslab-tcloud-server/opt/claudia/lib/* $CLAUDIAWORKDIR/Deb/stratuslab-claudia-lib/opt/claudia/lib/

dpkg-deb --build stratuslab-tcloud-server
mv  stratuslab-tcloud-server.deb $CLAUDIADEBDIR/stratuslab-tcloud-server-0.1-2.deb


