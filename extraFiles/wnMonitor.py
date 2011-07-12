__author__ = 'vangelis'

import time
import socket
import fileinput
import sys
import string
import logging
import subprocess

LOG_FILE = "/var/log/wnMonitor.log"
SSH_PORT = 22
SSH_TIMEOUT = 900
WN_POLL_INTERVAL = 30

logging.basicConfig(filename=LOG_FILE, level=logging.DEBUG, format="%(asctime)s - %(levelname)s - %(message)s")

WN_LIST_CONF = "/opt/glite/yaim/etc/wn-list.conf"

def waitForConnectivity(host, port, timeout):
    start_time = time.time()
    connected = False

    while (time.time() - start_time) < timeout and not connected:
        try:
            ssh_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
            ssh_socket.connect((host, port))
            connected = True
        except socket.error:
            ssh_socket.close()
            ssh_socket = None

    if ssh_socket == None:
        return False
    else:
        ssh_socket.close()
        return True


def checkWNConnectivity(wnList):
    for wn in wnList:
        logging.debug("Trying to connect to " + wn)

        if waitForConnectivity(wn, SSH_PORT, SSH_TIMEOUT):
            logging.debug("SSH server is up")
        else:
            logging.debug("SSH server timed out!")


def getWNs():
    wnList = fileinput.input(WN_LIST_CONF)
    temp_wn_list = []

    for wn in wnList:
        if wn != "\n":
            wn = string.rstrip(wn, "\n")
            temp_wn_list.append(wn)

    wnList.close()
    return temp_wn_list


def pollWNs(old_wn_list):
    new_wn_list = getWNs()
    return new_wn_list, list(set(new_wn_list) - set(old_wn_list))

def yaimReconfigure():
    conf_output,_ = call_command('/opt/glite/yaim/bin/yaim -c -s /opt/glite/yaim/etc/siteinfo/site-info.def -n TORQUE_server -n TORQUE_utils >> /var/log/yaim_torque_config.log')
    return 0

def call_command(command):
    process = subprocess.Popen(command, shell=True,
                            stdout=subprocess.PIPE,
                            stderr=subprocess.PIPE)
    return process.communicate()


def main():
    logging.info("Started Worker Node monitoring")

    wn_list = getWNs()

    checkWNConnectivity(wn_list)

    logging.info("Running yaim TORQUE reconfigure for the first time")

    yaimReconfigure()

    while True:
        time.sleep(WN_POLL_INTERVAL)
        allWNs, newWNs = pollWNs(wn_list)

        if not newWNs:
            logging.info("No new WN(s) found")
        else:
            logging.info("New WN(s) found:" + str(newWNs))
            checkWNConnectivity(newWNs)
            logging.info("Running yaim TORQUE reconfigure...")
            yaimReconfigure()

        wn_list = allWNs

    return 0

if __name__ == "__main__":
    sys.exit(main())