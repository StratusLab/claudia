package com.telefonica.claudia.slm.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Properties;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.IOUtils;
import org.restlet.Client;
import org.restlet.Context;
import org.restlet.data.ChallengeResponse;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Protocol;
import org.restlet.data.Reference;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.DomRepresentation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;




public class Test {
	public static final String RESERVOIR_PATH="/api/org/es_tid/vdc";

	private final static String PATH_TO_PROPERTIES_FILE="conf/claudiaClient.properties";
	private final static String PATH_TO_ACD_TEST="conf/";

	private final static String CUSTOMER_NAME="customer.name";
	private static final String HOST = "smi.host";
	private static final String KEY_PORT = "smi.port";
	private static final String KEY_INSTANTIATE = "smi.instantiationURI";	
	private static final String SERVER_TIMEOUT="connection.timeout";
	private static final String REST_PATH = "rest.path";
	private static final String REST_SERVER_PORT = "rest.port";
	private static final String REST_HOST = "rest.host";

	private static final String ACD_HOST = "acd.host";
	private static final String ACD_PORT = "acd.port";

	private static String CLAUDIA_AUTH="claudia.auth";
	private static String  CLAUDIA_PROXY="claudia.proxy";
	private static  String  CLAUDIA_CERT_PATH ="claudia.certpath";
	private static String  CLAUDIA_CERT="claudia.cert";
	private static String  CLAUDIA_CERT_TYPE="claudia.certtype";	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		
		Properties prop = new Properties();
		int serverTimeout;

		try {
			prop.load(new FileInputStream(PATH_TO_PROPERTIES_FILE));			
			serverTimeout = Integer.parseInt(prop.getProperty(SERVER_TIMEOUT));

		} catch (FileNotFoundException e) {
			System.out.println("Properties file not found. Expected path: " + PATH_TO_PROPERTIES_FILE);
		 return;
		} catch (IOException e) {
			System.out.println("Error reading properties file. Expected path: " + PATH_TO_PROPERTIES_FILE);
			return;
		} catch (NumberFormatException nfe) {
			serverTimeout = 30;
		}
		
