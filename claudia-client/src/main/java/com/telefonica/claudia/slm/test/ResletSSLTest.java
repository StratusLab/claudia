package com.telefonica.claudia.slm.test;

import java.io.UnsupportedEncodingException;

import junit.framework.TestCase;

import org.apache.http.HttpEntity;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.restlet.Client;
import org.restlet.data.Method;
import org.restlet.data.Protocol;
import org.restlet.data.Request;
import org.restlet.data.Response;

public class ResletSSLTest extends TestCase {

    public static final String URI = "https://localhost:8443/";
    
 	private static DefaultHttpClient httpClient = new DefaultHttpClient();
/*	static {
         httpClient.getCredentialsProvider().setCredentials(
                 new AuthScope("localhost", 8443),
                 new UsernamePasswordCredentials("username", "password"));
	}*/

	
    public void testPostSSLHttpClient() throws Exception {
        // Define our Restlet HTTPS client.
     //   Request request = new Request(Method.POST, URI);
        //domrep.getText();
		String message="<Disk ovf:capacity=\"1536MB\" ovf:diskId=\"storageelement\" ovf:fileRef=\"storageelement\" ovf:format=\"http://www.gnome.org/~markmc/qcow-image-format.html\"/>";
   
    	HttpPost post= new HttpPost("http://" + "localhost" + ":" + 8443 + "/");
	
    	HttpEntity request = null;		
		try {
			request = new StringEntity(message, "text/xml", null);
		} catch (UnsupportedEncodingException ex) {
			System.out.println("This should never happen? Cannot create a String request entity with null char encoding");
			return;
		}
		post.setEntity(request);
		
    	
    //	Response response = client.post(serviceItemsUri + "?serviceName=" + serviceName, domrep);
    	
     //   assertEquals(200, response.getStatus().getCode());
    }
/*	 
   public void testPostSSL() throws Exception {
        // Define our Restlet HTTPS client.
        Request request = new Request(Method.POST, URI);

        Client client = new Client(Protocol.HTTP);
        Response response = client.handle(request);
        assertEquals(200, response.getStatus().getCode());
    }

    public void testGetSSL() throws Exception {
        // Define our Restlet HTTPS client.
        Request request = new Request(Method.GET, URI);

        Client client = new Client(Protocol.HTTP);
        Response response = client.handle(request);
        assertEquals(200, response.getStatus().getCode());
    }*/

}
