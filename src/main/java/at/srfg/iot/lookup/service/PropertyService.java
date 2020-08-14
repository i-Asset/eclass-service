package at.srfg.iot.lookup.service;

import java.util.Set;

import at.srfg.iot.classification.model.Property;
import at.srfg.iot.classification.model.PropertyValue;

public interface PropertyService extends ConceptService<Property> {
	/**
	 * Retrieve all property-values for a given property and (optionally) for
	 * a provided class
	 * @param identifier
	 * @param classIdentifier
	 * @return
	 */
	Set<PropertyValue> getValues(String identifier, String classIdentifier);
}
