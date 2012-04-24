package com.telefonica.euro_iaas.placement.rest;

import java.util.List;

import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.dmtf.schemas.ovf.envelope._1.EnvelopeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.telefonica.euro_iaas.placement.core.ModelTranslator;
import com.telefonica.euro_iaas.placement.core.ProviderService;
import com.telefonica.euro_iaas.placement.exception.EntityNotFoundException;
import com.telefonica.euro_iaas.placement.model.CloudProviderList;
import com.telefonica.euro_iaas.placement.model.application.VDC;
import com.telefonica.euro_iaas.placement.model.provider.CloudProvider;
import com.telefonica.schemas.nuba_model.exp.ArrayCIMUserEntityType;
import com.telefonica.schemas.nuba_model.exp.CIMUserEntityType;
import com.telefonica.schemas.tcloud._1.InstantiateOvfParamsType;

@Path("/")
@Component
@Scope("request")
public class ProviderControllerImpl implements ProviderController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProviderController.class);

	@Autowired
	ModelTranslator modelTranslator;
	
	@Autowired
	ProviderService providerService;

	
    /* (non-Javadoc)
	 * @see com.telefonica.euro_iaas.placement.rest.ProviderController#listProviderMapping()
	 */
    @Override
    public JAXBElement<ArrayCIMUserEntityType> ovfListProviderMapping(EnvelopeType envelope) {
    	
    	VDC clientVDC = modelTranslator.getVDC(envelope);
    	List<CloudProvider> listAllowedProviders = null;
    	
    	try {
			listAllowedProviders = providerService.getAllowedCloudProviders(clientVDC);
		} catch (EntityNotFoundException e) {
			LOGGER.warn("Cloud provider not found");
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
    	
    	//CloudProviderList cpl = modelTranslator.getCloudProviderList(listAllowedProviders);
    	CloudProviderList cpl = new CloudProviderList();
    	cpl.setRelations(listAllowedProviders);
    	
    	return new JAXBElement<ArrayCIMUserEntityType>(new QName("ArrayCCIM_UserEntity_Type"), ArrayCIMUserEntityType.class,modelTranslator.getArrayUserEntity(cpl));
    }


	/* (non-Javadoc)
	 * @see com.telefonica.euro_iaas.placement.rest.ProviderController#ovfProviderMapping(org.dmtf.schemas.ovf.envelope._1.EnvelopeType)
	 */
	@Override
	public JAXBElement<CIMUserEntityType> ovfProviderMapping(EnvelopeType envelope) {
		
    	VDC clientVDC = modelTranslator.getVDC(envelope);
    	CloudProvider bestProvider = null;
    	
    	try {
			bestProvider = providerService.getBestCloudProvider(clientVDC);
		} catch (EntityNotFoundException e) {
			LOGGER.warn("Cloud provider not found");
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
    	LOGGER.warn("ProviderControllerImpl BYE");
    	return new JAXBElement<CIMUserEntityType>(new QName("CIM_UserEntity_Type"), CIMUserEntityType.class,modelTranslator.getUserEntity(bestProvider));

	}


	/* (non-Javadoc)
	 * @see com.telefonica.euro_iaas.placement.rest.ProviderController#listProviderMapping(com.telefonica.schemas.tcloud._1.InstantiateOvfParamsType)
	 */
	@Override
	public JAXBElement<ArrayCIMUserEntityType> listProviderMapping(
			InstantiateOvfParamsType instantiateOvfParams) {
    	VDC clientVDC = modelTranslator.getVDC(instantiateOvfParams.getEnvelope());
    	List<CloudProvider> listAllowedProviders = null;
    	
    	try {
			listAllowedProviders = providerService.getAllowedCloudProviders(clientVDC);
		} catch (EntityNotFoundException e) {
			LOGGER.warn("Cloud provider not found");
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
    	
    	//CloudProviderList cpl = modelTranslator.getCloudProviderList(listAllowedProviders);
    	CloudProviderList cpl = new CloudProviderList();
    	cpl.setRelations(listAllowedProviders);
    	
    	return new JAXBElement<ArrayCIMUserEntityType>(new QName("ArrayCCIM_UserEntity_Type"), ArrayCIMUserEntityType.class,modelTranslator.getArrayUserEntity(cpl));
	}


	/* (non-Javadoc)
	 * @see com.telefonica.euro_iaas.placement.rest.ProviderController#providerMapping(com.telefonica.schemas.tcloud._1.InstantiateOvfParamsType)
	 */
	@Override
	public JAXBElement<CIMUserEntityType> providerMapping(
			InstantiateOvfParamsType instantiateOvfParams) {
		
		VDC clientVDC = modelTranslator.getVDC(instantiateOvfParams.getEnvelope());
    	CloudProvider bestProvider = null;
    	
    	try {
			bestProvider = providerService.getBestCloudProvider(clientVDC);
		} catch (EntityNotFoundException e) {
			LOGGER.warn("Cloud provider not found");
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
    	LOGGER.warn("ProviderControllerImpl BYE");
    	return new JAXBElement<CIMUserEntityType>(new QName("CIM_UserEntity_Type"), CIMUserEntityType.class,modelTranslator.getUserEntity(bestProvider));
	}
}
