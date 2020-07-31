package at.srfg.iot.eclass.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import at.srfg.iot.eclass.service.ClassService;
import at.srfg.iot.eclass.service.DataDuplicationService;
import io.swagger.annotations.ApiOperation;

@RestController
public class SemanticConceptController {
	@Autowired
	private DataDuplicationService duplicate;
	
	@ApiOperation(
			value = "Duplicate Classes",
			notes = "The assigned PropertyDefinitions for a given ClassificationClass")
	@RequestMapping(
			method = RequestMethod.POST,
			path="/duplicate/class"			)
	public boolean duplicateClasses(@RequestParam(name="codedNamePrefix") String prefix) {
		
		duplicate.copyConceptClass(null, prefix);
		return true;
	}

	
}
