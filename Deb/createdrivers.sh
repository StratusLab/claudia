#!/bin/sh
echo "------------------------------------------"
echo "Creando driver one"
echo "------------------------------------------"
cd /home/dperez/Documentos/StratusLabs/git/claudia/driver-one/
cp  /home/dperez/Documentos/StratusLabs/git/claudia/driver-one/target/driver-one-0.1-1-deployable.zip /home/dperez/Documentos/StratusLabs/Deb/stratuslab-driver-one/opt/claudia/driver/driver-one-0.1-1-deployable.zip
cp /home/dperez/Documentos/StratusLabs/git/claudia/driver-one/target/driver-one-0.1-1-deployable.zip /home/dperez/Documentos/StratusLabs/Deb/deployable/driver-one-0.1-1-deployable.zip
cd /home/dperez/Documentos/StratusLabs/Deb 
dpkg-deb --build stratuslab-driver-one
mv  stratuslab-driver-one.deb finaldeb/stratuslab-driver-one-0.1-5.deb



echo "------------------------------------------"
echo "Creando driver wasup"
echo "------------------------------------------"
cd /home/dperez/Documentos/StratusLabs/git/claudia/driver-wasup/
cp  /home/dperez/Documentos/StratusLabs/git/claudia/driver-wasup/target/driver-wasup-0.1.1-deployable.zip /home/dperez/Documentos/StratusLabs/Deb/stratuslab-driver-wasup/opt/claudia/driver/driver-wasup-0.1.1-deployable.zip
cp /home/dperez/Documentos/StratusLabs/git/claudia/driver-wasup/target/driver-wasup-0.1.1-deployable.zip /home/dperez/Documentos/StratusLabs/Deb/deployable/driver-wasup-0.1.1-deployable.zip
cd /home/dperez/Documentos/StratusLabs/Deb
dpkg-deb --build stratuslab-driver-wasup
mv  stratuslab-driver-wasup.deb finaldeb/stratuslab-driver-wasup-0.1-1.deb


echo "------------------------------------------"
echo "Creando driver clotho"
echo "------------------------------------------"
cd /home/dperez/Documentos/StratusLabs/git/claudia/driver-clotho/
cp  /home/dperez/Documentos/StratusLabs/git/claudia/driver-clotho/target/driver-clotho-0.1.1-deployable.zip /home/dperez/Documentos/StratusLabs/Deb/stratuslab-driver-clotho/opt/claudia/driver/driver-clotho-0.1.1-deployable.zip
cp /home/dperez/Documentos/StratusLabs/git/claudia/driver-clotho/target/driver-clotho-0.1.1-deployable.zip /home/dperez/Documentos/StratusLabs/Deb/deployable/driver-clotho-0.1.1-deployable.zip
cd /home/dperez/Documentos/StratusLabs/Deb
dpkg-deb --build stratuslab-driver-clotho
mv  stratuslab-driver-clotho.deb finaldeb/stratuslab-driver-clotho-0.1-3.deb

