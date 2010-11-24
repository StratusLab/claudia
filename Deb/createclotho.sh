#!/bin/sh
echo "------------------------------------------"
echo "Creating Clotho Package"
echo "------------------------------------------"


cd  $CLAUDIAWORKDIR/clotho/target/
rm -fr $CLAUDIAWORKDIR/Deb/stratuslab-clotho/opt/claudia/*

mkdir -p $CLAUDIAWORKDIR/Deb/stratuslab-clotho/opt/claudia/
ls  $CLAUDIAWORKDIR/clotho/target/
cp -R $CLAUDIAWORKDIR/clotho/target/clotho-0.1.1-environment/clotho/* $CLAUDIAWORKDIR/Deb/stratuslab-clotho/opt/claudia/

cd $CLAUDIAWORKDIR/Deb
mv $CLAUDIAWORKDIR/Deb/stratuslab-clotho/opt/claudia/lib/* $CLAUDIAWORKDIR/Deb/stratuslab-claudia-lib/opt/claudia/lib/

dpkg-deb --build stratuslab-clotho
mv  stratuslab-clotho.deb $CLAUDIADEBDIR/stratuslab-clotho-0.2-5.deb


