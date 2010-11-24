#!/bin/sh
echo "------------------------------------------"
echo "Creating Claudia Client Package"
echo "------------------------------------------"

cd  $CLAUDIAWORKDIR/claudia-client/target/
rm -fr $CLAUDIAWORKDIR/Deb/stratuslab-claudia-client/opt/claudia/*

mkdir -p $CLAUDIAWORKDIR/Deb/stratuslab-claudia-client/opt/claudia/
cp -R $CLAUDIAWORKDIR/claudia-client/target/claudia-client-0.1.1-environment.dir/ClaudiaClient/* $CLAUDIAWORKDIR/Deb/stratuslab-claudia-client/opt/claudia/

cd $CLAUDIAWORKDIR/Deb
mv $CLAUDIAWORKDIR/Deb/stratuslab-claudia-client/opt/claudia/lib/* $CLAUDIAWORKDIR/Deb/stratuslab-claudia-lib/opt/claudia/lib/

dpkg-deb --build stratuslab-claudia-client
mv  stratuslab-claudia-client.deb $CLAUDIADEBDIR/stratuslab-claudia-client-0.1-2.deb


