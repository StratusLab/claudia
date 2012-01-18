package com.telefonica.claudia.slm.report;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.log4j.Logger;
import org.restlet.Client;
import org.restlet.data.Protocol;

import org.restlet.data.Reference;
import org.restlet.data.Response;

import org.w3c.dom.Document;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.telefonica.claudia.slm.common.SMConfiguration;

public class PersistenceClient {

    private static  Logger logger = Logger
            .getLogger(PersistenceClient.class);
    private static String TCloudServerURL;

    private final String SITE_ROOT;

    public static final String ROOT_MONITORING_TAG_NAME = "MonitoringInformation";
    public static final String EVENT_TYPE_TAG_NAME = "EventType";
    public static final String T_0_TAG_NAME = "EpochTimestamp";
    public static final String T_DELTA_TAG_NAME = "TimeDelta";
    public static final String FQN_TAG_NAME = "FQN";
    public static final String VALUE_TAG_NAME = "Value";

    private static String restPath;
    private static String restServerPort;
    private static String restServerHost;
    private static String measurementTopicIdentifier;
    private static String namingFactory;
    private static String serverProviderUrl;
    private static String connFactoryName;
    private static String vmMonName;
    private static String monitorName;

    private static HttpClient httpClient = new HttpClient();

    public static final String PATH_TO_PROPERTIES_FILE = "./conf/reportClient.properties";

    private static final Properties properties = new Properties();

    public static Client client;

    public PersistenceClient() {
        try {
            properties.load(new FileInputStream(PATH_TO_PROPERTIES_FILE));
            TCloudServerURL = properties.getProperty("TServer.url");
            SITE_ROOT = properties.getProperty("SiteRoot");
            restPath = properties.getProperty("restPath");
            restServerPort = properties.getProperty("restServerPort");
            restServerHost = properties.getProperty("restServerHost");
            vmMonName = properties.getProperty("vmMonName");
            monitorName = properties.getProperty("monitorName");
        } catch (IOException e) {
            logger.error("Unable to load properties from "
                    + PATH_TO_PROPERTIES_FILE);
            throw new RuntimeException("Unable to load properties from "
                    + PATH_TO_PROPERTIES_FILE);
        }
    }
    
    public PersistenceClient(Logger logger) {
    	this.logger = logger;
        try {
            properties.load(new FileInputStream(PATH_TO_PROPERTIES_FILE));
            TCloudServerURL = properties.getProperty("TServer.url");
            SITE_ROOT = properties.getProperty("SiteRoot");
            restPath = properties.getProperty("restPath");
            restServerPort = properties.getProperty("restServerPort");
            restServerHost = properties.getProperty("restServerHost");
            vmMonName = properties.getProperty("vmMonName");
            monitorName = properties.getProperty("monitorName");
        } catch (IOException e) {
            logger.error("Unable to load properties from "
                    + PATH_TO_PROPERTIES_FILE);
            throw new RuntimeException("Unable to load properties from "
                    + PATH_TO_PROPERTIES_FILE);
        }
    }

    public static void sendRESTMessage(String eventType, long t_0,
            long delta_t, String fqn, double value) {

        String message = "<" + ROOT_MONITORING_TAG_NAME + ">" + "<"
                + EVENT_TYPE_TAG_NAME + ">" + eventType + "</"
                + EVENT_TYPE_TAG_NAME + ">" + "<" + T_0_TAG_NAME + ">" + t_0
                + "</" + T_0_TAG_NAME + ">" + "<" + T_DELTA_TAG_NAME + ">"
                + delta_t + "</" + T_DELTA_TAG_NAME + ">" + "<" + FQN_TAG_NAME
                + ">" + fqn + "</" + FQN_TAG_NAME + ">" + "<" + VALUE_TAG_NAME
                + ">" + value + "</" + VALUE_TAG_NAME + ">" + "</"
                + ROOT_MONITORING_TAG_NAME + ">";

        PostMethod post = new PostMethod("http://" + SMConfiguration.getInstance().getRestServerHost() + ":"
                + SMConfiguration.getInstance().getRestServerPort() + "/vmi");

        RequestEntity request = null;
        try {
            request = new StringRequestEntity(message, "text/xml", null);
        } catch (UnsupportedEncodingException ex) {
            System.out
                    .println("This should never happen? Cannot create a String request entity with null char encoding");
            return;
        }

        post.setRequestEntity(request);

        try {
            httpClient.executeMethod(post);
            System.out.println("\n\tResult status: " + post.getStatusText()
                    + "\n");
        } catch (HttpException ex) {
            System.out
                    .println("HTTPException caught when trying to send POST message: "
                            + ex.getMessage());
            return;
        } catch (IOException ex) {
            System.out
                    .println("IOException caught when trying to send POST message: "
                            + ex.getMessage());
            return;
        } finally {
            post.releaseConnection();
        }
    }

