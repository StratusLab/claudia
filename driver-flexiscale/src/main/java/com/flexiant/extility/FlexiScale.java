/**
 * FlexiScale.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.flexiant.extility;

public interface FlexiScale extends java.rmi.Remote {

    /**
     * Lists the product offers available for the
     * 				customer's billing entity
     */
    public com.flexiant.extility.ProductOffer[] listProductOffers(int type_id) throws java.rmi.RemoteException;

    /**
     * Lists all Servers for a customer or for a
     * 				specific VDC within a customer
     */
    public com.flexiant.extility.Server[] listServers(long vdc_id) throws java.rmi.RemoteException;

    /**
     * Returns a list of all Servers for a customer or
     * 				for a specific VDC within a customer
     */
    public com.flexiant.extility.Server[] listServersForImage(long image_id) throws java.rmi.RemoteException;

    /**
     * Lists all known jobs in that customer ID. This
     * 				could quickly become large so you may want to use 'FilterJobs'
     */
    public com.flexiant.extility.Job[] listJobs() throws java.rmi.RemoteException;

    /**
     * Details specific information about a particular
     * 				Job ID
     */
    public com.flexiant.extility.Job getJob(long job_id) throws java.rmi.RemoteException;

    /**
     * Cancels an existing, still pending Job
     */
    public int cancelJob(long job_id) throws java.rmi.RemoteException;

    /**
     * Returns a list of rules for a specific firewall,
     * 				filterable by direction.
     */
    public com.flexiant.extility.FirewallRule[] listFirewallRules(long firewall_id, java.lang.String direction) throws java.rmi.RemoteException;

    /**
     * Returns a list of all Firewalls for a customer,
     * 				it can be filtered by VDC if required.
     */
    public com.flexiant.extility.Firewall[] listFirewalls(long vdc_id) throws java.rmi.RemoteException;

    /**
     * Lists all subnets attached to a particular VLAN
     */
    public com.flexiant.extility.Subnet[] listSubnets(long vlan_id) throws java.rmi.RemoteException;

    /**
     * Provides details for a specific subnet
     */
    public com.flexiant.extility.Subnet getSubnet(long subnet_id) throws java.rmi.RemoteException;

    /**
     * Lists allowed Firewall Protocols
     */
    public com.flexiant.extility.FirewallProtocol[] listFirewallProtocols() throws java.rmi.RemoteException;

    /**
     * Lists valid ICMP Protocols for ICMP rules on the
     * 				firewall
     */
    public com.flexiant.extility.IcmpProtocol[] listIcmpProtocols() throws java.rmi.RemoteException;

    /**
     * Lists all jobs for a customer subject to a range
     * 				of ID's and able to be sorted
     */
    public com.flexiant.extility.FilterJobList filterJobs(long from, long to, java.lang.String order_by, java.lang.String direction) throws java.rmi.RemoteException;

    /**
     * Lists all active Jobs for a customer
     */
    public com.flexiant.extility.Job[] listRunningJobs() throws java.rmi.RemoteException;

    /**
     * Lists the available public or private Image
     * 				Templates
     */
    public com.flexiant.extility.ImageTemplate[] listImageTemplates() throws java.rmi.RemoteException;

    /**
     * Lists all Disks of a specific customer or in a
     * 				specific VDC of a Customer
     */
    public com.flexiant.extility.Disk[] listDisks(long vdc_id) throws java.rmi.RemoteException;

    /**
     * Lists Disks that are not attached to a Server per
     * 				customer or filterable by VDC
     */
    public com.flexiant.extility.Disk[] listFreeDisks(long vdc_id) throws java.rmi.RemoteException;

    /**
     * Retrieves individual details for a specific Disk
     */
    public com.flexiant.extility.Disk getDisk(long disk_id) throws java.rmi.RemoteException;
    public com.flexiant.extility.Disk moveDisk(long disk_id, long vdc_id) throws java.rmi.RemoteException;

    /**
     * Assigns an IP Address to a Network Interface
     */
    public int addIpAddressToNic(long nic_id, java.lang.String ip_address, boolean dhcp) throws java.rmi.RemoteException;

    /**
     * Removes an IP Address from a Network Interface
     */
    public int removeIpAddressFromNic(long nic_id, java.lang.String ip_address) throws java.rmi.RemoteException;

    /**
     * Lists IP addresses attached to a Network
     * 				Interface
     */
    public com.flexiant.extility.IpAddress[] listIpAddressesForNic(long nic_id) throws java.rmi.RemoteException;

    /**
     * Creates a new VLAN in the specified VDC. The VLAN
     * 				can be public (using public IP Addresses) or Private (Using
     * 				customers own IP's via VPN, or RFC 1918)
     */
    public com.flexiant.extility.Vlan createVLAN(long vdc_id, boolean _private) throws java.rmi.RemoteException;

    /**
     * Deletes the requested VLAN
     */
    public int deleteVLAN(long vlan_id) throws java.rmi.RemoteException;

    /**
     * Lists the Network Interfaces for a particular
     * 				VLAN
     */
    public com.flexiant.extility.NetworkInterface[] listNicsForVLAN(long vlan_id) throws java.rmi.RemoteException;

    /**
     * Retrieves detailed information for a specific
     * 				Network Interface
     */
    public com.flexiant.extility.NetworkInterface getNetworkInterface(long nic_id) throws java.rmi.RemoteException;

    /**
     * Lists all VLANS in a specified VDC
     */
    public com.flexiant.extility.Vlan[] listVLANs(long vdc_id) throws java.rmi.RemoteException;

    /**
     * Retrieves details for a specific VLAN
     */
    public com.flexiant.extility.Vlan getVLAN(long vlan_id) throws java.rmi.RemoteException;

    /**
     * Creates a VDC (Virtual Data Centre)
     */
    public com.flexiant.extility.VDC createVDC(java.lang.String vdc_name) throws java.rmi.RemoteException;

    /**
     * Deletes a VDC and if not empty, it can specify to
     * 				delete everything in it as well
     */
    public int deleteVDC(long vdc_id, boolean delete_all) throws java.rmi.RemoteException;

    /**
     * List all VDC's within the customer
     */
    public com.flexiant.extility.VDC[] listVDCs() throws java.rmi.RemoteException;

    /**
     * Retrieves details on a specific VDC
     */
    public com.flexiant.extility.VDC getVDC(long vdc_id) throws java.rmi.RemoteException;

    /**
     * Moves a subnet to another VLAN within the same
     * 				VDC/Customer?
     */
    public int moveSubnet(long subnet_id, long new_vlan_id) throws java.rmi.RemoteException;

    /**
     * Attaches a new subnet to a specified VLAN
     */
    public com.flexiant.extility.Subnet attachSubnet(long vlan_id) throws java.rmi.RemoteException;

    /**
     * Removes a subnet from a Customer
     */
    public int removeSubnet(long subnet_id) throws java.rmi.RemoteException;

    /**
     * Adds a Network Interface to a Server
     */
    public long addNetworkInterface(long server_id, long vlan_id, int index) throws java.rmi.RemoteException;

    /**
     * Retrieves details about a specific Server
     */
    public com.flexiant.extility.Server getServer(long server_id) throws java.rmi.RemoteException;

    /**
     * Starts the requested Server
     */
    public long startServer(long server_id, com.flexiant.extility.RuntimeMetadata runtime_metadata) throws java.rmi.RemoteException;

    /**
     * Stops the requested Server
     */
    public long stopServer(long server_id, int stop_method) throws java.rmi.RemoteException;

    /**
     * Reboots the requested Server
     */
    public long rebootServer(long server_id) throws java.rmi.RemoteException;

    /**
     * Call to deliberately wait for a job to be
     * 				completed
     */
    public int waitForJob(long job_id) throws java.rmi.RemoteException;

    /**
     * Query to check if a job is in progress or not
     */
    public int isJobRunning(long job_id) throws java.rmi.RemoteException;

    /**
     * Destroys a Server
     */
    public int destroyServer(long server_id) throws java.rmi.RemoteException;

    /**
     * Creates a Blank Disk in a particular VDC and
     * 				specified name and size
     */
    public com.flexiant.extility.Disk createDisk(long vdc_id, java.lang.String name, long disk_productoffer_id, long image_id) throws java.rmi.RemoteException;

    /**
     * Deletes an existing Disk
     */
    public int deleteDisk(long disk_id) throws java.rmi.RemoteException;

    /**
     * Reverts a disk to a previous state from a Disk
     * 				Snapshot
     */
    public int revertDisk(long snapshot_id) throws java.rmi.RemoteException;

    /**
     * Attaches a specific Disk to a specific Server
     */
    public int attachDisk(long server_id, long disk_id, int index) throws java.rmi.RemoteException;

    /**
     * Detaches a disk from a server
     */
    public int detachDisk(long server_id, long disk_id) throws java.rmi.RemoteException;

    /**
     * Creates a snapshot of a Disk
     */
    public com.flexiant.extility.DiskSnapshot createDiskSnapshot(long disk_id, java.lang.String name, int force) throws java.rmi.RemoteException;

    /**
     * Deletes a Disk Snapshot
     */
    public int deleteDiskSnapshot(long snapshot_id) throws java.rmi.RemoteException;

    /**
     * Lists all Snapshots taken from a specific Disk
     */
    public com.flexiant.extility.DiskSnapshot[] listDiskSnapshots(long disk_id) throws java.rmi.RemoteException;

    /**
     * Retrieves details on a specific Snapshot
     */
    public com.flexiant.extility.DiskSnapshot getDiskSnapshot(long snapshot_id) throws java.rmi.RemoteException;

    /**
     * Lists all Snapshots for a particular VDC
     */
    public com.flexiant.extility.DiskSnapshot[] listAllDiskSnapshots(long vdc_id) throws java.rmi.RemoteException;

    /**
     * Creates a Disk from a disk snapshot
     */
    public com.flexiant.extility.Disk cloneDisk(long snapshot_id, java.lang.String new_disk_name, long vdc_id) throws java.rmi.RemoteException;

    /**
     * Lists all Disks created from a Disk Snapshot
     */
    public com.flexiant.extility.Disk[] listClonedDisks(long snapshot_id) throws java.rmi.RemoteException;

    /**
     * Deletes an Image Template
     */
    public int deleteImageTemplate(long image_template_id) throws java.rmi.RemoteException;

    /**
     * Creates a new Server
     */
    public com.flexiant.extility.Server createServer(long vdc_id, long vlan_id, java.lang.String name, long image_id, long server_productoffer_id, long[] disk_productoffers) throws java.rmi.RemoteException;

    /**
     * Creates a VNC (Console) Connection to a Server
     */
    public com.flexiant.extility.VNCAccess getVNCConnection(long server_id, java.lang.String access_network) throws java.rmi.RemoteException;

    /**
     * Creates a Firewall on a specific IP Address
     */
    public com.flexiant.extility.Firewall createFirewall(java.lang.String ip_address, java.lang.String default_inbound_policy, java.lang.String default_outbound_policy, long vdc_id) throws java.rmi.RemoteException;

    /**
     * Creates a rule for a preexisting Firewall
     */
    public com.flexiant.extility.FirewallRule createFirewallRule(com.flexiant.extility.FirewallRule specification, int index) throws java.rmi.RemoteException;

    /**
     * Modifies an existing Server
     */
    public com.flexiant.extility.Server modifyServer(long server_id, java.lang.String name, long bootable_disk_id, long server_productoffer_id) throws java.rmi.RemoteException;

    /**
     * Modifies an existing ImageTemplate
     */
    public com.flexiant.extility.ImageTemplate modifyImageTemplate(long image_id, java.lang.String name, java.lang.String default_username, boolean generate_password) throws java.rmi.RemoteException;

    /**
     * Modifies the parameters of an existing Disk
     */
    public com.flexiant.extility.Disk modifyDisk(long disk_id, java.lang.String name, long disk_productoffer_id) throws java.rmi.RemoteException;

    /**
     * Modifies an Existing Firewall's Global Settings
     */
    public com.flexiant.extility.Firewall modifyFirewall(long firewall_id, java.lang.String new_default_in_policy, java.lang.String new_default_out_policy) throws java.rmi.RemoteException;

    /**
     * Modifies an existing Firewall Rule
     */
    public com.flexiant.extility.FirewallRule modifyFirewallRule(com.flexiant.extility.FirewallRule specification) throws java.rmi.RemoteException;

    /**
     * Deletes a Firewall
     */
    public int deleteFirewall(long firewall_id) throws java.rmi.RemoteException;

    /**
     * Delete a specific Firewall Rule
     */
    public int deleteFirewallRule(int firewall_rule_id) throws java.rmi.RemoteException;

    /**
     * Removes a Network Interface from a Server
     */
    public int removeNetworkInterface(long nic_id) throws java.rmi.RemoteException;

    /**
     * Converts a disk in to a ImageTemplate
     */
    public com.flexiant.extility.ImageTemplate createImageTemplate(long disk_id, java.lang.String name, java.lang.String default_username, boolean generate_password) throws java.rmi.RemoteException;

    /**
     * Uploads a pre-existing disk image from an
     * 				external location via http(s)/ftp(s)
     */
    public long fetchDisk(java.lang.String url, java.lang.String checksum, java.lang.String conn_user, java.lang.String conn_pass, java.lang.String name, long disk_productoffer_id, long vdc_id, boolean makeImage, java.lang.String image_defaultusername) throws java.rmi.RemoteException;
    public com.flexiant.extility.VDC renameVDC(long vdc_id, java.lang.String name) throws java.rmi.RemoteException;
    public long createServerFromRemoteDisk(com.flexiant.extility.RemoteDisk[] remote_disks, java.lang.String server_name, java.lang.String initial_user, java.lang.String initial_password, long vdc_id, long vlan_id, long server_product_offer) throws java.rmi.RemoteException;
    public long migrateServer(com.flexiant.extility.RemoteDisk[] remote_disks, java.lang.String server_name, java.lang.String initial_user, java.lang.String initial_password, long vdc_id, long vlan_id, long server_product_offer) throws java.rmi.RemoteException;

    /**
     * Lists the product offers available for the
     * 				customer's billing entity
     */
    public long createScheduledServerJob(long server_id, int type, java.util.Calendar start_time, com.flexiant.extility.RuntimeMetadata runtime_metadata) throws java.rmi.RemoteException;
    public com.flexiant.extility.CustomerMetadata getCustomerMetadata() throws java.rmi.RemoteException;
    public com.flexiant.extility.ServerMetadata getServerMetadata(long server_id) throws java.rmi.RemoteException;
    public com.flexiant.extility.ImageTemplateMetadata getImageTemplateMetadata(long image_id) throws java.rmi.RemoteException;
    public com.flexiant.extility.CustomerMetadata setCustomerMetadata(com.flexiant.extility.CustomerMetadata metadata) throws java.rmi.RemoteException;
    public com.flexiant.extility.ServerMetadata setServerMetadata(com.flexiant.extility.ServerMetadata metadata) throws java.rmi.RemoteException;
    public com.flexiant.extility.ImageTemplateMetadata setImageTemplateMetadata(com.flexiant.extility.ImageTemplateMetadata metadata) throws java.rmi.RemoteException;
    public com.flexiant.extility.RuntimeMetadata getRuntimeMetadata(long server_id) throws java.rmi.RemoteException;
    public com.flexiant.extility.SSHKey addSSHKey(java.lang.String username, java.lang.String public_key, java.lang.String description) throws java.rmi.RemoteException;
    public int removeSSHKey(long key_id) throws java.rmi.RemoteException;
    public com.flexiant.extility.SSHKey[] listSSHKeys() throws java.rmi.RemoteException;
    public com.flexiant.extility.ImageCapabilities getOwnerImageCapabilities(long image_id) throws java.rmi.RemoteException;
    public com.flexiant.extility.ImageCapabilities setOwnerImageCapabilities(long image_id, com.flexiant.extility.ImageCapabilities capabilities, boolean allow_all) throws java.rmi.RemoteException;
    public com.flexiant.extility.ImageCapabilities getOtherImageCapabilities(long image_id) throws java.rmi.RemoteException;
    public com.flexiant.extility.ImageCapabilities setOtherImageCapabilities(long image_id, com.flexiant.extility.ImageCapabilities capabilities, boolean allow_all) throws java.rmi.RemoteException;
    public int publishImage(java.lang.String image_uuid, java.lang.String authoritative_uuid) throws java.rmi.RemoteException;
    public int revokeImage(java.lang.String image_uuid, java.lang.String authoritative_uuid) throws java.rmi.RemoteException;
    public com.flexiant.extility.Foreignkey addForeignKey(java.lang.String uuid, java.lang.String key, java.lang.String value) throws java.rmi.RemoteException;
    public int removeForeignKey(java.lang.String uuid, java.lang.String key) throws java.rmi.RemoteException;
    public com.flexiant.extility.Foreignkey[] getForeignKeys(java.lang.String uuid) throws java.rmi.RemoteException;
    public com.flexiant.extility.Resource[] listResourcesForForeignKey(java.lang.String key, int resource_type_id, java.lang.String value) throws java.rmi.RemoteException;
    public com.flexiant.extility.ResourceType[] listResourceTypes() throws java.rmi.RemoteException;
    public com.flexiant.extility.Customer getCustomer() throws java.rmi.RemoteException;
    public int enableIPv6Routing(java.lang.String uuid) throws java.rmi.RemoteException;
    public int disableIPv6Routing(java.lang.String uuid) throws java.rmi.RemoteException;
    public int renameResource(java.lang.String uuid, java.lang.String new_name) throws java.rmi.RemoteException;
}
