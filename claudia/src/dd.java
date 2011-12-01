
public class dd {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String sCadena = "1047=public=VLAN225";
		String codigo = sCadena.substring(0,sCadena.indexOf("="));
		System.out.println(codigo);
	

	}

}
