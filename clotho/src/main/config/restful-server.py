#!/usr/bin/python

import web
import os, subprocess, fileinput
from xml.dom.minidom import parseString

urls = ('/(.*)', 'CEServer')

class CEServer(object):
    
    def GET(self, num):
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
        o,_ = call_command('date >> /var/log/yaim_torque_config.log')
        conf_output,_ = call_command('/opt/glite/yaim/bin/yaim -c -s /opt/glite/yaim/etc/siteinfo/site-info.def -n TORQUE_server -n TORQUE_utils >> /var/log/yaim_torque_config.log')
        
        return data

    def POST(self, arg):
        data = web.data()
    	print 'Data received: '+data
        #hosts = parse_xml_data(data)
        hosts = []
        if len(data) > 0:
            hosts = parse_string_data(data)
        else:
            hosts = parse_string_data(arg)

        print hosts
        wn_list = '/opt/glite/yaim/etc/wn-list.conf'
        f = open(wn_list, 'a')
        skip = False
        for host in hosts:
            for line in fileinput.FileInput(wn_list):
                if line.startswith(host):
                    skip = True
                    print 'Node '+ host +' already included in the wn-list.conf'
                    break;
            if not skip:
                f.write(host+'\n')
        f.close()
        
        o,_ = call_command('date >> /var/log/yaim_torque_config.log')
        conf_output,_ = call_command('/opt/glite/yaim/bin/yaim -c -s /opt/glite/yaim/etc/siteinfo/site-info.def -n TORQUE_server -n TORQUE_utils >> /var/log/yaim_torque_config.log')
        
        return 

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
            print 'nslookup '+ p[1] +' | grep name'
            lookupoutput,_ = call_command('nslookup '+ p[1] +' | grep name')
            lu_data = lookupoutput.split('\n')
            name = ''
            for l in lu_data:
                print l
                if 'name ' in l:
                    n = l.split('=')
                    print n
                    name = n[1].strip()
                    if name.endswith('.'):
                        name = name[:-1]
                    hosts.append(name)
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
