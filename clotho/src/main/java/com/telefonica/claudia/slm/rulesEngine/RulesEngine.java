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
package com.telefonica.claudia.slm.rulesEngine;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;

import org.apache.log4j.Logger;
import org.drools.RuleBase;
import org.drools.RuleBaseFactory;
import org.drools.StatefulSession;
import org.drools.WorkingMemory;
import org.drools.compiler.DroolsParserException;
import org.drools.compiler.PackageBuilder;
import org.drools.compiler.PackageBuilderConfiguration;
import org.drools.event.DebugWorkingMemoryEventListener;
import org.drools.rule.Package;

import com.telefonica.claudia.slm.deployment.Rule;
import com.telefonica.claudia.slm.deployment.ServiceApplication;
import com.telefonica.claudia.slm.deployment.ServiceKPI;
import com.telefonica.claudia.slm.deployment.VEE;
import com.telefonica.claudia.slm.lifecyclemanager.FSM;
import com.telefonica.claudia.slm.lifecyclemanager.LifecycleController;

public class RulesEngine {
    
    private static Logger logger = Logger.getLogger(RulesEngine.class);
    
    private StatefulSession rulesEngineSession = null;
    
    @SuppressWarnings("unused")
	private String rulesFileName = null;
    
    @SuppressWarnings("unused")
	private EventsReceptor erecept;
    
    private PackageBuilderConfiguration builderConf;
    
    private PackageBuilder builder;
    
    private Package pkg;
    
    private RuleBase ruleBase;
    
    private HashSet<String> ruleSet= new HashSet<String>();
    
    public ServiceApplication sap;
    
    private LifecycleController lcc;
    
    private final static String paramOpener="@{";
    private final static String paramCloser="}";
    
    private String ruleBaseDir="./ruleHome/";
    private String ruleExtension=".drl";
    
    public RulesEngine(String rulesFileName) {
        this.rulesFileName = rulesFileName;
    }
    
    public RulesEngine(LifecycleController lcc) {                
        this.lcc=lcc;        
    }
    
    public void run() {
        
        // To avoid eclipse dependencies, we set the java compiler to janino.
        
        logger.info("Preparing package builder");
        Properties properties = new Properties();
        //properties.setProperty( "drools.dialect.java.compiler", "JANINO" );
        properties.setProperty( "drools.dialect.java.compiler", "ECLIPSE" );
        
        builderConf = new PackageBuilderConfiguration(properties);
        builder = new PackageBuilder(builderConf);
        
        initRules();
        
    }
    
    protected void executeRules(WorkingEnvironmentCallback callBack) {     
        logger.info("Starting rules engine stateful session");
        
        if (ruleBase==null) {
        	logger.error("Rule base not found for Service [" + sap + "]. This event may be lost from a previously deleted service.");
        	return;
        }
        
        WorkingMemory workingMemory = ruleBase.newStatefulSession();
        workingMemory.addEventListener(new DebugWorkingMemoryEventListener());
        callBack.initEnvironment(workingMemory);
        logger.info("Firing rules");
        workingMemory.fireAllRules(); 
    }
    
    /**
     *  init the first rule to be evaluated and loaded
     */
    public void startRule(String startRule){
        // add Rule to a Set to load it
    	System.out.println ("add rule " + startRule);
        ruleSet.add(startRule);    
    }
    
    /**
     * Configures the rule engine with the required data
     * @param sap
     * @param fsm
     */
    public void configure(FSM fsm) {        
        this.sap=fsm.getServiceApplication();
        erecept=new EventsReceptor(this, fsm, sap, lcc);   
    }
    
    /**
     * Load all the rules in the ruleSet
     */
    public void initRules(){
        String ruleFile;
        Iterator <String> it=ruleSet.iterator();
        while (it.hasNext()){
            ruleFile=it.next();
            System.out.println ("rule example " + ruleFile);
            addRule(ruleFile);
        }
    }
    
