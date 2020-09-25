package at.srfg.iot.lookup.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import at.srfg.iot.classification.api.SemanticLookupService;
import at.srfg.iot.classification.model.ConceptClass;
import at.srfg.iot.lookup.service.onto.OntologyService;
import io.swagger.annotations.ApiOperation;

@RestController
public class OntologyController {
	@Autowired
	private OntologyService onto;
	/**
	 * Read a {@link ConceptClass} from the semantic lookup service
	 * @param identifier The URI or IRDI  of the concept class
	 * @see SemanticLookupService#getConcept(String)
	 * @return
	 */
	@ApiOperation(
			value = "Upload a vocabulary",
			notes = "Read the ConceptClass with it's full URI or IRDI")
	@RequestMapping(
			method = RequestMethod.POST,
			path="/vocabulary/upload", consumes = {"application/rdf+xml", "application/turtle"})
	public Boolean getConceptClass(
			@RequestHeader(value="Content-Type", required = false, defaultValue = "application/rdf+xml")
			String mimeType,
			@RequestParam("nameSpace") 
			List<String> nameSpace,
			@RequestBody 
			String content) {
		
		onto.upload(mimeType, content, nameSpace);
		return true;
	}
}
