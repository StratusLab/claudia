#!/bin/sh
echo "------------------------------------------"
echo "Creando Clotho"
echo "------------------------------------------"


cd /home/dperez/Documentos/StratusLabs/git/claudia/clotho/

mvn clean
mvn assembly:assembly 

cd  /home/dperez/Documentos/StratusLabs/git/claudia/clotho/target/
cp -f /home/dperez/Documentos/StratusLabs/git/claudia/clotho/target/clotho-0.1.1-deployable.zip /home/dperez/Documentos/StratusLabs/Deb/deployable/clotho-0.2.1-deployable.zip  
cd /home/dperez/Documentos/StratusLabs/Deb/deployable/
rm -fr Clotho
unzip clotho-0.2.1-deployable.zip
cd Clotho
rm -fr /home/dperez/Documentos/StratusLabs/Deb/stratuslab-clotho/opt/claudia/*
cp -R * /home/dperez/Documentos/StratusLabs/Deb/stratuslab-clotho/opt/claudia/

cd /home/dperez/Documentos/StratusLabs/Deb
rm /home/dperez/Documentos/StratusLabs/Deb/stratuslab-clotho/opt/claudia/lib/ovf-manager-0.1-SNAPSHOT.jar

./compare.sh stratuslab-clotho/ stratuslab-claudia-lib/

dpkg-deb --build stratuslab-clotho
mv  stratuslab-clotho.deb finaldeb/stratuslab-clotho-0.2-5.deb