    /**
     *  Call only after run or startRule have been invoked: method to update the set of running rules
     * @param rulesFile
     */
    public void addRule(String rulesFile){
		
		//Read from an input stream
	    BufferedInputStream rf;
        InputStreamReader source;
         
        logger.info("Adding rule [" + rulesFile + "] to the rules base");
                
        try {
            rf = new BufferedInputStream(new FileInputStream(rulesFile));
                    //		 Read in the rules source file
            source = new InputStreamReader(rf);
            //		 This will parse and compile in one step
            builder.addPackageFromDrl(source);
        } catch (FileNotFoundException ex) {
            java.util.logging.Logger.getLogger(RulesEngine.class.getName()).log(Level.SEVERE, null, ex);            
            logger.error("File not found "+ex.getMessage());
        } catch (DroolsParserException ex) {
            java.util.logging.Logger.getLogger(RulesEngine.class.getName()).log(Level.SEVERE, null, ex);
            logger.error("Parse exception "+ex.getMessage());
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(RulesEngine.class.getName()).log(Level.SEVERE, null, ex);
            logger.error("IOException "+ex.getMessage());
        }
		 
        logger.info("Building package");
		pkg = builder.getPackage();
                 
        logger.info("Creating rules base (container of rules packages)");
		// Add the package to a rulebase (deploy the rule package).
		ruleBase = RuleBaseFactory.newRuleBase();
        try {
            logger.info("Adding rules package to rule base");
            ruleBase.addPackage(pkg);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(RulesEngine.class.getName()).log(Level.SEVERE, null, ex);
            logger.error("Exception caught when trying to add package", ex);
            System.out.println(ex.getMessage());
        }
		
   }
    
    public void close() {        
        logger.info("Removing rules from rules engine");
        rulesEngineSession.dispose();        
    }
     
    

    /**
     *  convert this VEE rules into Drools-compatible rules
     */
    public void rules2Drools(){ 
        String header, body, condition, function,action;
        CharSequence target,replacement,targetAction,replacementAction;
        int locatorInit,locatorEnd;
        PrintWriter out ;
        Rule rule;
        ServiceKPI kpi;
        VEE vee;
        
        int occurence,occurenceAction;
        header="package es.tid.reservoir.serviceManager.rulesEngine;"+"\n"+"// Imports for all types of events should be here..."+"\n"+"import es.tid.reservoir.serviceManager.eventsBus.events.VeeHwMeasureEvent;"+"\n"+"import es.tid.reservoir.serviceManager.eventsBus.events.AgentMeasureEvent;"+"\n"+"import es.tid.reservoir.serviceManager.eventsBus.events.ProbeMeasureEvent;"+"\n"+"global es.tid.reservoir.serviceManager.rulesEngine.Actions actions; \n";
        
        Iterator<ServiceKPI> kpiIterator;
        Iterator<VEE> veeIterator;
        Iterator<Rule> ruleIterator=sap.getServiceRules().iterator();

        while(ruleIterator.hasNext()){        
            rule=((Rule)ruleIterator.next());
            
            
            // add rule name
            
            body="rule"+" \""+rule.getFQN().toString();
            // add other headers
            body=body+"\""+"\n"+"lock-on-active true"+"\n"+"salience 100"+"\n"+"when"+"\n";
            // add when part of the rule
            
            condition=rule.getCondition();
            action=rule.getAction();
            
            // replace KPIs with their FQN
            kpiIterator=sap.getServiceKPIs().iterator();
            String kpiName = null;
            while(kpiIterator.hasNext()){
                kpi=((ServiceKPI)kpiIterator.next());
                target=kpi.getKPIName();
                target.toString();
                //assume naming convention                 
                target=paramOpener+"kpis."+target+paramCloser;
                occurence=(condition.indexOf(target.toString()));                
                if(!(occurence<0)){
                    // the KPI name is included in this rule,                    
                    //the parameter is the KPI
                    replacement="value";
                    kpiName=kpi.getFQN().toString();
                    // then, we must substitute it                    
                    condition=condition.replace(target, replacement);                             
                }                
            }
            
            // replace VEEs with their FQN
            veeIterator= sap.getVEEs().iterator();
            while(veeIterator.hasNext()){
                vee=(VEE)veeIterator.next();
                target=vee.getVEEName();
                target=paramOpener+"components."+target+".replicas.";
                occurence=(condition.indexOf(target.toString()));  
                if(!(occurence<0)){
                     // the VEE name is included in this rule                    
                     
                     // Find the function we are dealing with (amount in this case) to map it to a Drools global object
                     // assume amount is a known parameter (a set of allowed parameters must be defined and checked here in the future: TBD), 
                     locatorInit=occurence+target.length();
                     locatorEnd=condition.indexOf(paramCloser,occurence);
                     function=condition.substring(locatorInit, locatorEnd);
                     
                     if(function.equals("amount")){
                         target=target+function+paramCloser;
                         replacement="actions.getAmount(\""+vee.getFQN().toString()+"\")";
                         // then, we must substitute it
                        condition=condition.replace(target, replacement); 
                     }                             
                }
                targetAction=vee.getVEEName();
                targetAction="components."+targetAction;                
                occurenceAction=(action.indexOf(targetAction.toString()));                                  
                if(!(occurenceAction<0)){                    
                    targetAction=action;                    
                    replacementAction="actions."+action.substring(0,occurenceAction)+"\""+vee.getFQN().toString()+"\")" ;                      
                    // then, we must substitute it
                    action=action.replace(targetAction, replacementAction); 
                }                
                                   
            }            
            // add then part of the rule
           
            body=body+"   event : "+rule.getEventType().toString()+" (measure == \""+kpiName+"\" , "+"eval("+condition+")); \n"+"then \n";            
            body=body+action+";\n"+"end";
            
           // System.out.println(header+body);
            
            //write to Drools
            try {                
                File norma=new File(ruleBaseDir+rule.getFQN().toString()+ruleExtension);
                logger.info("Storing new Drools rule in file " + norma.getAbsolutePath());
                norma.createNewFile();                
                out = new PrintWriter(new FileWriter(norma));
                
                out.write(header+body);
                out.close();
            }catch (IOException ex){
                logger.error("IOException caught when trying to create rule file " + ex);
            }
            // load rules into drools          
           startRule(ruleBaseDir+rule.getFQN().toString()+ruleExtension); 
           
        }
     
 
    }
    

