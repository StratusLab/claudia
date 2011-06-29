#!/usr/bin/python -tt
# -*- coding: utf-8 -*-
import time, sys, commands, urllib2, logging, subprocess
from xml.dom.minidom import parse

FQN_XML_KEY = "stratus.glite.grid.ce.fqnkpi"
CLOTHO_URL="stratus.glite.grid.ce.ipmonitoring"
LOG_FILE = "/var/log/torqueProbe.log"
ROOT_MONITORING_TAG_NAME = "MonitoringInformation"
EVENT_TYPE_TAG_NAME = "EventType"
T_0_TAG_NAME = "EpochTimestamp"
T_DELTA_TAG_NAME = "TimeDelta"
FQN_TAG_NAME = "FQN"
VALUE_TAG_NAME = "Value"
EVENT_TYPE_VALUE = "AGENT"

logging.basicConfig(filename=LOG_FILE, level=logging.DEBUG, format="%(asctime)s - %(levelname)s - %(message)s")

def parserXML(ovf):
    datos = {}
    doc = parse(ovf)
    nodes = doc.getElementsByTagName('ns1:Property')
    for node in nodes:
        k = node.getAttribute('ns1:key')
        v = node.getAttribute('ns1:value')
        datos[k] = v

    return datos
#

def getKPI():
    """ return the kpi value of the load balancer. """
    total_slots = pbsnodes()
    if total_slots == 0 or (total_slots < 0):
        logging.info("There are 0 slots in Torque")
        return str(0)
    else:
        occupied_slots = qstat()
        KPI = (float(occupied_slots)/total_slots) * 100
	    
        logging.info("Total number of jobs: "+str(occupied_slots))
        logging.info("Total number of slots: "+str(total_slots))
        logging.info("KPI occupied_slots/total_slots: ("+ str(occupied_slots)+"/"+str(total_slots)+")*100="+str(KPI)+"\n")
    
        return str(KPI)
#

def createData(fqn, kpi):
    """ make the xml with the monitoring info """
    xml = "<"+ROOT_MONITORING_TAG_NAME+"><"+EVENT_TYPE_TAG_NAME+">" + EVENT_TYPE_VALUE + "</"+EVENT_TYPE_TAG_NAME+"><"+T_0_TAG_NAME+">0</"+T_0_TAG_NAME+"><"+T_DELTA_TAG_NAME+">0</"+T_DELTA_TAG_NAME+"><"+FQN_TAG_NAME+">" + fqn + "</"+FQN_TAG_NAME+"><"+VALUE_TAG_NAME+">" + kpi + "</"+VALUE_TAG_NAME+"></"+ROOT_MONITORING_TAG_NAME+">"
    return xml
#

def sendData(url, data):
    """ send the xml data to claudia url """
    req = urllib2.Request(url,data)
    urllib2.urlopen(req)
#

def qstat():
    # a dictionary of dictionaries, containing all jobs' info
    output_all = {}
   
    output,_ = call_command('qstat -a')
    data = output.split('\n')

    begin = False
    job_num = 0
    current_host = None
    for line in data:
        line = line.rstrip()

        if line.endswith(':'):
            current_host = line.replace(':', '')
            begin = False
        #Start monitoring output, you are streaming through the actual data
        if begin and line != '':
            output_all[job_num] = {}
            # A dictionary for each job entry
            output_all[job_num]['job_id'], output_all[job_num]['username'], output_all[job_num]['queue'], output_all[job_num]['jobname'], output_all[job_num]['session_id'], output_all[job_num]['nds'], output_all[job_num]['tsk'], output_all[job_num]['required_memory'], output_all[job_num]['required_time'], output_all[job_num]['status'], output_all[job_num]['elapsed_time'] = line.split()
            output_all[job_num]['host'] = current_host
            job_num = job_num + 1

        if line.startswith('--'):
            begin = True

    return len(output_all)
#
  
def pbsnodes():
    output,_ = call_command('pbsnodes')
    data = output.split('\n')

    npoutput,_ = call_command('pbsnodes | grep np')
    np_data = npoutput.split('\n')

    state_output,_ = call_command('pbsnodes | grep state\ =')
    state_data = state_output.split('\n')

    nps = 0
    i = 0
    for l in np_data:
        ls = l.strip().split('=')
        if(len(ls) > 1 and not state_data[i].startswith('down')):
            nps = nps + int(ls[1].strip())
        i = i + 1
    return nps
#

def call_command(command):
    process = subprocess.Popen(command, shell=True,
                            stdout=subprocess.PIPE,
                            stderr=subprocess.PIPE)
    return process.communicate()
#

def main():
    
    #parse ovf
    ovf = parserXML("/mnt/stratuslab/ovf-env.xml")
    url = "http://"+ ovf[CLOTHO_URL] +":1114/vmi"
    fqn = ovf[FQN_XML_KEY]
    
    logging.info("Claudia monitoring REST uri: " + url)
    logging.info("Sending KPI to Claudia every 30  seconds.")
    logging.info("Ctrl+C to stop\n")

    try:
        while True:
            try:
                kpi = getKPI()
                data = createData(fqn, kpi)
                logging.debug(data)
                logging.debug(url+"\n")
                sendData(url, data) 
            except urllib2.URLError:
                logging.debug(sys.exc_info())
                logging.debug("Claudia is down!!!\n")
            time.sleep(30)
    except KeyboardInterrupt:
        print "\nBye bye!!!"
    return 0
#

if __name__ == "__main__":
    sys.exit(main())

