/*
* Claudia Project
* http://claudia.morfeo-project.org
*
* (C) Copyright 2010 Telefonica Investigacion y Desarrollo
* S.A.Unipersonal (Telefonica I+D)
*
* See CREDITS file for info about members and contributors.
*
* This program is free software; you can redistribute it and/or modify
* it under the terms of the Affero GNU General Public License (AGPL) as 
* published by the Free Software Foundation; either version 3 of the License, 
* or (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the Affero GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
*
* If you want to use this software an plan to distribute a
* proprietary application in any way, and you are not licensing and
* distributing your source code under AGPL, you probably need to
* purchase a commercial license of the product. Please contact
* claudia-support@lists.morfeo-project.org for more information.
*/


/**
 * SCA component in order to analyze deployment descriptor using business criteria
 * @author  David Perales
 * @version 0.1
 */
package com.telefonica.claudia.slm.serviceconfiganalyzer;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.rules.InvalidRuleSessionException;
import javax.rules.StatelessRuleSession;
import javax.rules.admin.RuleAdministrator;

import com.telefonica.claudia.slm.deployment.GeographicDomain;
import com.telefonica.claudia.slm.deployment.ServiceApplication;
import com.telefonica.claudia.slm.deployment.VEE;


/**
 * This is a sample file to launch a rule package from a rule source file.
 */
public class ServiceConfigurationAnalyzer {

    private final String RULE_SERVICE_PROVIDER = "http://drools.org/";
    private final String RULE_URI = "./rulesHame/sca.drl";
    private StatelessRuleSession statelessRuleSession;
    private RuleAdministrator ruleAdministrator;
    private boolean clean = false;
    
    // FIXME: sites and buildingBlocks must be removed once the BIM database comes into play
    private Vector<SiteEntry> sites = new Vector<SiteEntry>(); 
    private Vector<BuildingBlockEntry> buildingBlocks = new Vector<BuildingBlockEntry>(); 
    private Vector<QosEntry> qoSs = new Vector<QosEntry>();
    
    // FIXME: remove this method with BIM database comes into play
    private void scaPopulationForTesting () {
    	
    	// sync with http://reservoir.cs.ucl.ac.uk/twiki/bin/view/Reservoir/BuildingBlocks
    	buildingBlocks.add(new BuildingBlockEntry("small", ScaConfiguration.getInstance().getSmallCpu(), ScaConfiguration.getInstance().getSmallMem()));
    	buildingBlocks.add(new BuildingBlockEntry("small_highmem", ScaConfiguration.getInstance().getSmallHCpu(), ScaConfiguration.getInstance().getSmallHMem()));
    	buildingBlocks.add(new BuildingBlockEntry("medium", ScaConfiguration.getInstance().getMediumCpu(), ScaConfiguration.getInstance().getMediumMem()));
    	buildingBlocks.add(new BuildingBlockEntry("medium_highmem", ScaConfiguration.getInstance().getMediumHCpu(), ScaConfiguration.getInstance().getMediumHMem()));
    	buildingBlocks.add(new BuildingBlockEntry("large", ScaConfiguration.getInstance().getLargeCpu(), ScaConfiguration.getInstance().getLargeMem()));
    	buildingBlocks.add(new BuildingBlockEntry("large_highmem", ScaConfiguration.getInstance().getLargeHCpu(), ScaConfiguration.getInstance().getLargeHMem()));
    	
    	// sync with http://reservoir.cs.ucl.ac.uk/twiki/bin/view/Reservoir/BuildingBlocks
    	qoSs.add(new QosEntry("silver",ScaConfiguration.getInstance().getSilver()));
    	qoSs.add(new QosEntry("gold",ScaConfiguration.getInstance().getGold()));
    	qoSs.add(new QosEntry("platinum",ScaConfiguration.getInstance().getPlatinum()));
    	
    	// sync with http://reservoir.cs.ucl.ac.uk/twiki/bin/view/Reservoir/SiteInformation
    	sites.add(new SiteEntry(ScaConfiguration.getInstance().getThalesSite(), "FR"));
    	sites.add(new SiteEntry(ScaConfiguration.getInstance().getMessinaSite(), "IT"));
    	sites.add(new SiteEntry(ScaConfiguration.getInstance().getUmeaSite(), "SE"));
    	sites.add(new SiteEntry(ScaConfiguration.getInstance().getIbmSite(), "IL"));
    	  
    }

