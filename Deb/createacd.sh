#!/bin/sh
echo "------------------------------------------"
echo "Creando acd broker"
echo "------------------------------------------"


cd /home/dperez/Documentos/StratusLabs/git/claudia/acd-server/

mvn clean

mvn  -Dmaven.test.skip=true  assembly:assembly

cd  /home/dperez/Documentos/StratusLabs/git/claudia/acd-server/target/
cp -f /home/dperez/Documentos/StratusLabs/git/claudia/acd-server/target/acd-broker-0.1-SNAPSHOT-deployable.zip /home/dperez/Documentos/StratusLabs/Deb/deployable/acd-broker-0.1-SNAPSHOT-deployable.zip  
cd /home/dperez/Documentos/StratusLabs/Deb/deployable/
rm -fr ACDBroker
unzip acd-broker-0.1-SNAPSHOT-deployable.zip
cd ACDBroker
rm -fr /home/dperez/Documentos/StratusLabs/Deb/stratuslab-acd-broker/opt/claudia/*
cp -R * /home/dperez/Documentos/StratusLabs/Deb/stratuslab-acd-broker/opt/claudia/

cd /home/dperez/Documentos/StratusLabs/Deb

./compare.sh stratuslab-acd-broker/ stratuslab-claudia-lib/

dpkg-deb --build stratuslab-acd-broker
mv  stratuslab-acd-broker.deb finaldeb/stratuslab-acd-broker-0.1-3.deb


