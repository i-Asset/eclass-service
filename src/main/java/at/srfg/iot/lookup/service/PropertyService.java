package at.srfg.iot.lookup.service;

import java.util.List;

import at.srfg.iot.classification.model.Property;
import at.srfg.iot.classification.model.PropertyValue;

public interface PropertyService extends ConceptService<Property> {
	/**
	 * Retrieve all property-values for a given class
	 * @param identifier
	 * @return
	 */
	List<PropertyValue> getValues(String identifier);
}
