package at.srfg.iot.lookup.dependency;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import at.srfg.indexing.model.common.ClassType;
import at.srfg.indexing.model.common.PropertyType;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@FeignClient(name = "indexing-service")
public interface SemanticIndexing {
	
	/**
	 * Store a new or update an existing {@link ClassType} object
	 * @param prop
	 * @return The stored version of the classType
	 * @throws Exception
	 */
	@ApiOperation(
			value = "Create or update a ClassType", 
			notes = "Store the provided ClassType object in the index, will also "
					+ "create/update Properties including the links between ClassType and PropertyType ...")
	@RequestMapping(
			method = RequestMethod.POST,
			path="/classType")
	public ClassType setClassType(
//			@RequestHeader(value = "Authorization") 
//			String bearerToken,
			@RequestBody ClassType prop) throws Exception;
	/**
	 * Remove the provided class from the index
	 * @param uri
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(
			method = RequestMethod.DELETE,
			path="/classType")
	@ApiOperation(
			value = "Delete a ClassType element from the index", 
			notes = "The entry is removed, corresponding links from PropertyType are also removed!")
	public boolean deleteClassType(
			@ApiParam("The id of the concept class to remove")
			@RequestParam(name = "id") List<String> uri) throws Exception ;
	/**
	 * Store a new or update an existing {@link PropertyType} object
	 * @param prop
	 * @return The stored version of the PropertyType
	 * @throws Exception
	 */
	@ApiOperation(
			value = "Create or update a PropertyType", 
			notes = "Store the provided PropertyType object in the index")
	@RequestMapping(
			method = RequestMethod.POST,
			path="/propertyType")
	public PropertyType setPropertyType(
//			@RequestHeader(value = "Authorization") 
//			String bearerToken,
			@RequestBody PropertyType prop) throws Exception;
	/**
	 * Remove the provided property from the index
	 * @param uri
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(
			method = RequestMethod.DELETE,
			path="/propertyType")
	@ApiOperation(
			value = "Delete a PropertyType element from the index", 
			notes = "The entry is removed, corresponding links from ClassType are NOT removed!")
	public boolean deletePropertyType(
			@ApiParam("The id of the property to remove")
			@RequestParam(name = "id") 
			List<String> uri) throws Exception;			
	
}
