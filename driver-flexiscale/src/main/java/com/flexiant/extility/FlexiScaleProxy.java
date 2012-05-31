package com.flexiant.extility;

public class FlexiScaleProxy implements com.flexiant.extility.FlexiScale {
  private String _endpoint = null;
  private com.flexiant.extility.FlexiScale flexiScale = null;
  
  public FlexiScaleProxy() {
    _initFlexiScaleProxy();
  }
  
  public FlexiScaleProxy(String endpoint) {
    _endpoint = endpoint;
    _initFlexiScaleProxy();
  }
  
  private void _initFlexiScaleProxy() {
    try {
      flexiScale = (new com.flexiant.extility.FlexiScaleServiceLocator()).getFlexiScale();
      if (flexiScale != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)flexiScale)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)flexiScale)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (flexiScale != null)
      ((javax.xml.rpc.Stub)flexiScale)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.flexiant.extility.FlexiScale getFlexiScale() {
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale;
  }
  
  public com.flexiant.extility.ProductOffer[] listProductOffers(int type_id) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.listProductOffers(type_id);
  }
  
  public com.flexiant.extility.Server[] listServers(long vdc_id) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.listServers(vdc_id);
  }
  
  public com.flexiant.extility.Server[] listServersForImage(long image_id) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.listServersForImage(image_id);
  }
  
  public com.flexiant.extility.Job[] listJobs() throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.listJobs();
  }
  
  public com.flexiant.extility.Job getJob(long job_id) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.getJob(job_id);
  }
  
  public int cancelJob(long job_id) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.cancelJob(job_id);
  }
  
  public com.flexiant.extility.FirewallRule[] listFirewallRules(long firewall_id, java.lang.String direction) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.listFirewallRules(firewall_id, direction);
  }
  
  public com.flexiant.extility.Firewall[] listFirewalls(long vdc_id) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.listFirewalls(vdc_id);
  }
  
  public com.flexiant.extility.Subnet[] listSubnets(long vlan_id) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.listSubnets(vlan_id);
  }
  
  public com.flexiant.extility.Subnet getSubnet(long subnet_id) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.getSubnet(subnet_id);
  }
  
  public com.flexiant.extility.FirewallProtocol[] listFirewallProtocols() throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.listFirewallProtocols();
  }
  
  public com.flexiant.extility.IcmpProtocol[] listIcmpProtocols() throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.listIcmpProtocols();
  }
  
  public com.flexiant.extility.FilterJobList filterJobs(long from, long to, java.lang.String order_by, java.lang.String direction) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.filterJobs(from, to, order_by, direction);
  }
  
  public com.flexiant.extility.Job[] listRunningJobs() throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.listRunningJobs();
  }
  
  public com.flexiant.extility.ImageTemplate[] listImageTemplates() throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.listImageTemplates();
  }
  
  public com.flexiant.extility.Disk[] listDisks(long vdc_id) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.listDisks(vdc_id);
  }
  
  public com.flexiant.extility.Disk[] listFreeDisks(long vdc_id) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.listFreeDisks(vdc_id);
  }
  
  public com.flexiant.extility.Disk getDisk(long disk_id) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.getDisk(disk_id);
  }
  
  public com.flexiant.extility.Disk moveDisk(long disk_id, long vdc_id) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.moveDisk(disk_id, vdc_id);
  }
  
  public int addIpAddressToNic(long nic_id, java.lang.String ip_address, boolean dhcp) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.addIpAddressToNic(nic_id, ip_address, dhcp);
  }
  
  public int removeIpAddressFromNic(long nic_id, java.lang.String ip_address) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.removeIpAddressFromNic(nic_id, ip_address);
  }
  
  public com.flexiant.extility.IpAddress[] listIpAddressesForNic(long nic_id) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.listIpAddressesForNic(nic_id);
  }
  
  public com.flexiant.extility.Vlan createVLAN(long vdc_id, boolean _private) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.createVLAN(vdc_id, _private);
  }
  
  public int deleteVLAN(long vlan_id) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.deleteVLAN(vlan_id);
  }
  
  public com.flexiant.extility.NetworkInterface[] listNicsForVLAN(long vlan_id) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.listNicsForVLAN(vlan_id);
  }
  
  public com.flexiant.extility.NetworkInterface getNetworkInterface(long nic_id) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.getNetworkInterface(nic_id);
  }
  
  public com.flexiant.extility.Vlan[] listVLANs(long vdc_id) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.listVLANs(vdc_id);
  }
  
  public com.flexiant.extility.Vlan getVLAN(long vlan_id) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.getVLAN(vlan_id);
  }
  
  public com.flexiant.extility.VDC createVDC(java.lang.String vdc_name) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.createVDC(vdc_name);
  }
  
  public int deleteVDC(long vdc_id, boolean delete_all) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.deleteVDC(vdc_id, delete_all);
  }
  
  public com.flexiant.extility.VDC[] listVDCs() throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.listVDCs();
  }
  
  public com.flexiant.extility.VDC getVDC(long vdc_id) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.getVDC(vdc_id);
  }
  
  public int moveSubnet(long subnet_id, long new_vlan_id) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.moveSubnet(subnet_id, new_vlan_id);
  }
  
  public com.flexiant.extility.Subnet attachSubnet(long vlan_id) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.attachSubnet(vlan_id);
  }
  
  public int removeSubnet(long subnet_id) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.removeSubnet(subnet_id);
  }
  
  public long addNetworkInterface(long server_id, long vlan_id, int index) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.addNetworkInterface(server_id, vlan_id, index);
  }
  
  public com.flexiant.extility.Server getServer(long server_id) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.getServer(server_id);
  }
  
  public long startServer(long server_id, com.flexiant.extility.RuntimeMetadata runtime_metadata) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.startServer(server_id, runtime_metadata);
  }
  
  public long stopServer(long server_id, int stop_method) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.stopServer(server_id, stop_method);
  }
  
  public long rebootServer(long server_id) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.rebootServer(server_id);
  }
  
  public int waitForJob(long job_id) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.waitForJob(job_id);
  }
  
  public int isJobRunning(long job_id) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.isJobRunning(job_id);
  }
  
  public int destroyServer(long server_id) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.destroyServer(server_id);
  }
  
  public com.flexiant.extility.Disk createDisk(long vdc_id, java.lang.String name, long disk_productoffer_id, long image_id) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.createDisk(vdc_id, name, disk_productoffer_id, image_id);
  }
  
  public int deleteDisk(long disk_id) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.deleteDisk(disk_id);
  }
  
  public int revertDisk(long snapshot_id) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.revertDisk(snapshot_id);
  }
  
  public int attachDisk(long server_id, long disk_id, int index) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.attachDisk(server_id, disk_id, index);
  }
  
  public int detachDisk(long server_id, long disk_id) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.detachDisk(server_id, disk_id);
  }
  
  public com.flexiant.extility.DiskSnapshot createDiskSnapshot(long disk_id, java.lang.String name, int force) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.createDiskSnapshot(disk_id, name, force);
  }
  
  public int deleteDiskSnapshot(long snapshot_id) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.deleteDiskSnapshot(snapshot_id);
  }
  
  public com.flexiant.extility.DiskSnapshot[] listDiskSnapshots(long disk_id) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.listDiskSnapshots(disk_id);
  }
  
  public com.flexiant.extility.DiskSnapshot getDiskSnapshot(long snapshot_id) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.getDiskSnapshot(snapshot_id);
  }
  
  public com.flexiant.extility.DiskSnapshot[] listAllDiskSnapshots(long vdc_id) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.listAllDiskSnapshots(vdc_id);
  }
  
  public com.flexiant.extility.Disk cloneDisk(long snapshot_id, java.lang.String new_disk_name, long vdc_id) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.cloneDisk(snapshot_id, new_disk_name, vdc_id);
  }
  
  public com.flexiant.extility.Disk[] listClonedDisks(long snapshot_id) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.listClonedDisks(snapshot_id);
  }
  
  public int deleteImageTemplate(long image_template_id) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.deleteImageTemplate(image_template_id);
  }
  
  public com.flexiant.extility.Server createServer(long vdc_id, long vlan_id, java.lang.String name, long image_id, long server_productoffer_id, long[] disk_productoffers) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.createServer(vdc_id, vlan_id, name, image_id, server_productoffer_id, disk_productoffers);
  }
  
  public com.flexiant.extility.VNCAccess getVNCConnection(long server_id, java.lang.String access_network) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.getVNCConnection(server_id, access_network);
  }
  
  public com.flexiant.extility.Firewall createFirewall(java.lang.String ip_address, java.lang.String default_inbound_policy, java.lang.String default_outbound_policy, long vdc_id) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.createFirewall(ip_address, default_inbound_policy, default_outbound_policy, vdc_id);
  }
  
  public com.flexiant.extility.FirewallRule createFirewallRule(com.flexiant.extility.FirewallRule specification, int index) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.createFirewallRule(specification, index);
  }
  
  public com.flexiant.extility.Server modifyServer(long server_id, java.lang.String name, long bootable_disk_id, long server_productoffer_id) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.modifyServer(server_id, name, bootable_disk_id, server_productoffer_id);
  }
  
  public com.flexiant.extility.ImageTemplate modifyImageTemplate(long image_id, java.lang.String name, java.lang.String default_username, boolean generate_password) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.modifyImageTemplate(image_id, name, default_username, generate_password);
  }
  
  public com.flexiant.extility.Disk modifyDisk(long disk_id, java.lang.String name, long disk_productoffer_id) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.modifyDisk(disk_id, name, disk_productoffer_id);
  }
  
  public com.flexiant.extility.Firewall modifyFirewall(long firewall_id, java.lang.String new_default_in_policy, java.lang.String new_default_out_policy) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.modifyFirewall(firewall_id, new_default_in_policy, new_default_out_policy);
  }
  
  public com.flexiant.extility.FirewallRule modifyFirewallRule(com.flexiant.extility.FirewallRule specification) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.modifyFirewallRule(specification);
  }
  
  public int deleteFirewall(long firewall_id) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.deleteFirewall(firewall_id);
  }
  
  public int deleteFirewallRule(int firewall_rule_id) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.deleteFirewallRule(firewall_rule_id);
  }
  
  public int removeNetworkInterface(long nic_id) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.removeNetworkInterface(nic_id);
  }
  
  public com.flexiant.extility.ImageTemplate createImageTemplate(long disk_id, java.lang.String name, java.lang.String default_username, boolean generate_password) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.createImageTemplate(disk_id, name, default_username, generate_password);
  }
  
  public long fetchDisk(java.lang.String url, java.lang.String checksum, java.lang.String conn_user, java.lang.String conn_pass, java.lang.String name, long disk_productoffer_id, long vdc_id, boolean makeImage, java.lang.String image_defaultusername) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.fetchDisk(url, checksum, conn_user, conn_pass, name, disk_productoffer_id, vdc_id, makeImage, image_defaultusername);
  }
  
  public com.flexiant.extility.VDC renameVDC(long vdc_id, java.lang.String name) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.renameVDC(vdc_id, name);
  }
  
  public long createServerFromRemoteDisk(com.flexiant.extility.RemoteDisk[] remote_disks, java.lang.String server_name, java.lang.String initial_user, java.lang.String initial_password, long vdc_id, long vlan_id, long server_product_offer) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.createServerFromRemoteDisk(remote_disks, server_name, initial_user, initial_password, vdc_id, vlan_id, server_product_offer);
  }
  
  public long migrateServer(com.flexiant.extility.RemoteDisk[] remote_disks, java.lang.String server_name, java.lang.String initial_user, java.lang.String initial_password, long vdc_id, long vlan_id, long server_product_offer) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.migrateServer(remote_disks, server_name, initial_user, initial_password, vdc_id, vlan_id, server_product_offer);
  }
  
  public long createScheduledServerJob(long server_id, int type, java.util.Calendar start_time, com.flexiant.extility.RuntimeMetadata runtime_metadata) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.createScheduledServerJob(server_id, type, start_time, runtime_metadata);
  }
  
  public com.flexiant.extility.CustomerMetadata getCustomerMetadata() throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.getCustomerMetadata();
  }
  
  public com.flexiant.extility.ServerMetadata getServerMetadata(long server_id) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.getServerMetadata(server_id);
  }
  
  public com.flexiant.extility.ImageTemplateMetadata getImageTemplateMetadata(long image_id) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.getImageTemplateMetadata(image_id);
  }
  
  public com.flexiant.extility.CustomerMetadata setCustomerMetadata(com.flexiant.extility.CustomerMetadata metadata) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.setCustomerMetadata(metadata);
  }
  
  public com.flexiant.extility.ServerMetadata setServerMetadata(com.flexiant.extility.ServerMetadata metadata) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.setServerMetadata(metadata);
  }
  
  public com.flexiant.extility.ImageTemplateMetadata setImageTemplateMetadata(com.flexiant.extility.ImageTemplateMetadata metadata) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.setImageTemplateMetadata(metadata);
  }
  
  public com.flexiant.extility.RuntimeMetadata getRuntimeMetadata(long server_id) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.getRuntimeMetadata(server_id);
  }
  
  public com.flexiant.extility.SSHKey addSSHKey(java.lang.String username, java.lang.String public_key, java.lang.String description) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.addSSHKey(username, public_key, description);
  }
  
  public int removeSSHKey(long key_id) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.removeSSHKey(key_id);
  }
  
  public com.flexiant.extility.SSHKey[] listSSHKeys() throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.listSSHKeys();
  }
  
  public com.flexiant.extility.ImageCapabilities getOwnerImageCapabilities(long image_id) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.getOwnerImageCapabilities(image_id);
  }
  
  public com.flexiant.extility.ImageCapabilities setOwnerImageCapabilities(long image_id, com.flexiant.extility.ImageCapabilities capabilities, boolean allow_all) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.setOwnerImageCapabilities(image_id, capabilities, allow_all);
  }
  
  public com.flexiant.extility.ImageCapabilities getOtherImageCapabilities(long image_id) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.getOtherImageCapabilities(image_id);
  }
  
  public com.flexiant.extility.ImageCapabilities setOtherImageCapabilities(long image_id, com.flexiant.extility.ImageCapabilities capabilities, boolean allow_all) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.setOtherImageCapabilities(image_id, capabilities, allow_all);
  }
  
  public int publishImage(java.lang.String image_uuid, java.lang.String authoritative_uuid) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.publishImage(image_uuid, authoritative_uuid);
  }
  
  public int revokeImage(java.lang.String image_uuid, java.lang.String authoritative_uuid) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.revokeImage(image_uuid, authoritative_uuid);
  }
  
  public com.flexiant.extility.Foreignkey addForeignKey(java.lang.String uuid, java.lang.String key, java.lang.String value) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.addForeignKey(uuid, key, value);
  }
  
  public int removeForeignKey(java.lang.String uuid, java.lang.String key) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.removeForeignKey(uuid, key);
  }
  
  public com.flexiant.extility.Foreignkey[] getForeignKeys(java.lang.String uuid) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.getForeignKeys(uuid);
  }
  
  public com.flexiant.extility.Resource[] listResourcesForForeignKey(java.lang.String key, int resource_type_id, java.lang.String value) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.listResourcesForForeignKey(key, resource_type_id, value);
  }
  
  public com.flexiant.extility.ResourceType[] listResourceTypes() throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.listResourceTypes();
  }
  
  public com.flexiant.extility.Customer getCustomer() throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.getCustomer();
  }
  
  public int enableIPv6Routing(java.lang.String uuid) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.enableIPv6Routing(uuid);
  }
  
  public int disableIPv6Routing(java.lang.String uuid) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.disableIPv6Routing(uuid);
  }
  
  public int renameResource(java.lang.String uuid, java.lang.String new_name) throws java.rmi.RemoteException{
    if (flexiScale == null)
      _initFlexiScaleProxy();
    return flexiScale.renameResource(uuid, new_name);
  }
  
  
}
