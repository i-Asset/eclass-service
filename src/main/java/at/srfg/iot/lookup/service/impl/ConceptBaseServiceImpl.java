package at.srfg.iot.lookup.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import at.srfg.iot.classification.model.ConceptBase;
import at.srfg.iot.classification.model.ConceptClass;
import at.srfg.iot.classification.model.Property;
import at.srfg.iot.classification.model.PropertyUnit;
import at.srfg.iot.classification.model.PropertyValue;
import at.srfg.iot.lookup.service.ConceptService;

@Service
public class ConceptBaseServiceImpl extends ConceptServiceImpl<ConceptBase> implements ConceptService<ConceptBase>{
	@Autowired
	private ConceptClassServiceImpl conceptClass;
	@Autowired
	private PropertyServiceImpl property;
	@Autowired
	private PropertyUnitServiceImpl propertyUnit;
	@Autowired
	private PropertyValueServiceImpl propertyValue;

	@Override
	public Optional<ConceptBase> addConcept(ConceptBase concept) {
		switch(concept.getConceptType()) {
		case ConceptBase.CONCEPT_CLASS:
			Optional<ConceptClass> storedClass = conceptClass.addConcept(ConceptClass.class.cast(concept));
			return Optional.ofNullable(storedClass.orElse(null));
		case ConceptBase.PROPERTY:
			Optional<Property> storedProperty = property.addConcept(Property.class.cast(concept));
			return Optional.ofNullable(storedProperty.orElse(null));
		case ConceptBase.PROPERTY_UNIT:
			Optional<PropertyUnit> storedUnit = propertyUnit.addConcept(PropertyUnit.class.cast(concept));
			return Optional.ofNullable(storedUnit.orElse(null));
		case ConceptBase.PROPERTY_VALUE:
			Optional<PropertyValue> storedValue = propertyValue.addConcept(PropertyValue.class.cast(concept));
			return Optional.ofNullable(storedValue.orElse(null));
		default:
			return Optional.empty();
		}
	}

	@Override
	public Optional<ConceptBase> setConcept(ConceptBase concept) {
		switch(concept.getConceptType()) {
		case ConceptBase.CONCEPT_CLASS:
			Optional<ConceptClass> storedClass = conceptClass.setConcept(ConceptClass.class.cast(concept));
			return Optional.ofNullable(storedClass.orElse(null));
		case ConceptBase.PROPERTY:
			Optional<Property> storedProperty = property.setConcept(Property.class.cast(concept));
			return Optional.ofNullable(storedProperty.orElse(null));
		case ConceptBase.PROPERTY_UNIT:
			Optional<PropertyUnit> storedUnit = propertyUnit.setConcept(PropertyUnit.class.cast(concept));
			return Optional.ofNullable(storedUnit.orElse(null));
		case ConceptBase.PROPERTY_VALUE:
			Optional<PropertyValue> storedValue = propertyValue.setConcept(PropertyValue.class.cast(concept));
			return Optional.ofNullable(storedValue.orElse(null));
		default:
			return Optional.empty();
		}
	}
	

}
