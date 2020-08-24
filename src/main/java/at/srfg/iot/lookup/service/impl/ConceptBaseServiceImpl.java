package at.srfg.iot.lookup.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import at.srfg.iot.classification.model.ConceptBase;
import at.srfg.iot.classification.model.ConceptClass;
import at.srfg.iot.classification.model.ConceptProperty;
import at.srfg.iot.classification.model.ConceptPropertyUnit;
import at.srfg.iot.classification.model.ConceptPropertyValue;
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
			Optional<ConceptProperty> storedProperty = property.addConcept(ConceptProperty.class.cast(concept));
			return Optional.ofNullable(storedProperty.orElse(null));
		case ConceptBase.PROPERTY_UNIT:
			Optional<ConceptPropertyUnit> storedUnit = propertyUnit.addConcept(ConceptPropertyUnit.class.cast(concept));
			return Optional.ofNullable(storedUnit.orElse(null));
		case ConceptBase.PROPERTY_VALUE:
			Optional<ConceptPropertyValue> storedValue = propertyValue.addConcept(ConceptPropertyValue.class.cast(concept));
			return Optional.ofNullable(storedValue.orElse(null));
		default:
			return Optional.empty();
		}
	}

	@Override
	public Optional<ConceptBase> setConcept(ConceptBase concept) {
		Optional<ConceptBase> base = getConcept(concept.getConceptId(), ConceptBase.class);
		if ( base.isPresent() ) {
			return Optional.of(setConcept(base.get(), concept));
		}
		return Optional.empty();
	}

	@Override
	public ConceptBase setConcept(ConceptBase concept, ConceptBase update) {
		
		switch(concept.getConceptType()) {
		case ConceptBase.CONCEPT_CLASS:
			 return conceptClass.setConcept(
					ConceptClass.class.cast(concept),
					ConceptClass.class.cast(update));
		case ConceptBase.PROPERTY:
			return property.setConcept(
					ConceptProperty.class.cast(concept),
					ConceptProperty.class.cast(update));
		case ConceptBase.PROPERTY_UNIT:
			return propertyUnit.setConcept(
					ConceptPropertyUnit.class.cast(concept),
					ConceptPropertyUnit.class.cast(update));
		case ConceptBase.PROPERTY_VALUE:
			return propertyValue.setConcept(
					ConceptPropertyValue.class.cast(concept),
					ConceptPropertyValue.class.cast(update));
		default:
			throw new IllegalStateException("Additional Concept Type not handled: " +concept.getConceptType());
		}
	}
	

}
