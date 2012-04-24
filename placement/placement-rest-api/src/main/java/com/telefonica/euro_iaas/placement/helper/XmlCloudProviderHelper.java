package com.telefonica.euro_iaas.placement.helper;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.PropertyResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.activation.UnsupportedDataTypeException;

import com.telefonica.euro_iaas.placement.model.provider.CloudProvider;
import com.telefonica.euro_iaas.placement.model.provider.VEE;

public class XmlCloudProviderHelper {
	
	/**
	 * 
	 */
	private final static Logger log=Logger.getLogger(XmlCloudProviderHelper.class.getName());
	/**
	 * Nombre de fichero de CloudProviders por defecto.
	 */
	private String cloudProvidersFileName="/tmp/mf.xml";
	
	/**
	 * 
	 */
	private static XmlCloudProviderHelper xmlHelper=null;
	/**
	 * Constructor por defecto
	 */
	private XmlCloudProviderHelper() {
		try {
			PropertyResourceBundle prb=new PropertyResourceBundle(
					new InputStreamReader(getClass().getClassLoader().getResourceAsStream("placement.properties")));
			cloudProvidersFileName=prb.getString("placement.context.vdcFileName");
		} catch (Exception e) {
			log.log(Level.WARNING,"Usando configuracion por defecto:{0} por {1}",
					new String [] {cloudProvidersFileName, e.getMessage()});
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public static XmlCloudProviderHelper getInstance() {
		if (xmlHelper==null) {
			xmlHelper=new XmlCloudProviderHelper();			
			log.log(Level.INFO, "Fichero de configuracion: {0}", xmlHelper.cloudProvidersFileName);
		}
		return xmlHelper;
	}
	
	/**
	 * Para leer un fichero codificado de CloudProviders y obtener una lista.
	 * @return
	 * @throws FileNotFoundException
	 * @throws UnsupportedDataTypeException
	 */
	public Set<CloudProvider> decode() throws FileNotFoundException, UnsupportedDataTypeException {		
		HashSet<CloudProvider> set=new HashSet<CloudProvider>();
		XMLDecoder decoder=new XMLDecoder(new BufferedInputStream(
				new FileInputStream(cloudProvidersFileName)));
		CloudProvider cp=null;
		VEE vee;	
		try {
			while (true) {
				Object o = decoder.readObject();
				if (o instanceof CloudProvider) {
					cp=(CloudProvider)o;
					cp.setVees(new HashSet<VEE>());
					set.add(cp);
				} else if (o instanceof VEE) {
					vee=(VEE)o;
					vee.setCloudProvider(cp);
					cp.getVees().add(vee);
				} else {
					throw new UnsupportedDataTypeException("Unexpected " + o.getClass().getName() + " found");
				}
			}
		} catch (ArrayIndexOutOfBoundsException aiob) {}
		finally {
			decoder.close();
		}
		return set;
	}
	
	/**
	 * Para Codificar un conjunto de CloudProviders
	 * 
	 * @param cps
	 * @throws FileNotFoundException
	 */
	public void encode(Set<CloudProvider> cps) throws FileNotFoundException {
		XMLEncoder encoder=new XMLEncoder(
				new BufferedOutputStream(
						new FileOutputStream(cloudProvidersFileName)));
		for (CloudProvider cp : cps) {
			encoder.writeObject(cp);
			Set<VEE> veeSet=cp.getVees();
			for (VEE vee : veeSet) {
				encoder.writeObject(vee);
			}
		}
		encoder.close();
	}
}
