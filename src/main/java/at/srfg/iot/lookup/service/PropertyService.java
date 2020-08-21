package at.srfg.iot.lookup.service;

import java.util.Set;

import at.srfg.iot.classification.model.ConceptProperty;
import at.srfg.iot.classification.model.ConceptPropertyValue;

public interface PropertyService extends ConceptService<ConceptProperty> {
	/**
	 * Retrieve all property-values for a given property and (optionally) for
	 * a provided class
	 * @param identifier
	 * @param classIdentifier
	 * @return
	 */
	Set<ConceptPropertyValue> getValues(String identifier, String classIdentifier);
}
