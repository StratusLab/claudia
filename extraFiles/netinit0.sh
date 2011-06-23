source /mnt/stratuslab/context.sh
#hostname 
hostname $HOSTNAME
#network interfaces
ifconfig eth0 $IP_ETH0
#additinal dns
echo "nameserver $DNS_ETH0" >> /etc/resolv.conf
#add new gateway route
route add default gw $GATEWAY_ETH0
