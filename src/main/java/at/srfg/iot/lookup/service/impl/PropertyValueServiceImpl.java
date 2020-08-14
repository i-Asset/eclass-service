package at.srfg.iot.lookup.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import at.srfg.iot.classification.model.PropertyValue;
import at.srfg.iot.lookup.service.PropertyValueService;

@Service
public class PropertyValueServiceImpl extends ConceptServiceImpl<PropertyValue> implements PropertyValueService {

	@Override
	public Optional<PropertyValue> addConcept(PropertyValue newConcept) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<PropertyValue> setConcept(PropertyValue updatedConcept) {
		// TODO Auto-generated method stub
		return null;
	}

}
