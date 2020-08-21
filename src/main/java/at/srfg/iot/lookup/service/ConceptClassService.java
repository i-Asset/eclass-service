package at.srfg.iot.lookup.service;

import java.util.List;
import java.util.Optional;

import at.srfg.iot.classification.model.ConceptClass;
import at.srfg.iot.classification.model.ConceptProperty;

public interface ConceptClassService extends ConceptService<ConceptClass> {
	/**
	 * Retrieve all properties for a class
	 * @param identifier
	 * @return
	 */
	List<ConceptProperty> getProperties(String identifier);
	
	Optional<ConceptClass> addConcept(String parentConceptIdentifier, ConceptClass newConcept);
}
