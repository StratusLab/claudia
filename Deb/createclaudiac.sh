#!/bin/sh
echo "------------------------------------------"
echo "Creando Claudia Client"
echo "------------------------------------------"


cd /home/dperez/Documentos/StratusLabs/git/claudia/claudia-client

mvn clean
mvn assembly:assembly 

cd  /home/dperez/Documentos/StratusLabs/git/claudia/claudia-client/target/
cp -f /home/dperez/Documentos/StratusLabs/git/claudia/claudia-client/target/claudia-client-0.1.1-deployable.zip /home/dperez/Documentos/StratusLabs/Deb/deployable/claudia-client-0.1.1-deployable.zip  
cd /home/dperez/Documentos/StratusLabs/Deb/deployable/
rm -fr ClaudiaClient
unzip claudia-client-0.1.1-deployable.zip
cd ClaudiaClient
rm -fr /home/dperez/Documentos/StratusLabs/Deb/stratuslab-claudia-client/opt/claudia/*
cp -R * /home/dperez/Documentos/StratusLabs/Deb/stratuslab-claudia-client/opt/claudia/

cd /home/dperez/Documentos/StratusLabs/Deb

./compare.sh stratuslab-claudia-client/ stratuslab-claudia-lib/

dpkg-deb --build stratuslab-claudia-client
mv  stratuslab-claudia-client.deb finaldeb/stratuslab-claudia-client-0.1-2.deb


