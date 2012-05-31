package com.telefonica.euro_iaas.placement.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBElement;

import org.dmtf.schemas.ovf.envelope._1.EnvelopeType;

import com.telefonica.euro_iaas.placement.exception.EntityNotFoundException;
import com.telefonica.schemas.nuba_model.exp.ArrayCIMUserEntityType;
import com.telefonica.schemas.nuba_model.exp.CIMUserEntityType;
import com.telefonica.schemas.tcloud._1.InstantiateOvfParamsType;

public interface ProviderController {	

	/**
	 * Retrieves all qualified CIMUserEntity for a OVF envelope
	 * 
	 * @return ArrayCIMUserEntityType
	 * @throws EntityNotFoundException 
	 */	
	@POST
	//@Path("/listprovidermapping")
	@Path("/api/org/{org}/vdc/{vdc}/action/ovflistprovidermapping")
	@Consumes( { MediaType.APPLICATION_XML })
	@Produces( { MediaType.APPLICATION_XML })
	public JAXBElement<ArrayCIMUserEntityType> ovfListProviderMapping(EnvelopeType envelope);
	
	/**
	 * Retrieves the best CIMUserEntity for a OVF envelope
	 * 
	 * @return a CIMUserEntity
	 * @throws EntityNotFoundException 
	 * 
	 */	
	@POST
	@Path("/api/org/{org}/vdc/{vdc}/action/ovfprovidermapping")
	@Consumes( { MediaType.APPLICATION_XML })
	@Produces( { MediaType.APPLICATION_XML })
	public JAXBElement<CIMUserEntityType> ovfProviderMapping(EnvelopeType envelope);

	/**
	 * Retrieves all qualified CIMUserEntity for a InstantiateOvfParamsType
	 * 
	 * @return ArrayCIMUserEntityType
	 * @throws EntityNotFoundException 
	 */	
	@POST
	//@Path("/listprovidermapping")
	@Path("/api/org/{org}/vdc/{vdc}/action/listprovidermapping")
	@Consumes( { MediaType.APPLICATION_XML })
	@Produces( { MediaType.APPLICATION_XML })
	public JAXBElement<ArrayCIMUserEntityType> listProviderMapping(InstantiateOvfParamsType instantiateOvfParams);
		
	
	/**
	 * Retrieves the best CIMUserEntity for a OVF envelope
	 * 
	 * @return a CIMUserEntity
	 * @throws EntityNotFoundException 
	 * 
	 */	
	@POST
	@Path("/api/org/{org}/vdc/{vdc}/action/providermapping")
	@Consumes( { MediaType.APPLICATION_XML })
	@Produces( { MediaType.APPLICATION_XML })
	public JAXBElement<CIMUserEntityType> providerMapping(InstantiateOvfParamsType instantiateOvfParams);
	
	
}
