package at.srfg.iot.lookup.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;

import at.srfg.iot.classification.model.ConceptClass;
import at.srfg.iot.classification.model.ConceptProperty;
import at.srfg.iot.classification.model.ConceptPropertyUnit;
import at.srfg.iot.classification.model.ConceptPropertyValue;
import at.srfg.iot.eclass.service.DataDuplicationService;
import at.srfg.iot.lookup.repository.ClassPropertyValueRepository;
import at.srfg.iot.lookup.service.PropertyService;
import at.srfg.iot.lookup.service.indexing.SemanticIndexer;

@Service
public class PropertyServiceImpl extends ConceptServiceImpl<ConceptProperty> implements PropertyService {
	@Autowired
	private SemanticIndexer indexer;
	
	@Autowired
	private ClassPropertyValueRepository classPropertyValueRepository;
	
	@Autowired
	private PropertyUnitServiceImpl propertyUnitService;

	@Autowired
	private PropertyValueServiceImpl propertyValueService;

	@Autowired
	DataDuplicationService duplexer;
	
	public Optional<ConceptProperty> getConcept(String identifier) {
		Optional<ConceptProperty> ccOpt = typeRepository.findByConceptId(identifier);
		if (!ccOpt.isPresent()) {
			return duplexer.copyProperty(identifier);
			
		}
		else {
			return ccOpt;
		}
	}

	@Override
	public Optional<ConceptProperty> addConcept(ConceptProperty newConcept) {
		ConceptProperty toStore = new ConceptProperty(newConcept.getConceptId());
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
		ConceptProperty stored = typeRepository.save(toStore);
		indexer.store(toStore);
		return Optional.of(stored);
	}
	/**
	 * Helper method for assigning a {@link ConceptPropertyUnit} - will be created when not present
	 * @param property
	 * @param unit
	 */
	private void checkPropertyUnit(ConceptProperty property, ConceptPropertyUnit unit) {
		if (unit != null && ! Strings.isNullOrEmpty(unit.getConceptId())) {
			// check the provided unit (the identifier is important to find the stuff)
			Optional<ConceptPropertyUnit> stored = propertyUnitService.getConcept(unit.getConceptId());
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
	 * Helper method for assigning a {@link ConceptPropertyValue} - will be created when not preset
	 * @param property
	 * @param value
	 * @return
	 */
	private Optional<ConceptPropertyValue> checkPropertyValue(ConceptProperty property, ConceptPropertyValue value) {
		if (value != null && ! Strings.isNullOrEmpty(value.getConceptId())) {
			// check the provided unit (the identifier is important to find the stuff)
			Optional<ConceptPropertyValue> stored = propertyValueService.getConcept(value.getConceptId());
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
	 * Helper for processing the list of provided {@link ConceptPropertyValue} elements
 	 * @param property
	 * @param values
	 */
	private void checkPropertyValues(ConceptProperty property, Set<ConceptPropertyValue> values) {
		Set<ConceptPropertyValue> propValues = new HashSet<>();
		if ( values != null && ! values.isEmpty()) {
			for (ConceptPropertyValue value : values ) {
				Optional<ConceptPropertyValue> stored = checkPropertyValue(property, value);
				if ( stored.isPresent()) {
					propValues.add(stored.get());
				}
			}
			property.setValues(propValues);
		}
	}
	@Override
	public Optional<ConceptProperty> setConcept(ConceptProperty updated) {
		Optional<ConceptProperty> stored = getStoredConcept(updated);
		if ( stored.isPresent()) {
			ConceptProperty property = stored.get();
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
			// store in database
			typeRepository.save(property);
			// also store in index
			indexer.store(property);
			//
			return Optional.of(property);
		}
		return Optional.empty();
	}

	@Override
	public Set<ConceptPropertyValue> getValues(String identifier, String classIdentifier) {
		Optional<ConceptProperty> property = getConcept(identifier);
		if (property.isPresent()) {
			if (! Strings.isNullOrEmpty(classIdentifier)) {
				Optional<ConceptClass> cClass = getConcept(classIdentifier, ConceptClass.class);
				if ( cClass.isPresent() ) {
					List<ConceptPropertyValue> cClassPropertyValues 
						= classPropertyValueRepository.findByConceptClassAndProperty(cClass.get(), property.get());
					if (! cClassPropertyValues.isEmpty()) {
						Set<ConceptPropertyValue> values = new HashSet<>();
						values.addAll(cClassPropertyValues);
						return values;
					}
				}
			}
			return property.get().getValues();
		}
		
		return new HashSet<>();
	}
	public boolean deleteConcept(String identifier) {
		Optional<ConceptProperty> property = typeRepository.findByConceptId(identifier);
		if (property.isPresent()) {
			typeRepository.delete(property.get());
			indexer.remove(property.get());
			return true;
		}
		return false;
	}

}
