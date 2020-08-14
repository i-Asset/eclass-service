package at.srfg.iot.lookup.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import at.srfg.iot.classification.model.Property;
import at.srfg.iot.classification.model.PropertyUnit;
import at.srfg.iot.classification.model.PropertyValue;
import at.srfg.iot.eclass.service.DataDuplicationService;
import at.srfg.iot.lookup.service.PropertyService;

@Service
public class PropertyServiceImpl extends ConceptServiceImpl<Property> implements PropertyService {
	@Autowired
	DataDuplicationService duplexer;
	
	public Optional<Property> getConcept(String identifier) {
		Optional<Property> ccOpt = typeRepository.findByConceptId(identifier);
		if (!ccOpt.isPresent()) {
			return duplexer.copyProperty(identifier);
			
		}
		else {
			return ccOpt;
		}
	}

	@Override
	public Optional<Property> addConcept(Property newConcept) {
		Property toStore = new Property(newConcept.getConceptId());
		toStore.setCategory(newConcept.getCategory());
		toStore.setCoded(newConcept.isCoded());
		toStore.setDataType(newConcept.getDataType());
		toStore.setRevisionNumber(newConcept.getRevisionNumber());
		toStore.setDescription(newConcept.getDescription());
		toStore.setNote(newConcept.getNote());
		toStore.setRemark(newConcept.getRemark());
		toStore.setShortName(newConcept.getShortName());
		toStore.setSourceOfDefinition(newConcept.getSourceOfDefinition());
		if (newConcept.getUnit() != null ) {
			// check the provided unit (the identifier is important to find the stuff)
			Optional<PropertyUnit> unit = getConcept(newConcept.getUnit().getConceptId(), PropertyUnit.class);
			if ( unit.isPresent()) {
				toStore.setUnit(unit.get());
			}
		}
		if ( newConcept.getValues() != null ) {
			// deal with the values
		}
		Property stored = typeRepository.save(toStore);
		return Optional.of(stored);
	}

	@Override
	public Optional<Property> setConcept(Property updatedConcept) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public List<PropertyValue> getValues(String identifier) {
		// TODO Auto-generated method stub
		return null;
	}

}
