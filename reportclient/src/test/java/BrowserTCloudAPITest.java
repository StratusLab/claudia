import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.restlet.Client;
import org.restlet.data.Protocol;
import org.restlet.data.Reference;

import com.telefonica.claudia.slm.monitoring.PersistenceClient;
public class BrowserTCloudAPITest {
	public static void main (String [] args) throws IOException
	{
		PersistenceClient per = new PersistenceClient();
		ArrayList<String>vdcs = new ArrayList<String>();
		ArrayList<String> result = new ArrayList<String>();

		Client client = new Client(Protocol.HTTP);
		Reference  TcloudURL  = new Reference("http://62.217.120.136:8182/api/org/grnet");
		String url = null;
		try {
			url = per.get(client,TcloudURL);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		vdcs=per.findvdc(url);

		for (Iterator iterator = vdcs.iterator(); iterator.hasNext();) {
			String vdc = (String) iterator.next();	
			System.out.println ("vdc " + vdc);
			ArrayList<String> vms = new ArrayList<String>();
			int i = vdc.indexOf("/api");
			String vdcfqn = "http://62.217.120.136:8182"+vdc.substring(i,vdc.length());
			Reference  vdcURL  = new Reference(vdcfqn);
			//	    logger.info("PONG VDC: " + vdcURL);
			String vmurl=per.get(client,vdcURL);
			
			System.out.println (vmurl);
			//		logger.info("PONG GET VM: " + vmurl); 
			
			vms=per.findvms(vmurl);

			for (Iterator iterator2 = vms.iterator(); iterator2.hasNext();) {
				
				String vm = (String) iterator2.next();	
				System.out.println ("serv " + vm);
				int j = vm.indexOf("/api");
				result.add("http://62.217.120.136:8182"+vm.substring(j,vm.length()));
			}

		}

		for (Iterator iterator = result.iterator(); iterator.hasNext();) {
			String res = (String) iterator.next();	
			System.out.println("VMs found: " + res); 

		}
	}
	
	
	
	

}
