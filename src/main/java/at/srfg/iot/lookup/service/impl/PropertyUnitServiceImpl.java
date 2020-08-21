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
	public Optional<ConceptPropertyUnit> setConcept(ConceptPropertyUnit unit,
			ConceptPropertyUnit updated) {
		unit.setDescription(updated.getDescription());
		unit.setDescription(updated.getDescription());
		// note
		if (! Strings.isNullOrEmpty(updated.getNote())) {
			unit.setNote(updated.getNote());
		}
		// remark
		if (! Strings.isNullOrEmpty(updated.getRemark())) {
			unit.setRemark(updated.getRemark());
		}
		// shortName
		if (! Strings.isNullOrEmpty(updated.getShortName())) {
			unit.setShortName(updated.getShortName());
		}
		// dinNotation
		if (! Strings.isNullOrEmpty(updated.getDinNotation())) {
			unit.setDinNotation(updated.getDinNotation());
		}
		// eceCode
		if (! Strings.isNullOrEmpty(updated.getEceCode())) {
			unit.setEceCode(updated.getEceCode());
		}
		// eceName
		if (! Strings.isNullOrEmpty(updated.getEceName())) {
			unit.setEceName(updated.getEceName());
		}
		// siName
		if (! Strings.isNullOrEmpty(updated.getSiName())) {
			unit.setSiName(updated.getSiName());
		}
		// siNotation
		if (! Strings.isNullOrEmpty(updated.getSiNotation())) {
			unit.setSiNotation(updated.getSiNotation());
		}
		// structuredNaming
		if (! Strings.isNullOrEmpty(updated.getStructuredNaming())) {
			unit.setStructuredNaming(updated.getStructuredNaming());
		}
		// nistName
		if (! Strings.isNullOrEmpty(updated.getNistName())) {
			unit.setNistName(updated.getNistName());
		}
		// iecClassification
		if (! Strings.isNullOrEmpty(updated.getIecClassification())) {
			unit.setIecClassification(updated.getIecClassification());
		}
		
		//
		typeRepository.save(unit);
		return Optional.of(unit);
	}

	@Override
	public Optional<ConceptPropertyUnit> setConcept(ConceptPropertyUnit updated) {
		Optional<ConceptPropertyUnit> stored = getStoredConcept(updated);
		if ( stored.isPresent()) {
			return setConcept(stored.get(),updated);
		}
		return Optional.empty();
	}

}