    public ServiceConfigurationAnalyzer() throws Exception // the constructor name obviously changes based on class name
    {
    	// FIXME: the population step should be removed when the BIM database comes into play
    	ScaConfiguration.loadProperties();
    	scaPopulationForTesting();    	
    	
    	//prepare();
    }

     
    public ServiceApplication refineService(ServiceApplication sa) throws InvalidRuleSessionException, RemoteException {


    	java.util.logging.Logger.getLogger(ServiceConfigurationAnalyzer.class.getName()).log(java.util.logging.Level.INFO, "SCA: Preparing rules engine");
   	 	//SpJpaController spDAO = new SpJpaController();
    	//Sp sp = new Sp();
    	
    	java.util.logging.Logger.getLogger(ServiceConfigurationAnalyzer.class.getName()).log(java.util.logging.Level.INFO, "SCA Searching customer: " + sa.getCustomer().getCustomerName());
    	//sp=  spDAO.findBySpName(sa.getCustomer().getCustomerName());
    	//java.util.logging.Logger.getLogger(ServiceConfigurationAnalyzer.class.getName()).log(java.util.logging.Level.INFO, "Found customer: " + sp.getSpName() + " with ID: " + sp.getSpId());
    	
    	for (Iterator<VEE> it = sa.getVEEs().iterator(); it.hasNext();) 
    	{
            VEE vee = (VEE)it.next();
            java.util.logging.Logger.getLogger(ServiceConfigurationAnalyzer.class.getName()).log(java.util.logging.Level.INFO, "VEE: " + vee.getVEEName() + " with initReplicas: " + vee.getInitReplicas());
    	}
    	
    	ArrayList<Object> objectList = new ArrayList<Object>();
        objectList.add(sa);
        //objectList.add(sp);
        statelessRuleSession.executeRules(objectList);
        for (Iterator<VEE> it = sa.getVEEs().iterator(); it.hasNext();) 
    	{
            VEE vee = (VEE)it.next();
            java.util.logging.Logger.getLogger(ServiceConfigurationAnalyzer.class.getName()).log(java.util.logging.Level.INFO, "VEE: " + vee.getVEEName() + " with initReplicas: " + vee.getInitReplicas());          
    	}
    	
        return sa;

    }

    protected void finalize() throws Throwable {
        if (!clean) {
            cleanUp();
        }
    }
	
	/*
    private void prepare() throws Exception {
    	
        RuleServiceProviderManager.registerRuleServiceProvider(RULE_SERVICE_PROVIDER, RuleServiceProviderImpl.class);
        RuleServiceProvider ruleServiceProvider = RuleServiceProviderManager.getRuleServiceProvider(RULE_SERVICE_PROVIDER);

        ruleAdministrator = ruleServiceProvider.getRuleAdministrator();

        LocalRuleExecutionSetProvider ruleSetProvider = ruleAdministrator.getLocalRuleExecutionSetProvider(null);
        InputStream rules = this.getClass().getResourceAsStream(RULE_URI);
        RuleExecutionSet ruleExecutionSet = ruleSetProvider.createRuleExecutionSet(rules, null);
        //RuleExecutionSet ruleExecutionSet = ruleAdministrator.getLocalRuleExecutionSetProvider(null).createRuleExecutionSet(rules, null);
        rules.close();

        ruleAdministrator.registerRuleExecutionSet(RULE_URI, ruleExecutionSet, null);
        RuleRuntime ruleRuntime = ruleServiceProvider.getRuleRuntime();
        statelessRuleSession = (StatelessRuleSession) ruleRuntime.createRuleSession(RULE_URI, null, RuleRuntime.STATELESS_SESSION_TYPE);
    }
    */
    
    public void cleanUp() throws Exception {
        clean = true;
        statelessRuleSession.release();
        ruleAdministrator.deregisterRuleExecutionSet(RULE_URI, null);
    }

    public static class Message {

        public static final int HELLO = 0;
        public static final int GOODBYE = 1;
        private String message;
        private int status;

