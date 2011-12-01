
public class ScriptTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String scriptListProp  = "OVFParser.py/torqueProbe.py/restful-server.py/netinit1.sh";
		String[] scriptList = scriptListProp.split("/");

		String scriptListTemplate = "";

		for (String scrt: scriptList){
			
			if (scrt.indexOf(".py")!=-1)
			{

			  System.out.println ("python /mnt/stratuslab/"+scrt);
			}
		}

	}

}