    public static String get(Client client, Reference reference)
            throws IOException {
        client = new Client(Protocol.HTTP);
        Response response = client.get(reference);
        System.out.println(" reference " + reference.getIdentifier());
        if (response.getStatus().isSuccess()) {
            if (response.isEntityAvailable()) {
                return response.getEntity().getText();
            } else {
                return "No response from the server";
            }
        } else {
            System.out.println("GET request didn't succeed");
            return "ERROR";
        }
    }

    public static ArrayList<String> findvdc(String getresponse) {

        ArrayList<String> vdcs = new ArrayList<String>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new ByteArrayInputStream(getresponse
                    .getBytes()));

            NodeList vdcList = doc.getElementsByTagName("Link");

            for (int i = 0; i < vdcList.getLength(); i++) {

                Node node = vdcList.item(i);
                NamedNodeMap atributes = node.getAttributes();
                Node typeAtribute = atributes.getNamedItem("rel");
                if (typeAtribute.getNodeValue().equals("down")) {
                    Node hrefAtribute = atributes.getNamedItem("href");
                    String fqn = hrefAtribute.getNodeValue();
                    vdcs.add(fqn);
                    logger.info("VDC found " + fqn);
                }
            }
        } catch (Exception spe) {
            // Algún tipo de error: fichero no accesible, formato de XML
            // incorrecto, etc.
        }
        return vdcs;
    }

    public static ArrayList<String> findvapps(String getresponse) {

        ArrayList<String> vapps = new ArrayList<String>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new ByteArrayInputStream(getresponse
                    .getBytes()));

            NodeList vappList = doc.getElementsByTagName("Link");

            for (int i = 0; i < vappList.getLength(); i++) {

                Node node = vappList.item(i);
                NamedNodeMap atributes = node.getAttributes();
                Node typeAtribute = atributes.getNamedItem("type");
                if (typeAtribute.getNodeValue().equals(
                        "application/vnd.telefonica.tcloud.vapp+xml")) {
                    Node hrefAtribute = atributes.getNamedItem("href");
                    String fqn = hrefAtribute.getNodeValue();
                    vapps.add(fqn);
                    logger.info("Vapp found " + fqn);
                }
            }
        } catch (Exception spe) {
            // Algún tipo de error: fichero no accesible, formato de XML
            // incorrecto, etc.
        }
        return vapps;
    }

    public static ArrayList<String> findvms(String getresponse) {

        ArrayList<String> vms = new ArrayList<String>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new ByteArrayInputStream(getresponse
                    .getBytes()));

            NodeList vmList = doc.getElementsByTagName("VApp");

            for (int i = 0; i < vmList.getLength(); i++) {

                Node node = vmList.item(i);
                NamedNodeMap atributes = node.getAttributes();
                Node hrefAtribute = atributes.getNamedItem("href");
                String fqn = hrefAtribute.getNodeValue();
                if (fqn.substring(fqn.length() - 2, fqn.length() - 1).equals(
                        "/")) {
                    vms.add(fqn);
                    // logger.info(" VM found " + fqn);
                }
            }
        } catch (Exception spe) {
            // Algún tipo de error: fichero no accesible, formato de XML
            // incorrecto, etc.
        }
        return vms;
    }

    public static ArrayList<String> findmeasures(String monitor)
            throws IOException {

        logger.info("Measures for monitor" + monitor);
        ArrayList<String> measures = new ArrayList<String>();

        int i = monitor.indexOf("/api");
        Reference monitorurl = new Reference(monitor);
        String monxml = get(client, monitorurl);

        // logger.info(" monitor XML  " + monxml);

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new ByteArrayInputStream(monxml
                    .getBytes()));

            NodeList measureList = doc
                    .getElementsByTagName("MeasureDescriptor");

            for (int j = 0; j < measureList.getLength(); j++) {

                Node node = measureList.item(j);
                NamedNodeMap atributes = node.getAttributes();
                Node nameAtribute = atributes.getNamedItem("name");
                String name = nameAtribute.getNodeValue();
                measures.add(name);
                logger.info(" measure found " + name);
            }
        } catch (Exception spe) {
            // Algún tipo de error: fichero no accesible, formato de XML
            // incorrecto, etc.
        }

        return measures;
    }

    public ArrayList<String> findmonitors(List<String> list) throws IOException {

        ArrayList<String> monitors = new ArrayList<String>();
        for (Iterator iterator = list.iterator(); iterator.hasNext();) {
            String vm = (String) iterator.next();
            int i = vm.indexOf("/api");
            String monitorfqn = TCloudServerURL
                    + (vm.substring(i, vm.length()) + "/monitor");
            logger.info("Monitor found: " + monitorfqn);
            monitors.add(monitorfqn);
        }

        return monitors;
    }

    public String getmeasure(String monitor, String measure) throws IOException {

        String value = null;
        Reference ValueURL = new Reference(monitor + "/" + measure + "/"
                + "values");
        String valuexml = get(client, ValueURL);

        return valuexml;
    }

    public static int nthIndexOf(String text, char needle, int n) {
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == needle) {
                n--;
                if (n == 0) {
                    return i;
                }
            }
        }
        return -1;
    }

    public void sendvalue(String valuexml, String monitorfqn, String measure, String type) throws SAXException, IOException, ParserConfigurationException, ParseException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new ByteArrayInputStream(valuexml
                    .getBytes()));

            NodeList valueList = doc.getElementsByTagName("Sample");

            for (int i = 0; i < valueList.getLength(); i++) {

                Node node = valueList.item(i);
                NamedNodeMap atributes = node.getAttributes();
                Node unitAtribute = atributes.getNamedItem("unit");
                String unit = unitAtribute.getNodeValue();
                Node timestampAtribute = atributes.getNamedItem("timestamp");
                String timestamp = timestampAtribute.getNodeValue();
                Node valueAtribute = atributes.getNamedItem("value");
                String value = valueAtribute.getNodeValue();

                // logger.info(" values: " + unit+" "+timestamp+" "+ value);

                String pattern = "yyyy-MM-dd'T'HH:mm:ss";
                SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                Date date = sdf.parse(timestamp);
                // logger.info(" date: " +date);
                MeasuredValue mv = new MeasuredValue(value, date, unit);

                String monitor = getMonitorFQN (monitorfqn, measure, type);
                
                // logger.info("sending : " + monitor);

           //     String replicanamelimit=replica + ".replicas." + number;
            //    String monitornamelimit=measure;

             //   if ((replicanamelimit.equals(vmMonName) || vmMonName.equals("all"))
               //         && (monitornamelimit.equals(monitorName) || monitorName.equals("all"))){

                try { 
                sendRESTMessage(type, mv.getRegisterDate().getTime(), 4,
                        monitor, Double.parseDouble(mv.getValue()));}
                catch (Exception e){}
                // sendRESTMessage("AGENT", mv.getRegisterDate().getTime(), 4,
                // monitorfqn, Double.parseDouble(mv.getValue()));
                logger.info(" monitor: " + monitor + " " + measure);
                logger.info("Sending values: " + unit + " " + timestamp + " " + value);
                
            }
        
    }
    
    public void sendvalueVEEHW(String valuexml, String monitorfqn, String measure, String type) throws SAXException, IOException, ParserConfigurationException, ParseException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new ByteArrayInputStream(valuexml
                    .getBytes()));

            NodeList valueList = doc.getElementsByTagName("Sample");

            for (int i = 0; i < valueList.getLength(); i++) {

                Node node = valueList.item(i);
                NamedNodeMap atributes = node.getAttributes();
                Node unitAtribute = atributes.getNamedItem("unit");
                String unit = unitAtribute.getNodeValue();
                Node timestampAtribute = atributes.getNamedItem("timestamp");
                String timestamp = timestampAtribute.getNodeValue();
                Node valueAtribute = atributes.getNamedItem("value");
                String value = valueAtribute.getNodeValue();

                // logger.info(" values: " + unit+" "+timestamp+" "+ value);

                String pattern = "yyyy-MM-dd'T'HH:mm:ss";
                SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                Date date = sdf.parse(timestamp);
                // logger.info(" date: " +date);
                MeasuredValue mv = new MeasuredValue(value, date, unit);

                int j = monitorfqn.indexOf("vdc");
                int k = monitorfqn.indexOf("vapp");
                String customer = monitorfqn.substring(j + 4, k - 1);

                int l = nthIndexOf(monitorfqn, '/', 10);

                // logger.info("k : " + k);
                // logger.info("l : " + l);
                // logger.info("customer : " + customer);

                String service = monitorfqn.substring(k + 5, l);

                // logger.info("service : " + service);

                int m = nthIndexOf(monitorfqn, '/', 11);
                String replica = monitorfqn.substring(l + 1, m);

                // logger.info("replica : " + replica);

                int n = nthIndexOf(monitorfqn, '/', 12);
                String number = monitorfqn.substring(m + 1, n);

                // logger.info("number : " + number);

                String monitor = SITE_ROOT + ".customers." + customer
                        + ".services." + service + ".vees." + replica
                        + ".replicas." + number + ".kpis." + measure;
                // logger.info("sending : " + monitor);

                String replicanamelimit=replica + ".replicas." + number;
                String monitornamelimit=measure;

             //   if ((replicanamelimit.equals(vmMonName) || vmMonName.equals("all"))
               //         && (monitornamelimit.equals(monitorName) || monitorName.equals("all"))){

                try { 
                sendRESTMessage(type, mv.getRegisterDate().getTime(), 4,
                        monitor, Double.parseDouble(mv.getValue()));}
                catch (Exception e){}
                // sendRESTMessage("AGENT", mv.getRegisterDate().getTime(), 4,
                // monitorfqn, Double.parseDouble(mv.getValue()));
                logger.info(" monitor: " + monitor + " " + measure);
                System.out.println("Sending values: " + unit + " " + timestamp + " " + value);
                
            }
        
    }

    public List<String> getVMs() throws IOException {

        ArrayList<String> result = new ArrayList<String>();
        ArrayList<String> vdcs = new ArrayList<String>();

        client = new Client(Protocol.HTTP);
        Reference TcloudURL = new Reference(TCloudServerURL + "/api/org/"
                + SITE_ROOT);
        String url = get(client, TcloudURL);

        logger.info("Tcloud URL: " + TcloudURL);
        vdcs = findvdc(url);

        for (Iterator iterator = vdcs.iterator(); iterator.hasNext();) {
            String vdc = (String) iterator.next();
            ArrayList<String> vapps = new ArrayList<String>();
            int i = vdc.indexOf("/api");
            String vdcfqn = TCloudServerURL + vdc.substring(i, vdc.length());
            Reference vdcURL = new Reference(vdcfqn);
            // logger.info(" VDC: " + vdcURL);
            String vappxml = get(client, vdcURL);
            // logger.info(" GET VM: " + vmurl);
            vapps = findvapps(vappxml);

            for (Iterator iterator2 = vapps.iterator(); iterator2.hasNext();) {
                String vapp = (String) iterator2.next();
                ArrayList<String> vms = new ArrayList<String>();
                int j = vapp.indexOf("/api");
                String vappfqn = TCloudServerURL
                        + vapp.substring(j, vapp.length());
                Reference vmurl = new Reference(vappfqn);
                String vmxml = get(client, vmurl);
                vms = findvms(vmxml);

                for (Iterator iterator3 = vms.iterator(); iterator3.hasNext();) {
                    String vm = (String) iterator3.next();
                    int k = vm.indexOf("/api");
                    String vmfqn = TCloudServerURL
                            + vm.substring(k, vm.length());
                    logger.info("VM found " + vmfqn);
                    result.add(vmfqn);

                }

            }

        }

        return result;
    }
    
    private String getMonitorFQN (String monitorfqn, String measure, String type)
    {
    	String monitor = null; 
    	String customer = monitorfqn.substring(monitorfqn.indexOf("vdc") +1+ "vdc".length(), monitorfqn.indexOf("vapp") - 1);

        int l = nthIndexOf(monitorfqn, '/', 10);

        // logger.info("k : " + k);
        // logger.info("l : " + l);
        // logger.info("customer : " + customer);

        String service = monitorfqn.substring(monitorfqn.indexOf("vapp") + 5, l);
        
        System.out.println ("customer " + customer + " " + "service " + service);

        // logger.info("service : " + service);
        
        
        if (type.equals("AGENT"))
        {
         
          monitor = SITE_ROOT + ".customers." + customer
          + ".services." + service + ".kpis." + measure;
        }
        
        else 
        {
          int m = nthIndexOf(monitorfqn, '/', 11);
          String vee = monitorfqn.substring(l + 1, m);

          int n = nthIndexOf(monitorfqn, '/', 12);
          String replica = monitorfqn.substring(m + 1, n);
          
          monitor = SITE_ROOT + ".customers." + customer
          + ".services." + service + ".vees." + vee
          + ".replicas." + replica + ".kpis." + measure;
        }

        return monitor;
    }

    public static void main(String[] args) {
        // PersistenceClient pc = new PersistenceClient();
        // String fqn =
        // pc.getFqnNIC("http://10.95.129.34:8183/api/org/tid34/vdc/joseldCPD/vapp/joseldServ/AnaVM1/1/hw/networks-4001/monitor/values?measures=netPacketstxSummation,netInput,netOutput,netPacketsrxSummation&from=2011-02-03T11:48:00Z&to=2011-02-03T12:18:00Z&interval=5m");
        // List<String> nics = pc.getNics();
        // System.out.println("Nic"+nics.size());

    }
}
