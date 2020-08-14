package at.srfg.iot.lookup.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import at.srfg.iot.classification.model.PropertyUnit;
import at.srfg.iot.lookup.service.PropertyUnitService;

@Service
public class PropertyUnitServiceImpl extends ConceptServiceImpl<PropertyUnit> implements PropertyUnitService {

	@Override
	public Optional<PropertyUnit> addConcept(PropertyUnit newConcept) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<PropertyUnit> setConcept(PropertyUnit updatedConcept) {
		// TODO Auto-generated method stub
		return null;
	}

}
