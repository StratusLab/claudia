#!/bin/sh
echo "------------------------------------------"
echo "Creating Clotho Package"
echo "------------------------------------------"


cd  $CLAUDIAWORKDIR/clotho/target/
cp -f $CLAUDIAWORKDIR/clotho/target/clotho-0.1.1-deployable.zip /home/dperez/Documentos/StratusLabs/Deb/deployable/clotho-0.2.1-deployable.zip  
cd $CLAUDIAWORKDIR/Deb/deployable/
rm -fr Clotho
unzip clotho-0.2.1-deployable.zip
cd Clotho
rm -fr $CLAUDIAWORKDIR/Deb/stratuslab-clotho/opt/claudia/*
cp -R * $CLAUDIAWORKDIR/Deb/stratuslab-clotho/opt/claudia/

cd $CLAUDIAWORKDIR/Deb
mv $CLAUDIAWORKDIR/Deb/stratuslab-clotho/opt/claudia/lib/* $CLAUDIAWORKDIR/Deb/stratuslab-claudia-lib/opt/claudia/lib/

dpkg-deb --build stratuslab-clotho
mv  stratuslab-clotho.deb $CLAUDIADEBDIR/stratuslab-clotho-0.2-5.deb


