#!/bin/sh

#
# Author: Telefonica I+D 2012.
#
# This script is passed to the virtual machines created and installs the
# monitoring tools from a repository. It installs a key in the authorized
# keys:
# - Creates a new yum repository
# - Installs the collectd and those tools from that repo.
# - Configures the collectd conf files with the hosts name in the DB.
# - Configures the daemons to be initialized at boot
# - Starts the daemons required to work.

. /mnt/stratuslab/context.sh

echo "" >> /root/.ssh/authorized_keys
echo "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQCk5YREWI6oZmyToCTKEb02I2ZM/Hspy/9aONIHxXUOD8nesX8UFVyNetIvqXoIXo1/wmjQisWbcIIpG+D4x7rwfU8Zx8LMBwNLXUpacvIhHDzh6PGgbH5EtlecQ3/vD7vfY5xsnMpw386c/bACpvLA5zcfXUAeJW89knCxqXtyGD+CFu/bSbJDwvCViybHuckoQggAVSnsiNWD3gwZ8wtUEUo3o1Wm9bk+dFRyn22QlqP69wLF1uyvRFeLT0n8rfj68BY69VH8XYZJkr4IdYuTW/kD99oV1HK6YDSBIESWdO1mdoEMlM3hRoHxYadP1a8Wj12Nv7aRZneUYFkz9m/B root@fedora14" >> /root/.ssh/authorized_keys


# Calculo el hostname a partir del md5sum del FQN que extraigo como puedo del contexto.
FQN=$(basename $(echo $FILES | awk 'BEGIN {FS=".vees."} ; {print $1}'))
HOSTNAME=$(echo -n $FQN | md5sum | awk '{print $1}')

###
### Configuramos el repositorio para la instalacion de las sondas.
### 

[ ! -f /etc/yum.repos.d/monitoring-service.repo ] && cat << EOT > /etc/yum.repos.d/monitoring-service.repo
[monitoring-service]
name=monitoring-service
baseurl=http://62.217.120.136/fedora14/monitoring
gpgcheck=0
enabled=1
EOT

###
### Iniciamos la instalacion: Paquetes...
###
yum install -y collectd-base collectd-basicprobes collectd-extendedprobes-rpm

###
### Paramos el colector para reconfigurar....
###
service collectd stop
service memcached stop

###
### Reconfiguramos y borramos el fichero auxiliar.
### 
mv /opt/monitoring/conf/monitoring.properties /opt/monitoring/conf/monitoring.properties.inst

sed -e 's|^[ \t]*mysqlurl[ \t]*=.*$|mysqlurl=jdbc\\:mysql\\://telefonica-1\\:3306/monitoring|g' \
    -e 's|^[ \t]*mysqluser[ \t]*=.*$|mysqluser=claudia|g' \
    -e 's|^[ \t]*mysqlpassword[ \t]*=.*$|mysqlpassword=ClaudiaPass|g' \
    /opt/monitoring/conf/monitoring.properties.inst > /opt/monitoring/conf/monitoring.properties

rm -f /opt/monitoring/conf/monitoring.properties.inst


### Ponemos los datos del servidor collectd:
cat << EOT > /opt/collectd/etc/conf.d/network-client.conf
Loadplugin network
<Plugin network>
        <Server "telefonica-1" "25826">
                Interface "eth0"
        </Server>
</Plugin>

EOT

echo "Hostname    \"$HOSTNAME\""   > /opt/collectd/etc/conf.d/host.conf
###
### Arrancamos de nuevo el demonio.
###
chkconfig memcached on

service memcached start
service collectd start
