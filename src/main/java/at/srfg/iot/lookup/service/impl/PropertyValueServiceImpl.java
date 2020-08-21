package at.srfg.iot.lookup.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.google.common.base.Strings;

import at.srfg.iot.classification.model.ConceptPropertyValue;
import at.srfg.iot.lookup.service.PropertyValueService;

@Service
public class PropertyValueServiceImpl extends ConceptServiceImpl<ConceptPropertyValue> implements PropertyValueService {

	@Override
	public Optional<ConceptPropertyValue> addConcept(ConceptPropertyValue newConcept) {
		ConceptPropertyValue toStore = new ConceptPropertyValue(newConcept.getConceptId());
		toStore.setShortName(newConcept.getShortName());
		toStore.setDescription(newConcept.getDescription());
		toStore.setNote(newConcept.getNote());
		toStore.setRemark(newConcept.getRemark());
		toStore.setRevisionNumber(newConcept.getRevisionNumber());
		// 
		toStore.setDefinition(newConcept.getDefinition());
		toStore.setDataType(newConcept.getDataType());
		toStore.setReference(newConcept.getReference());
		toStore.setValue(newConcept.getValue());
		// store the unit
		ConceptPropertyValue stored = typeRepository.save(toStore);
		return Optional.of(stored);
	}

	@Override
	public Optional<ConceptPropertyValue> setConcept(ConceptPropertyValue updated) {
		Optional<ConceptPropertyValue> stored = getStoredConcept(updated);
		if ( stored.isPresent()) {
			ConceptPropertyValue property = stored.get();
			// description
			property.setDescription(updated.getDescription());
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
			// reference
			if (! Strings.isNullOrEmpty(updated.getReference())) {
				property.setReference(updated.getReference());
			}
			// definition
			if (! Strings.isNullOrEmpty(updated.getDefinition())) {
				property.setDefinition(updated.getDefinition());
			}
			// value
			if (! Strings.isNullOrEmpty(updated.getValue())) {
				property.setValue(updated.getValue());
			}
			//
			typeRepository.save(property);
			return Optional.of(property);
		}
		return Optional.empty();
	}

}
