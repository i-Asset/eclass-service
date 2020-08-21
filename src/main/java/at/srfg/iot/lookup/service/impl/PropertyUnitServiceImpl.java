package at.srfg.iot.lookup.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.google.common.base.Strings;

import at.srfg.iot.classification.model.ConceptPropertyUnit;
import at.srfg.iot.lookup.service.PropertyUnitService;

@Service
public class PropertyUnitServiceImpl extends ConceptServiceImpl<ConceptPropertyUnit> implements PropertyUnitService {

	@Override
	public Optional<ConceptPropertyUnit> addConcept(ConceptPropertyUnit newConcept) {
		ConceptPropertyUnit toStore = new ConceptPropertyUnit(newConcept.getConceptId());
		toStore.setShortName(newConcept.getShortName());
		toStore.setDescription(newConcept.getDescription());
		toStore.setNote(newConcept.getNote());
		toStore.setRemark(newConcept.getRemark());
		toStore.setRevisionNumber(newConcept.getRevisionNumber());
		// 
		toStore.setDinNotation(newConcept.getDinNotation());
		toStore.setEceCode(newConcept.getEceCode());
		toStore.setEceName(newConcept.getEceName());
		toStore.setSiName(newConcept.getSiName());
		toStore.setSiNotation(newConcept.getSiNotation());
		toStore.setStructuredNaming(newConcept.getStructuredNaming());
		toStore.setNistName(newConcept.getNistName());
		// store the unit
		ConceptPropertyUnit stored = typeRepository.save(toStore);
		return Optional.of(stored);
	}

	@Override
	public Optional<ConceptPropertyUnit> setConcept(ConceptPropertyUnit updated) {
		Optional<ConceptPropertyUnit> stored = getStoredConcept(updated);
		if ( stored.isPresent()) {
			ConceptPropertyUnit property = stored.get();
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
			// dinNotation
			if (! Strings.isNullOrEmpty(updated.getDinNotation())) {
				property.setDinNotation(updated.getDinNotation());
			}
			// eceCode
			if (! Strings.isNullOrEmpty(updated.getEceCode())) {
				property.setEceCode(updated.getEceCode());
			}
			// eceName
			if (! Strings.isNullOrEmpty(updated.getEceName())) {
				property.setEceName(updated.getEceName());
			}
			// siName
			if (! Strings.isNullOrEmpty(updated.getSiName())) {
				property.setSiName(updated.getSiName());
			}
			// siNotation
			if (! Strings.isNullOrEmpty(updated.getSiNotation())) {
				property.setSiNotation(updated.getSiNotation());
			}
			// structuredNaming
			if (! Strings.isNullOrEmpty(updated.getStructuredNaming())) {
				property.setStructuredNaming(updated.getStructuredNaming());
			}
			// nistName
			if (! Strings.isNullOrEmpty(updated.getNistName())) {
				property.setNistName(updated.getNistName());
			}
			// iecClassification
			if (! Strings.isNullOrEmpty(updated.getIecClassification())) {
				property.setIecClassification(updated.getIecClassification());
			}

			//
			typeRepository.save(property);
			return Optional.of(property);
		}
		return Optional.empty();
	}

}
