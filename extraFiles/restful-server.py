#!/usr/bin/python

import web, OVFParser
import os, subprocess, fileinput
from xml.dom.minidom import parseString

#urls = ('/ce_server/addWN', 'addWN',
#        '/ce_server/removeWN/(.*)', 'removeWN')

urls = ('/(.*)', 'CEServer')

class CEServer(object):
    
    def GET(self, num):
        #print 'This is remove: '+ num + ' of nodes.'
        free_nodes = get_free_wns()
        wn_list = '/opt/glite/yaim/etc/wn-list.conf'
        data = '<?xml version="1.0" encoding="UTF-8"?><nodes>'
        n = 0
        for host in free_nodes:
            data = data +'<node>'+host+'</node>'
            for line in fileinput.FileInput(wn_list,inplace=1):
                line = line.replace(host,'').strip()
                print line
            n = n + 1
            if n > num:
                break
        
        data = data +'</nodes>'
        
        output,_ = call_command('sed -i /^$/d '+wn_list)
        o,_ = call_command('echo date >> /var/log/yaim_torque_config.log')
        conf_output,_ = call_command('/opt/glite/yaim/bin/yaim -c -s /opt/glite/yaim/etc/siteinfo/site-info.def -n TORQUE_server -n TORQUE_utils >> /var/log/yaim_torque_config.log')
        
        return data

    def POST(self):
        data = web.data()
        print data
        #hosts = parse_xml_data(data)
        hosts = parse_string_data(data)
        wn_list = '/opt/glite/yaim/etc/wn-list.conf'
        #wn_list = './wn-list.conf'
        f = open(wn_list, 'a')
        for host in hosts:
            #f.write(host.firstChild.data+'\n')
            f.write(host+'\n')
        f.close()
        
        o,_ = call_command('echo date >> /var/log/gLite3_2ConfigCE.log')
        conf_output,_ = call_command('/opt/glite/yaim/bin/yaim -c -s /opt/glite/yaim/etc/siteinfo/site-info.def -n TORQUE_server -n TORQUE_utils >> /var/log/yaim_torque_config.log')
        
        return 

def parse_ovf():
    myParser = OVFParser.OVFParser()
    f = open("ovf-env.xml", "r").read()
    myParser.parse(f)
    f.close()
    
def parse_xml_data(xml):
    dom = parseString(xml)
    hosts = dom.getElementsByTagName('node')
    return hosts

def parse_string_data(s):
    params = s.split('&')
    hosts = []
    for param in params:
        p = param.split('=')
        if p[0].startswith('ip'):
            lookupoutput,_ = call_command('nslookup '+ p[1] +' | grep name')
            lu_data = lookupoutput.split('\n')
            name = ''
            for l in lu_data:
                print l
                if 'name ' in l:
                    n = l.split('=')
                    print n
                    name = n[1].strip()
                    hosts.append(name)
    print hosts
    return hosts

def get_free_wns():
    output,_ = call_command('pbsnodes')
    data = output.split('\n')
    # create a dictionary {nodename, state}
    free_nodes = {}
    current_host = ''    
    for line in data:
        line = line.strip()
        if line != '':
            if ' = ' not in line:
                current_host = line
            elif 'state = ' in line:
                state = line.replace('state = ', '').strip()
                if state.endswith('free'):
                    free_nodes[current_host] = state
    return free_nodes
    
def call_command(command):
    process = subprocess.Popen(command, shell=True,
                            stdout=subprocess.PIPE,
                            stderr=subprocess.PIPE)
    return process.communicate()

if __name__ == "__main__":
    app = web.application(urls, globals())
    app.run()
