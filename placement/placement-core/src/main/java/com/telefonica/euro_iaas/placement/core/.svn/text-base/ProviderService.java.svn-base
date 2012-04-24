package com.telefonica.euro_iaas.placement.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.drools.agent.KnowledgeAgent;
import org.drools.command.Command;
import org.drools.command.CommandFactory;
import org.drools.runtime.ExecutionResults;
import org.drools.runtime.StatelessKnowledgeSession;
import org.drools.runtime.rule.QueryResults;
import org.drools.runtime.rule.QueryResultsRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.telefonica.euro_iaas.placement.dao.CloudProviderDao;
import com.telefonica.euro_iaas.placement.exception.EntityNotFoundException;
import com.telefonica.euro_iaas.placement.model.application.ServiceApplication;
import com.telefonica.euro_iaas.placement.model.application.VDC;
import com.telefonica.euro_iaas.placement.model.application.VEERequired;
import com.telefonica.euro_iaas.placement.model.provider.CloudProvider;
import com.telefonica.euro_iaas.placement.model.provider.VEE;

/**
 * @author jpuente
 * 
 */
@Service
public class ProviderService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProviderService.class
			.getName());

	@Autowired
	private CloudProviderDao cloudProviderDao;


	@Autowired
	private KnowledgeAgent kAgent;

	/**
	 * 
	 */
	public ProviderService() {
		super();
	}

	@Transactional
	public List<CloudProvider> getAllowedCloudProviders(VDC vdc)
			throws EntityNotFoundException {

		List<CloudProvider> listProviders = new Vector<CloudProvider>();

		// Create session, insert cloud providers and vdc. Run all and Query.
		StatelessKnowledgeSession ksession = kAgent.newStatelessKnowledgeSession();

		//KnowledgeBase kBase = KnowledgeBaseFactory.newKnowledgeBase();
		//KnowledgeBuilder kbuilder = KnowledgeBuilderFactory
		//		.newKnowledgeBuilder();
		//kbuilder.add(ResourceFactory.newClassPathResource("changeset.xml",
		//		getClass()), ResourceType.CHANGE_SET);
		// Check the builder for errors
		//if (kbuilder.hasErrors()) {
		//	System.out.println(kbuilder.getErrors().toString());
		//	throw new RuntimeException("Unable to compile rules");
		//}
		//Collection<KnowledgePackage> pkgs = kbuilder.getKnowledgePackages();
		//kBase.addKnowledgePackages(pkgs);
		//StatelessKnowledgeSession ksession = kBase
		//		.newStatelessKnowledgeSession();

		List<Command> cmds = new ArrayList<Command>();
		cmds.addAll(getCloudProvidersCommands());
		cmds.addAll(getVDCCommands(vdc));
		cmds.add(CommandFactory.newSetGlobal("LOGGER", LOGGER));
		cmds.add(CommandFactory.newFireAllRules());
		cmds.add(CommandFactory.newQuery("allowedProviders",
				"Get Allowed Cloud Providers"));
		ExecutionResults results = ksession.execute(CommandFactory
				.newBatchExecution(cmds));

		for (String id : results.getIdentifiers()) {
			LOGGER.info("results id: " + id);
		}

		QueryResults qr = (QueryResults) results.getValue("allowedProviders");
		LOGGER.info("QueryResults" + qr);

		for (Iterator<?> it = qr.iterator(); it.hasNext();) {
			QueryResultsRow qResult = (QueryResultsRow) it.next();
			CloudProvider cp = (CloudProvider) qResult.get("$cp");
			listProviders.add(cp);
		}

		if (listProviders.size() == 0)
			throw new EntityNotFoundException();
		else
			return listProviders;
	}

	@Transactional
	public CloudProvider getBestCloudProvider(VDC vdc)
			throws EntityNotFoundException {
		// Create session, insert cloud providers and vdc. Run all and Query.
		StatelessKnowledgeSession ksession = kAgent
				.newStatelessKnowledgeSession();

		List<Command> cmds = new ArrayList<Command>();
		cmds.addAll(getCloudProvidersCommands());
		cmds.addAll(getVDCCommands(vdc));
		cmds.add(CommandFactory.newSetGlobal("LOGGER", LOGGER));
		cmds.add(CommandFactory.newFireAllRules());
		cmds.add(CommandFactory.newQuery("bestProvider",
				"Get Best Cloud Provider"));
		ExecutionResults results = ksession.execute(CommandFactory
				.newBatchExecution(cmds));
		QueryResults qr = (QueryResults) results.getValue("bestProvider");

		for (Iterator<?> it = qr.iterator(); it.hasNext();) {
			QueryResultsRow qResult = (QueryResultsRow) it.next();
			CloudProvider cp = (CloudProvider) qResult.get("$cp");
			return cp;
		}
		throw new EntityNotFoundException();
	}

	private Collection<Command> getCloudProvidersCommands() {
		List<Command> cmds = new ArrayList<Command>();

		List<CloudProvider> listCloudProviders = cloudProviderDao.findAll();

		for (CloudProvider cp : listCloudProviders) {

			LOGGER.info("new cloud provider" + cp);
			cmds.add(CommandFactory.newInsert(cp));
			for (VEE vee : cp.getVees()) {
				cmds.add(CommandFactory.newInsert(vee));
			}
		}

		return cmds;
	}

	private Collection<Command> getVDCCommands(VDC vdc) {
		List<Command> cmds = new ArrayList<Command>();

		cmds.add(CommandFactory.newInsert(vdc));
		for (ServiceApplication sa : vdc.getServiceApplications()) {
			cmds.add(CommandFactory.newInsert(sa));
			for (VEERequired vee : sa.getVeesRequired()) {
				cmds.add(CommandFactory.newInsert(vee));
			}
		}

		return cmds;
	}
	

	/**
	 * @return the cloudProviderDao
	 */
	public CloudProviderDao getCloudProviderDao() {
		return cloudProviderDao;
	}

	/**
	 * @param cloudProviderDao the cloudProviderDao to set
	 */
	public void setCloudProviderDao(CloudProviderDao cloudProviderDao) {
		this.cloudProviderDao = cloudProviderDao;
	}

	/**
	 * @return the kAgent
	 */
	public KnowledgeAgent getkAgent() {
		return kAgent;
	}

	/**
	 * @param kAgent the kAgent to set
	 */
	public void setkAgent(KnowledgeAgent kAgent) {
		this.kAgent = kAgent;
	}	

}
