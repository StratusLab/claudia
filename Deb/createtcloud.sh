#!/bin/sh
echo "------------------------------------------"
echo "Creando Tcloud server"
echo "------------------------------------------"


cd /home/dperez/Documentos/StratusLabs/git/claudia/tcloud-server/

mvn clean
mvn assembly:assembly 

cd  /home/dperez/Documentos/StratusLabs/git/claudia/tcloud-server/target/
cp -f /home/dperez/Documentos/StratusLabs/git/claudia/tcloud-server/target/tcloud-server-0.1.1-deployable.zip /home/dperez/Documentos/StratusLabs/Deb/deployable/tcloud-server-0.2.1-deployable.zip  
cd /home/dperez/Documentos/StratusLabs/Deb/deployable/
rm -fr tcloud-server
unzip tcloud-server-0.2.1-deployable.zip
cd tcloud-server
rm -fr /home/dperez/Documentos/StratusLabs/Deb/stratuslab-tcloud-server/opt/claudia/*
cp -R * /home/dperez/Documentos/StratusLabs/Deb/stratuslab-tcloud-server/opt/claudia/

cd /home/dperez/Documentos/StratusLabs/Deb
rm /home/dperez/Documentos/StratusLabs/Deb/stratuslab-tcloud-server/opt/claudia/lib/ovf-manager-0.1-SNAPSHOT.jar

./compare.sh stratuslab-tcloud-server/ stratuslab-claudia-lib/

dpkg-deb --build stratuslab-tcloud-server
mv  stratuslab-tcloud-server.deb finaldeb/stratuslab-tcloud-server-0.1-2.deb


