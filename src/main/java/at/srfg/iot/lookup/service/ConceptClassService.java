package at.srfg.iot.lookup.service;

import java.util.List;

import at.srfg.iot.classification.model.ConceptClass;
import at.srfg.iot.classification.model.Property;

public interface ConceptClassService extends ConceptService<ConceptClass> {
	/**
	 * Retrieve all properties for a class
	 * @param identifier
	 * @return
	 */
	List<Property> getProperties(String identifier);
}