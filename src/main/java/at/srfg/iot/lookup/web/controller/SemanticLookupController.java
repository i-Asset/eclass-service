package at.srfg.iot.lookup.web.controller;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import at.srfg.iot.classification.api.SemanticLookupService;
import at.srfg.iot.classification.model.ConceptBase;
import at.srfg.iot.classification.model.ConceptClass;
import at.srfg.iot.classification.model.Description;
import at.srfg.iot.classification.model.Property;
import at.srfg.iot.classification.model.PropertyUnit;
import at.srfg.iot.classification.model.PropertyValue;
import at.srfg.iot.lookup.service.ConceptClassService;
import at.srfg.iot.lookup.service.ConceptService;
import at.srfg.iot.lookup.service.PropertyService;
import at.srfg.iot.lookup.service.PropertyUnitService;
import at.srfg.iot.lookup.service.PropertyValueService;

@RestController
public class SemanticLookupController implements SemanticLookupService {
//	@Autowired
//	private ClassService classService;
	@Autowired
	private ConceptClassService conceptService;
	@Autowired
	private PropertyService propertyService;
	@Autowired
	private PropertyUnitService propertyUnitService;
	@Autowired
	private PropertyValueService propertyValueService;
	@Autowired
	private ConceptService<ConceptBase> conceptBase;
//	@Autowired
//	private ConceptService<ConceptClass> conceptClass;
//	@Autowired
//	private ConceptService<Property> property;
	

	@Override
	public Optional<ConceptClass> getConceptClass(String identifier) {
		return conceptService.getConcept(identifier);
	}

	@Override
	public Optional<ConceptBase> getConcept(String identifier) {
		// read the concept
		return conceptBase.getConcept(identifier);
	}
	@Override
	public Optional<ConceptBase> addConcept(ConceptBase concept) {
		return conceptBase.addConcept(concept);
	}
	@Override
	public Optional<ConceptBase> setConcept(ConceptBase concept) {
		return conceptBase.setConcept(concept);
	}
	@Override
	public boolean deleteConcept(String identifier) {
		return conceptBase.deleteConcept(identifier);
	}
	@Override
	public Optional<ConceptClass> addConceptClass(String parentIdentifier, ConceptClass conceptClass) {
		return conceptService.addConcept(parentIdentifier, conceptClass);
	}
	@Override
	public Optional<ConceptClass> setConceptClass(ConceptClass conceptClass) {
		return conceptService.setConcept(conceptClass);
	}
	
	@Override
	public boolean deleteConceptClass(List<String> identifiers) {
		for (String id : identifiers) {
			conceptService.deleteConcept(id);
		}
		return true;
	}
	@Override
	public Optional<ConceptBase> setConceptDescription(String identifier, Description description) {
		return conceptBase.setDescription(identifier, description);
	}
	@Override
	public Collection<Property> getPropertiesForConceptClass(String identifier, boolean complete) {
		return conceptService.getProperties(identifier);
	}

	@Override
	public Optional<Property> getProperty(String identifier) {
		return propertyService.getConcept(identifier);
	}

	@Override
	public Optional<Property> addProperty(Property property) {
		return propertyService.addConcept(property);
	}

	@Override
	public Optional<Property> setProperty(Property property) {
		return propertyService.setConcept(property);
	}

	@Override
	public boolean deleteProperty(List<String> identifiers) {
		
		for (String id : identifiers) {
			propertyService.deleteConcept(id);
		}
		return true;
	}
	
	@Override
	public Collection<PropertyValue> getValuesForProperty(String identifier, String classIdentifier) {
		// TODO Auto-generated method stub
		return propertyService.getValues(identifier, classIdentifier);
	}

	@Override
	public Optional<PropertyUnit> getPropertyUnit(String identifier) {
		return propertyUnitService.getConcept(identifier);
	}

	@Override
	public Optional<PropertyUnit> addPropertyUnit(PropertyUnit propertyUnit) {
		return propertyUnitService.addConcept(propertyUnit);
	}

	@Override
	public Optional<PropertyUnit> setPropertyUnit(PropertyUnit PropertyUnit) {
		return propertyUnitService.setConcept(PropertyUnit);
	}

	@Override
	public boolean deletePropertyUnit(List<String> identifiers) {
		for (String id : identifiers ) {
			propertyUnitService.deleteConcept(id);
		}
		return true;
	}

	@Override
	public Optional<PropertyValue> getPropertyValue(String identifier) {
		return propertyValueService.getConcept(identifier);
	}

	@Override
	public Optional<PropertyValue> addPropertyValue(PropertyValue propertyUnit) {
		return propertyValueService.addConcept(propertyUnit);
	}

	@Override
	public Optional<PropertyValue> setPropertyValue(PropertyValue propertyValue) {
		return propertyValueService.setConcept(propertyValue);
	}

	@Override
	public boolean deletePropertyValue(List<String> identifiers) {
		for (String id : identifiers ) {
			propertyValueService.deleteConcept(id);
		}
		return true;
	}
	
}