    /**
    *  convert this VEE rules into Drools-compatible rules
    *  @param type of rules employed that should be converted
    */

   public void claudiaRules2Drools(){
       String ruleUp, ruleDown;
       boolean up=true;
       PrintWriter out ;
       Rule rule;
       Iterator<Rule> ruleIterator;

       Set<Rule> ruleVector = sap.getServiceRules();
       System.out.println ("numero de reglas " + ruleVector.size());
	   for(ruleIterator=ruleVector.iterator();ruleIterator.hasNext();){
	       rule=((Rule)ruleIterator.next());
	
	       // IN Y2 a rule is associated with a single KPI: univocal association
	       // build upscaling rule first
	      
	       ruleUp=rule.buildRule(up);
	
	       // build donwscaling rule
	       System.out.println ("ruling down");
	       ruleDown=rule.buildRule(!up);
	
	       //write to Drools
	       try {
	               // write upscaling rule to file
	           File norma=new
	           File(ruleBaseDir+rule.getFQN().toString()+ruleExtension);
	           norma.createNewFile();
	           out = new PrintWriter(new FileWriter(norma));
	           out.write(ruleUp);
	           out.close();
	
	        // write downscaling rule to file
	           norma=new
	           File(ruleBaseDir+rule.getFQN().toString()+"b"+ruleExtension);
	           norma.createNewFile();
	           out = new PrintWriter(new FileWriter(norma));
	           out.write(ruleDown);
	           out.close();
	
	
			}catch (IOException ex){
			       //log.debug("IOException caught when trying to createrule file " + ex);
			}
			   // load rules into drools
	        startRule(ruleBaseDir+rule.getFQN().toString()+ruleExtension);
	        startRule(ruleBaseDir+rule.getFQN().toString()+"b"+ruleExtension);
		}
    }
}