		String claudiaAuth=prop.getProperty(CLAUDIA_AUTH);
		String claudiaProxy=prop.getProperty(CLAUDIA_PROXY);
		String login=prop.getProperty("login");
		String password=prop.getProperty("password");

		
		SSLContext ctx = null;
		try {
			ctx = SSLContext.getInstance("TLS");
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			ctx.init(new KeyManager[0], new TrustManager[] {new DefaultTrustManager()}, new SecureRandom());
		} catch (KeyManagementException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		SSLContext.setDefault(ctx);

		// Define our Restlet HTTPS client.
		//	        Reference reference = new Reference(claudiaProxy);
		//
		//	        Request request = new Request(Method.POST, reference);
		//
		//	        request.setChallengeResponse(new ChallengeResponse(
		//	                ChallengeScheme.HTTP_BASIC, "username", "password"));
		
		System.out.println ("Loading certifciates...");

		File keystoreFile = new  File("/opt/claudia/extraFiles/user/","stratuslab-test-user.p12");

		System.setProperty("javax.net.ssl.trustStore",
				keystoreFile.getAbsolutePath());
		System.setProperty("javax.net.ssl.trustStorePassword",
		"changeit");
		System.setProperty("javax.net.ssl.keyStoreType", "pkcs12");
		System.setProperty("javax.net.ssl.keyStore",
				keystoreFile.getAbsolutePath());
		System.setProperty("javax.net.ssl.keyStorePassword",
		"changeit");

		Client client = new Client(new Context(), Protocol.HTTPS);
		
		System.out.println ("Getting ovf...");
		Document docOvf = null;
		try {
			docOvf = getOVF("http://84.21.173.141:8080/telefonica.xml", "nombreservicio");
		} catch (SAXException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ParserConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		DomRepresentation domrep = new DomRepresentation(MediaType.APPLICATION_XML, docOvf);
		
		
		
		// TODO Auto-generated method stub
		Response response = null;
		
		System.out.print ("creating reference y request");
		
			Reference reference = new Reference(claudiaProxy+"/api/org/es_tid/vdc/s2/action/instantiateOvf");
			Request request = new Request(Method.POST, reference);
			
			request.setChallengeResponse(new ChallengeResponse(
					ChallengeScheme.HTTP_BASIC,login, password));
			request.setEntity(domrep);
			try {
				System.out.println (" in the get Entity" + request.getEntity().getText());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				System.out.print ("calling client");
				response = client.handle(request);
				String reply = IOUtils.toString(response.getEntity().getStream());
				System.out.println("Status: "+response.getStatus());
				System.out.println(response.getRedirectRef());
			//	System.out.println("Reply: "+reply);
				System.out.println("isSucces: "+response.getStatus().isSuccess());
			}
			catch (NullPointerException npe) {
				System.out.println("No response from proxy");
				npe.printStackTrace();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
			System.out.println ("GET OPERATION");
			
			reference = new Reference(claudiaProxy+"/api/org/es_tid/vdc/s2");
			 request = new Request(Method.GET, reference);
			
			request.setChallengeResponse(new ChallengeResponse(
					ChallengeScheme.HTTP_BASIC,login, password));
			//request.setEntity(domrep);
			
			try {
				System.out.print ("calling client");
				response = client.handle(request);
				String reply = IOUtils.toString(response.getEntity().getStream());
				System.out.println("Status: "+response.getStatus());
				System.out.println(response.getRedirectRef());
			//	System.out.println("Reply: "+reply);
				System.out.println("isSucces: "+response.getStatus().isSuccess());
			}
			catch (NullPointerException npe) {
				System.out.println("No response from proxy");
				npe.printStackTrace();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
           System.out.println ("DELETE OPERATION");
			
			reference = new Reference(claudiaProxy+"/api/org/es_tid/vdc/s2");
			 request = new Request(Method.DELETE, reference);
			
			request.setChallengeResponse(new ChallengeResponse(
					ChallengeScheme.HTTP_BASIC,login, password));
			//request.setEntity(domrep);
			
			try {
				System.out.print ("calling client");
				response = client.handle(request);
			//	String reply = IOUtils.toString(response.getEntity().getStream());
				System.out.println("Status: "+response.getStatus());
				System.out.println(response.getRedirectRef());
			//	System.out.println("Reply: "+reply);
				System.out.println("isSucces: "+response.getStatus().isSuccess());
			}
			catch (NullPointerException npe) {
				System.out.println("No response from proxy");
				npe.printStackTrace();

			} 

		
		
		

	}

private static Document getOVF (String urlovf, String serviceName) throws SAXException, IOException, ParserConfigurationException
{
	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	DocumentBuilder db = dbf.newDocumentBuilder();

	String s;
	StringBuffer completeOVF = new StringBuffer();
	URL ovfURL = new URL(urlovf);
	InputStream is;

	try {
		is = ovfURL.openStream();

		BufferedReader buf = new BufferedReader(new InputStreamReader(is));
		while ((s = buf.readLine()) != null) {                 
			completeOVF.append(s);                            
		}
		is.close();
	} catch (IOException ex) {

		System.out.println("It was imposible to get an OVF from that URL");
		return null;
	}

	Document doc = db.parse(urlovf);

	DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
	DocumentBuilder docBuilder;

	try {
		docBuilder = dbfac.newDocumentBuilder();
	} catch (ParserConfigurationException e) {
		System.out.println("Error configuring a XML Builder.");
		return null;
	}

	Document docOvf = docBuilder.newDocument();
	Element root = docOvf.createElement("InstantiateOVFParams");
	docOvf.appendChild(root);
	root.setAttribute("name", serviceName);

	root.appendChild(docOvf.importNode(doc.getDocumentElement(), true));
	return docOvf;
	}

private static class DefaultTrustManager implements X509TrustManager {


	public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}


	public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}


	public X509Certificate[] getAcceptedIssuers() {
		return null;
	}

}

}
