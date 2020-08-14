package at.srfg.iot.lookup.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;

import at.srfg.iot.classification.model.ConceptClass;
import at.srfg.iot.classification.model.Property;
import at.srfg.iot.classification.model.PropertyUnit;
import at.srfg.iot.classification.model.PropertyValue;
import at.srfg.iot.eclass.service.DataDuplicationService;
import at.srfg.iot.lookup.repository.ClassPropertyValueRepository;
import at.srfg.iot.lookup.service.PropertyService;

@Service
public class PropertyServiceImpl extends ConceptServiceImpl<Property> implements PropertyService {
	@Autowired
	private ClassPropertyValueRepository classPropertyValueRepository;
	
	@Autowired
	private PropertyUnitServiceImpl propertyUnitService;

	@Autowired
	private PropertyValueServiceImpl propertyValueService;

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
		// 
		checkPropertyUnit(toStore, newConcept.getUnit());
		// 
		if ( newConcept.getValues() != null ) {
			checkPropertyValues(toStore, newConcept.getValues());
		}
		Property stored = typeRepository.save(toStore);
		return Optional.of(stored);
	}
	/**
	 * Helper method for assigning a {@link PropertyUnit} - will be created when not present
	 * @param property
	 * @param unit
	 */
	private void checkPropertyUnit(Property property, PropertyUnit unit) {
		if (unit != null && ! Strings.isNullOrEmpty(unit.getConceptId())) {
			// check the provided unit (the identifier is important to find the stuff)
			Optional<PropertyUnit> stored = propertyUnitService.getConcept(unit.getConceptId());
			if ( stored.isPresent()) {
				property.setUnit(stored.get());
			}
			else {
				stored = propertyUnitService.addConcept(unit);
				if ( stored.isPresent()) {
					property.setUnit(stored.get());
				}
			}
		}
	}
	/**
	 * Helper method for assigning a {@link PropertyValue} - will be created when not preset
	 * @param property
	 * @param value
	 * @return
	 */
	private Optional<PropertyValue> checkPropertyValue(Property property, PropertyValue value) {
		if (value != null && ! Strings.isNullOrEmpty(value.getConceptId())) {
			// check the provided unit (the identifier is important to find the stuff)
			Optional<PropertyValue> stored = propertyValueService.getConcept(value.getConceptId());
			if ( stored.isPresent()) {
				return stored;
			}
			else {
				return propertyValueService.addConcept(value);
			}
		}
		return Optional.empty();
	}
	/**
	 * Helper for processing the list of provided {@link PropertyValue} elements
 	 * @param property
	 * @param values
	 */
	private void checkPropertyValues(Property property, Set<PropertyValue> values) {
		Set<PropertyValue> propValues = new HashSet<>();
		if ( values != null && ! values.isEmpty()) {
			for (PropertyValue value : values ) {
				Optional<PropertyValue> stored = checkPropertyValue(property, value);
				if ( stored.isPresent()) {
					propValues.add(stored.get());
				}
			}
			property.setValues(propValues);
		}
	}
	@Override
	public Optional<Property> setConcept(Property updated) {
		Optional<Property> stored = getStoredConcept(updated);
		if ( stored.isPresent()) {
			Property property = stored.get();
			// description
			property.setDescription(updated.getDescription());
			// note
			if (! Strings.isNullOrEmpty(updated.getNote())) {
				property.setNote(updated.getNote());
			}
			// remark
			if (! Strings.isNullOrEmpty(updated.getRemark())) {
				property.setRemark(updated.getRemark());
			}
			// shortName
			if (! Strings.isNullOrEmpty(updated.getShortName())) {
				property.setShortName(updated.getShortName());
			}
			// category
			if (! Strings.isNullOrEmpty(updated.getCategory())) {
				property.setCategory(updated.getCategory());
			}
			// unit
			checkPropertyUnit(property, updated.getUnit());
			// values
			checkPropertyValues(property, updated.getValues());
			//
			typeRepository.save(property);
			return Optional.of(property);
		}
		return Optional.empty();
	}

	@Override
	public Set<PropertyValue> getValues(String identifier, String classIdentifier) {
		Optional<Property> property = getConcept(identifier);
		if (property.isPresent()) {
			if (! Strings.isNullOrEmpty(classIdentifier)) {
				Optional<ConceptClass> cClass = getConcept(classIdentifier, ConceptClass.class);
				if ( cClass.isPresent() ) {
					List<PropertyValue> cClassPropertyValues 
						= classPropertyValueRepository.findByConceptClassAndProperty(cClass.get(), property.get());
					if (! cClassPropertyValues.isEmpty()) {
						Set<PropertyValue> values = new HashSet<>();
						values.addAll(cClassPropertyValues);
						return values;
					}
				}
			}
			return property.get().getValues();
		}
		
		return new HashSet<>();
	}

}
