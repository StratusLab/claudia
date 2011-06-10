#!/usr/bin/env python
'''
Created on 06 May 2011

@author: nassia
'''
import os, errno
from xml.dom.minidom import parseString

class OVFParser:
    
    
    def __init__(self):
        self.configAttr = {}
        self.path = "/opt/glite/yaim/etc/"

    def findXmlSection(self,dom, sectionName):
        sections = dom.getElementsByTagName(sectionName)
        return sections[0]

    def parse(self, file):
        dom = parseString(file)
        section = self.findXmlSection(dom, "ns1:PropertySection")
        for property in section.getElementsByTagName("ns1:Property"):
            key   = property.getAttribute("ns1:key")
            value = property.getAttribute("ns1:value")
            self.configAttr[key] = value
        dom.unlink()
    
        if 'computingelement' in file:
            self.service = "ce"
        elif 'storageelement' in file:
            self.service = "se"
        elif 'workernode' in file:
            self.service = "wn"
        
        return self.configAttr
    
    def createDirs(self):
        try:
            os.makedirs('/opt/glite/yaim/etc/siteinfo')
            os.makedirs('/opt/glite/yaim/etc/siteinfo/services')
            os.makedirs('/opt/glite/yaim/etc/siteinfo/vo.d')
        except OSError, e:
            if e.errno != errno.EEXIST:
                raise
        wnlistconf = open('/opt/glite/yaim/etc/wn-list.conf','w') 
        wnlistconf.close()
            
            
    def createUserConf(self):
        userconf = open('/opt/glite/yaim/etc/users.conf','w')
        sgroupId = 21000
        for i in range (1,200):
            userId = sgroupId + i
            userconf.write(str(userId) + ":strat")
	    userconf.write("%.3d" %i)
	    userconf.write(":"+str(sgroupId)+":stratuslab:vo.stratuslab.eu::\n")
        groupId = 21800
        #for i in range (1,100):
         #   userId = groupId + i
         #   userconf.write(str(userId) + ":prdstrat" +'{0:03d}'.format(i)+":"+str(groupId)+","+str(sgroupId)+":stratuslabprd,stratuslab:vo.stratuslab.eu:prd:\n")
        groupId = 21900
        for i in range (1,100):
           userId = groupId + i
           userconf.write(str(userId) + ":sgmstrat")
	   userconf.write ("%.3d" %i)
	   userconf.write(":"+str(groupId)+","+str(sgroupId)+":stratuslabsgm,stratuslab:vo.stratuslab.eu:sgm:\n")
        userconf.close()
        
    def createGroupsConf(self):
        groupconf = open('/opt/glite/yaim/etc/groups.conf','w')
        groupconf.write("\"/vo.stratuslab.eu/ROLE=smanager\":::sgm:\n")
        groupconf.write("\"/vo.stratuslab.eu\"::::\n")
        groupconf.close()

    def createStrausvod(self):
        stratusvo = open('/opt/glite/yaim/etc/siteinfo/vo.d/vo.stratuslab.eu','w')
        stratusvo.write('''SW_DIR=$VO_SW_DIR/stratuslab
DEFAULT_SE=$DPM_HOST
QUEUES="stratuslab"
VOMS_SERVERS="vomss://voms.grid.auth.gr:8443/voms/vo.stratuslab.eu?/vo.stratuslab.eu"
VOMSES="'vo.stratuslab.eu voms.grid.auth.gr 15180 /C=GR/O=HellasGrid/OU=auth.gr/CN=voms.grid.auth.gr vo.stratuslab.eu'"
VOMS_CA_DN="'/C=GR/O=HellasGrid/OU=Certification Authorities/CN=HellasGrid CA 2006'"
MAP_WILDCARDS=yes
USER_HOME_PREFIX=/home/stratuslab''')
        stratusvo.close()


    def creategliteBdii(self):
        gliteBdii = open('/opt/glite/yaim/etc/siteinfo/services/glite-bdii_site','w')
        
        for line in open('/opt/glite/yaim/examples/siteinfo/services/glite-bdii_site','r').readlines():
            if('SITE_DESC=' in line):
                gliteBdii.write("SITE_DESC=\""+self.configAttr["stratus.glite.grid."+self.service+".SITE_DESC"]+"\"\n")
            elif 'SITE_SUPPORT_EMAIL=' in line:
                gliteBdii.write("SITE_SUPPORT_EMAIL=" + self.configAttr["stratus.glite.grid."+self.service+".SITE_SUPPORT_EMAIL"]+"\n")    
            elif 'SITE_SECURITY_EMAIL' in line:
                gliteBdii.write("SITE_SECURITY_EMAIL=" + self.configAttr["stratus.glite.grid."+self.service+".SITE_SECURITY_EMAIL"]+"\n")
            elif 'SITE_LOC=' in line:
                gliteBdii.write("SITE_LOC=" + self.configAttr["stratus.glite.grid."+self.service+".SITE_LOC"]+"\n")
            elif 'SITE_WEB=' in line:
                gliteBdii.write("SITE_WEB=\"" + self.configAttr["stratus.glite.grid."+self.service+".SITE_WEB"]+"\"\n")
            elif 'SITE_OTHER_GRID=' in line:
                gliteBdii.write("SITE_OTHER_GRID=\"" + self.configAttr["stratus.glite.grid."+self.service+".SITE_OTHER_GRID"]+"\"\n")
                gliteBdii.write("SITE_OTHER_EGI_NGI=\"" + self.configAttr["stratus.glite.grid."+self.service+".SITE_OTHER_EGEE_ROC"]+"\"\n")
            elif 'BDII_REGIONS=' in line:
                gliteBdii.write("BDII_REGIONS=\"" + self.configAttr["stratus.glite.grid."+self.service+".BDII_REGIONS"]+"\"\n")
            elif 'BDII_host-id-1_URL=' in line:
                gliteBdii.write("BDII_CE_URL=\"ldap://$CE_HOST:2170/mds-vo-name=resource,o=grid\"\n")
                gliteBdii.write("BDII_SE_URL=\"ldap://$DPM_HOST:2170/mds-vo-name=resource,o=grid\"\n")
            else:
                gliteBdii.write(line)
        
    def createCreamCE(self):
        creamCE = open('/opt/glite/yaim/etc/siteinfo/services/glite-creamce','w')
            
        for line in open('/opt/glite/yaim/examples/siteinfo/services/glite-creamce','r').readlines():
            if 'CEMON_HOST=' in line:
                creamCE.write("CEMON_HOST=" + self.configAttr["stratus.glite.grid."+self.service+".CE_HOST"]+"\n")
            elif 'CREAM_DB_USER=' in line:
                creamCE.write("CREAM_DB_USER=stratusUser\n")
            elif 'CREAM_DB_PASSWORD=' in line:
                creamCE.write("CREAM_DB_PASSWORD=cream_" + self.configAttr["stratus.glite.grid."+self.service+".DB_PASSWD"]+"\n")
            elif 'BLPARSER_HOST=' in line:
                creamCE.write(line)
                creamCE.write("BLPARSER_WITH_UPDATER_NOTIFIER=true\n")
            elif 'CREAM_CE_STATE=' in line:
                creamCE.write("CREAM_CE_STATE=Production\n")
            else:
                creamCE.write(line)
           
    def creategliteMpi(self):       
        gliteMpiCE = open('/opt/glite/yaim/etc/siteinfo/services/glite-mpi_ce','w')
        gliteMpiWN = open('/opt/glite/yaim/etc/siteinfo/services/glite-mpi_wn','w')
        gliteMpiCE.write('''#----------------------------------
# MPI-related configuration:
#----------------------------------

MPI_MPICH_ENABLE="no"
MPI_MPICH2_ENABLE="yes"
MPI_OPENMPI_ENABLE="yes"
MPI_LAM_ENABLE="no"
MPI_SHARED_HOME="no"
MPI_SSH_HOST_BASED_AUTH="yes"
MPI_OPENMPI_PATH=/usr/lib64/openmpi/1.4-gcc/
MPI_OPENMPI_MPIEXEC=/usr/lib64/openmpi/1.4-gcc/bin/mpiexec
MPI_MPICH2_MPIEXEC=/usr/bin/mpiexec
MPI_OPENMPI_VERSION=1.4
MPI_MPICH2_PATH=/usr/
MPI_MPICH2_VERSION=1.1.1
I2G_MPI_START=/opt/i2g/bin/mpi-start''')
        gliteMpiWN.write('''#----------------------------------
# MPI-related configuration:
#----------------------------------

MPI_MPICH_ENABLE="no"
MPI_MPICH2_ENABLE="yes"
MPI_OPENMPI_ENABLE="yes"
MPI_LAM_ENABLE="no"
MPI_SHARED_HOME="no"
MPI_SSH_HOST_BASED_AUTH="yes"
MPI_OPENMPI_PATH=/usr/lib64/openmpi/1.4-gcc/
MPI_OPENMPI_MPIEXEC=/usr/lib64/openmpi/1.4-gcc/bin/mpiexec
MPI_MPICH2_MPIEXEC=/usr/bin/mpiexec
MPI_OPENMPI_VERSION=1.4
MPI_MPICH2_PATH=/usr/
MPI_MPICH2_VERSION=1.1.1
I2G_MPI_START=/opt/i2g/bin/mpi-start''')
        gliteMpiCE.close()
        gliteMpiWN.close()
           
    def createSeDpmDisk(self):        
        gliteDpmDisk = open('/opt/glite/yaim/etc/siteinfo/services/glite-se_dpm_disk','w')   
        for line in open('/opt/glite/yaim/examples/siteinfo/services/glite-se_dpm_disk','r').readlines():
            if 'DPMPOOL=' in line:
                gliteDpmDisk.write("DPMPOOL=dpmpool\n")
            elif 'DPM_FILESYSTEMS=' in line:
                gliteDpmDisk.write("DPM_FILESYSTEMS=\"$DPM_HOST:/dpmpool\"\n")
            else:
                gliteDpmDisk.write(line)
        gliteDpmDisk.close()   
        
    def createSeDpmMysql(self): 
        gliteDpmMySql = open('/opt/glite/yaim/etc/siteinfo/services/glite-se_dpm_mysql','w')   
        for line in open('/opt/glite/yaim/examples/siteinfo/services/glite-se_dpm_mysql','r').readlines():
            if 'DPMPOOL=' in line:
                gliteDpmMySql.write("DPMPOOL=dpmpool\n")
            elif 'DPM_FILESYSTEMS=' in line:
                gliteDpmMySql.write("DPM_FILESYSTEMS=\"$DPM_HOST:/dpmpool\"\n")    
            elif 'DPM_DB_USER=' in line:
                gliteDpmMySql.write("DPM_DB_USER=dpm_stratusUser\n")
            elif 'DPM_DB_PASSWORD=' in line:
                gliteDpmMySql.write("DPM_DB_PASSWORD=" + self.configAttr["stratus.glite.grid."+self.service+".DB_PASSWD"]+"\n")
            elif 'DPM_INFO_USER=' in line:
                gliteDpmMySql.write("DPM_INFO_USER=dpmI_stratusUser\n")
            elif 'DPM_INFO_PASS=' in line:
                gliteDpmMySql.write("DPM_INFO_PASS=" + self.configAttr["stratus.glite.grid."+self.service+".DB_PASSWD"]+"\n")
            else:
                gliteDpmMySql.write(line)
        gliteDpmMySql.close()
        
        
    def createSiteInfoDef(self):
        siteinfo = open('/opt/glite/yaim/etc/siteinfo/site-info.def','w')
        
        for line in open('/opt/glite/yaim/examples/siteinfo/site-info.def','r').readlines():
            if '/glite/yaim/examples/' in line :
                line = line.replace('examples','etc')
                siteinfo.write(line)
            elif 'MYSQL_PASSWORD' in line:
                siteinfo.write("MYSQL_PASSWORD=" + self.configAttr["stratus.glite.grid."+self.service+".DB_PASSWD"]+"\n")
            elif 'SITE_NAME=' in line:
                siteinfo.write("SITE_NAME=" + self.configAttr["stratus.glite.grid."+self.service+".SITENAME"]+"\n"  )
            elif 'SITE_EMAIL=' in line:
                siteinfo.write("SITE_EMAIL=" + self.configAttr["stratus.glite.grid."+self.service+".SITE_SUPPORT_EMAIL"]+"\n"  )
            elif 'SITE_LAT=' in line:
                siteinfo.write("SITE_LAT=" + self.configAttr["stratus.glite.grid."+self.service+".SITE_LAT"]+"\n"  )
            elif 'SITE_LONG=' in line:
                siteinfo.write("SITE_LONG=" + self.configAttr["stratus.glite.grid."+self.service+".SITE_LONG"]+"\n"  )
            elif 'CE_HOST=' in line:
                siteinfo.write("CE_HOST=" + self.configAttr["stratus.glite.grid."+self.service+".CE_HOST"]+"\n"  )
            elif 'CE_CPU_MODEL' in line:
                siteinfo.write("CE_CPU_MODEL=Qemu\n")
            elif 'CE_CPU_VENDOR=' in line:
                siteinfo.write("CE_CPU_VENDOR=Qemu\n")
            elif 'CE_CPU_SPEED=' in line:
                siteinfo.write("CE_CPU_SPEED=" + self.configAttr["stratus.glite.grid."+self.service+".CPUSPEED"] +"\n" )
            elif 'CE_OS=' in line:
                siteinfo.write("CE_OS=\"ScientificSL\"\n")
            elif 'CE_OS_RELEASE=' in line:
                siteinfo.write("CE_OS_RELEASE=5.5\n")
            elif 'CE_OS_VERSION=' in line:
                siteinfo.write("CE_OS_VERSION=\"Boron\"\n")
            elif 'CE_OS_ARCH=' in line:
                siteinfo.write("CE_OS_ARCH="+self.configAttr["stratus.glite.grid."+self.service+".OSARCH"]+"\n" )
            elif 'CE_MINPHYSMEM=' in line:
                siteinfo.write("CE_MINPHYSMEM=" + self.configAttr["stratus.glite.grid."+self.service+".MINPHYSMEM"]+"\n" )
            elif 'CE_MINVIRTMEM=' in line:
                siteinfo.write("CE_MINVIRTMEM=" + self.configAttr["stratus.glite.grid."+self.service+".MINVIRTMEM"]+"\n" )
            elif 'CE_PHYSCPU=' in line:
                siteinfo.write("CE_PHYSCPU=" + self.configAttr["stratus.glite.grid."+self.service+".PHYSCPU"]+"\n" )
            elif 'CE_LOGCPU=' in line:
                siteinfo.write("CE_LOGCPU=" + self.configAttr["stratus.glite.grid."+self.service+".LOGCPU"]+"\n" )
            elif 'CE_SMPSIZE=' in line:
                siteinfo.write("CE_SMPSIZE=" + self.configAttr["stratus.glite.grid."+self.service+".SMPSIZE"]+"\n" )
            elif 'CE_SI00=' in line:
                siteinfo.write("CE_SI00=" + self.configAttr["stratus.glite.grid."+self.service+".SI00"]+"\n" )     
            elif 'CE_SF00=' in line:
                siteinfo.write("CE_SF00=0\n")   
            elif 'CE_OUTBOUNDIP=' in line:
                siteinfo.write("CE_OUTBOUNDIP=TRUE\n")    
            elif 'CE_RUNTIMEENV=' in line : 
                if 'e.g' not in line:
                    siteinfo.write("CE_RUNTIMEENV=\"LCG-2 LCG-2_1_0 LCG-2_1_1 LCG-2_2_0 GLITE-3_0_0 GLITE-3_1_0 GLITE-3_2_0\"\n")
            elif 'CE_CAPABILITY=' in line:
                siteinfo.write("CE_CAPABILITY=\"CPUScalingReferenceSI00="+self.configAttr["stratus.glite.grid."+self.service+".SI00"]+"\"\n")
            elif 'CE_OTHERDESCR=' in line:
                siteinfo.write("CE_OTHERDESCR=\""+self.configAttr["stratus.glite.grid."+self.service+".HEP-SPEC"]+"\"\n" )
            elif 'JOB_MANAGER=' in line:
                siteinfo.write("JOB_MANAGER=lcgpbs\n")
            elif 'CE_BATCH_SYS=' in line:
                siteinfo.write("CE_BATCH_SYS=pbs\n")
            elif 'BATCH_LOG_DIR=' in line:
                siteinfo.write("BATCH_LOG_DIR=/var/spool/pbs\n")
            elif 'BATCH_VERSION=' in line:
                siteinfo.write("BATCH_VERSION=2.3.6-2cri.el5\n")      
            elif 'APEL_DB_PASSWORD=' in line:  
                siteinfo.write("APEL_MYSQL_HOST="+ self.configAttr["stratus.glite.grid."+self.service+".apel.HOSTNAME"]+"\n" ) 
                siteinfo.write("APEL_DB_PASSWORD=\""+ self.configAttr["stratus.glite.grid."+self.service+".DB_PASSWD"]+"\"\n")
                siteinfo.write("APEL_PUBLISH_USER_DN=\"yes\"\n")
            elif 'DPM_HOST=' in line:
                siteinfo.write("DPM_HOST=" + self.configAttr["stratus.glite.grid."+self.service+".DPM_HOST"]+"\n" )
            elif 'SE_LIST=' in line:
                siteinfo.write("SE_LIST=\"$DPM_HOST\"\n")
            elif 'SE_MOUNT_INFO_LIST=' in line:
                siteinfo.write("SE_MOUNT_INFO_LIST=\"none\"\n")
            elif 'SITE_BDII_HOST=' in line:
                siteinfo.write("SITE_BDII_HOST=" + self.configAttr["stratus.glite.grid."+self.service+".CE_HOST"]+"\n" )
            elif 'BDII_HOST=' in line:
                siteinfo.write("BDII_HOST=" + self.configAttr["stratus.glite.grid."+self.service+".BDII_HOST"]+"\n" )
            elif 'VOS=' in line:
                siteinfo.write("VOS=\"vo.stratuslab.eu\"\n")
            elif 'VO_SW_DIR=' in line:
                siteinfo.write("VO_SW_DIR=/opt/exp_soft\n")
            elif 'QUEUES=' in line:
                if 'Ex.' not in line:
                    siteinfo.write("QUEUES=\"stratuslab\"\n")
            elif '<queue-name>_GROUP_ENABLE=' in line:
                siteinfo.write("STRATUSLAB_GROUP_ENABLE=\"vo.stratuslab.eu /VO=vo.stratuslab.eu/GROUP=/vo.stratuslab.eu/ROLE=smanager\"\n")
            elif 'VO_<vo_name>_' in line:
                siteinfo.write("#"+line)
            else:
                siteinfo.write(line)    
        siteinfo.close()

    def createConfigurationFiles(self):
        self.createDirs()
        self.createUserConf()
        self.createGroupsConf()
        self.createSiteInfoDef()
        self.createStrausvod()
        self.creategliteMpi()
        if self.service == 'ce':
            self.creategliteBdii()
            self.createCreamCE()
            os.system("/root/gLite3_2ConfigCE.sh")
        if self.service == 'se':
            self.createSeDpmDisk()
            self.createSeDpmMysql()
            os.system("/root/gLite3_2ConfigSE.sh")
        if self.service == 'wn':
            #self.creategliteBdii()
            #self.createCreamCE()
            os.system("hostname >> /opt/glite/yaim/etc/wn-list.conf ")
            os.system("/root/gLite3_2ConfigWN.sh")
            
# Executing the functions to create configuration files

myParser = OVFParser()
file = open("/mnt/ovf-env.xml", "r").read()

myParser.parse(file)
myParser.createConfigurationFiles()

#myParser.createDirs()
#myParser.createUserConf()
#myParser.createGroupsConf()
#myParser.createSiteInfoDef()
#myParser.createStrausvod()


#myParser.creategliteBdii()
#myParser.createCreamCE()
#myParser.creategliteMpi()
#myParser.createSeDpmDisk()
#myParser.createSeDpmMysql()
        
        
