#!/bin/sh
#!/bin/sh
echo "------------------------------------------"
echo "Creando paquetes RPM"
echo "------------------------------------------"
#sudo rm *.rpm
sudo alien -r -c finaldeb/*.deb
sudo mv *.rpm finalrpm/ 
