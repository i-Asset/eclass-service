package at.srfg.iot.eclass.web.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import at.srfg.iot.eclass.api.SemanticLookupService;
import at.srfg.iot.eclass.model.ClassificationClass;
import at.srfg.iot.eclass.model.PropertyDefinition;
import at.srfg.iot.eclass.model.PropertyUnit;
import at.srfg.iot.eclass.model.PropertyValue;
import at.srfg.iot.eclass.service.ClassService;

@RestController
public class SemanticLookupController implements SemanticLookupService {
	@Autowired
	private ClassService classService;


	public Optional<ClassificationClass> getClass(
			String irdi) {
		return classService.getClassification(irdi);//.orElse(null);
	}
	public Optional<PropertyDefinition> getProperty(String irdi) {
		return classService.getProperty(irdi);//.orElse(null);
	}
	public List<PropertyDefinition> getPropertiesForClass(String irdi) {
		return classService.getProperties(irdi);
	}
	public Optional<PropertyValue> getValue(String irdi) {
		return classService.getValue(irdi);//.orElse(null);
	}
	public List<PropertyValue> getPropertyValues(String irdicc, String irdipr) {
		return classService.getPropertyValues(irdicc, irdipr);
	}
	public Optional<PropertyUnit> getUnit(String irdi) {
		return classService.getUnit(irdi);//.orElse(null);
	}
	
}