        public String getMessage() {
            return this.message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getStatus() {
            return this.status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
    
    /* New methods needed for Y2 functionality */
    
	public HashMap<String,String> getBuildingBlockCapacityLabel(ServiceApplication sa) throws Exception {
		
		HashMap<String,String> hm = new HashMap<String,String>(); 
		
		for ( Iterator<VEE> i = sa.getVEEs().iterator(); i.hasNext() ; ) {
			VEE vee = i.next();
			
			int memory = vee.getMemoryConf().getCapacity();
			int numberCpus = vee.getCPUsConf().size();
			
			/* Search in the list of available building blocks */
			String label = null;
			for (Iterator<BuildingBlockEntry> ii =  buildingBlocks.iterator(); ii.hasNext() ; ) {
				BuildingBlockEntry bb = ii.next();
				// FIXME: consider the possibility of not exact matching
				if (memory == bb.getMemory() && numberCpus == bb.getCpuNumber()) {
					label = bb.getSizingLabel();
				}
				
			}
			
			if (label == null) {
				throw new Exception ("no building block (memory: "+memory+", CPUs: "+numberCpus+") can be found for VEE " + vee.getVEEName());
			}
			else {
				hm.put(vee.getVEEName(), label);
			}
		}
		
		return hm;
		
	}

	public HashMap<String,String> getBuildingBlockAvailabilyLabel(ServiceApplication sa) throws Exception {
		
		HashMap<String,String> hm = new HashMap<String,String>(); 
		
		for ( Iterator<VEE> i = sa.getVEEs().iterator(); i.hasNext() ; ) {
			VEE vee = i.next();

			double availability = vee.getAvailabilityValue();
			
			/* Search in the list of availabilities */
			String label = null;
			for (Iterator<QosEntry> ii =  qoSs.iterator(); ii.hasNext() ; ) {
				QosEntry q = ii.next();
				// FIXME: consider the possibility of not exact matching
				if (availability == q.getQosLevel()) { 
					label = q.getQosLabel();
				}
			}
			
			if (label == null) {
				throw new Exception ("no QoS level (" + availability + ")can be found for VEE " + vee.getVEEName());
			}
			else {
				hm.put(vee.getVEEName(), label);
			}
			
		}
		
		return hm;
		
	}

	@SuppressWarnings("unchecked")
	public HashMap<String,ArrayList<String>> getAllowableSites(ServiceApplication sa) {
		
		HashMap<String,ArrayList<String>> hm = new HashMap<String,ArrayList<String>>();
		
		for ( Iterator<VEE> i = sa.getVEEs().iterator(); i.hasNext() ; ) {
			VEE vee = i.next();
			
			/* The initial set is equal to the allowed sites */
			Vector<SiteEntry> allowedSites = (Vector<SiteEntry>)sites.clone();
			
			for (Iterator<GeographicDomain> ii = vee.getAllowedDomains().iterator(); ii.hasNext() ; ) {
				
				GeographicDomain gd = ii.next();
				
				//FIXME: by the moment only countries are considered
				Map<String, Boolean> countries = gd.getCountries();
				for (Iterator<String> iii = countries.keySet().iterator(); iii.hasNext() ; ) {
					String country = iii.next();

						/* We need this new close to not ruin the Vector while iterating on it */
						Vector<SiteEntry> allowedSitesClone = (Vector<SiteEntry>)allowedSites.clone();
					
						for (Iterator<SiteEntry> iiii =  allowedSitesClone.iterator(); iiii.hasNext() ; ) {
							SiteEntry site = iiii.next();
							if (countries.get(country)) {
								// restricted to inside the country
								if (!site.getCountry().equals(country)) {
									allowedSites.remove(site);
								}
							}
							else {
								// restricted to outside the country
								if (site.getCountry().equals(country)) {
									allowedSites.remove(site);
								}
						}
					}
				}
			}
			
			/* Build an ArrayList with the resulting sites */
			ArrayList<String> l = new ArrayList<String>();
			for (Iterator<SiteEntry> ii =  allowedSites.iterator(); ii.hasNext() ; ) {
				l.add(ii.next().getName());
			}

			hm.put(vee.getVEEName(), l);

		}
		
		return hm;
	}

}
