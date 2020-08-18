package at.srfg.iot.lookup.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;

import at.srfg.iot.classification.model.ConceptClass;
import at.srfg.iot.classification.model.Property;
import at.srfg.iot.eclass.service.DataDuplicationService;
import at.srfg.iot.lookup.repository.ConceptClassPropertyRepository;
import at.srfg.iot.lookup.service.ConceptClassService;
import at.srfg.iot.lookup.service.indexing.SemanticIndexer;

@Service
public class ConceptClassServiceImpl extends ConceptServiceImpl<ConceptClass> implements ConceptClassService {
	@Autowired
	private SemanticIndexer indexer;
	
	@Autowired
	private DataDuplicationService duplexer;
	
	@Autowired
	ConceptClassPropertyRepository conceptClassPropertyRepository;
	public Optional<ConceptClass> getConcept(String identifier) {
		Optional<ConceptClass> ccOpt = typeRepository.findByConceptId(identifier);
		if (!ccOpt.isPresent()) {
			return duplexer.copyClassificationClass(identifier);
			
		}
		else {
			return ccOpt;
		}
	}

	@Override
	public Optional<ConceptClass> addConcept(String parentConceptIdentifier, ConceptClass newConcept) {
		ConceptClass parent = null;
		if (! Strings.isNullOrEmpty(parentConceptIdentifier)) {
			Optional<ConceptClass> parentClass = getConcept(parentConceptIdentifier);
			if (parentClass.isPresent()) {
				parent = parentClass.get();
			}
		}
		ConceptClass toStore = new ConceptClass(parent, newConcept.getConceptId());
		toStore.setShortName(newConcept.getShortName());
		toStore.setDescription(newConcept.getDescription());
		toStore.setNote(newConcept.getNote());
		toStore.setRemark(newConcept.getRemark());
		toStore.setRevisionNumber(newConcept.getRevisionNumber());
		// 
		toStore.setCodedName(newConcept.getCodedName());
		toStore.setLevel(newConcept.getLevel());
		// store the unit
		ConceptClass stored = typeRepository.save(toStore);
		indexer.store(toStore);
		return Optional.of(stored);
	}

	@Override
	public Optional<ConceptClass> addConcept(ConceptClass newConcept) {
		return addConcept(null, newConcept);
	}

	@Override
	public Optional<ConceptClass> setConcept(ConceptClass updated) {
		Optional<ConceptClass> stored = getStoredConcept(updated);
		if ( stored.isPresent()) {
			ConceptClass property = stored.get();
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
			if (! Strings.isNullOrEmpty(updated.getCodedName())) {
				property.setCodedName(updated.getCodedName());
			}
			// TODO: deal with parent element
			
			//
			typeRepository.save(property);
			return Optional.of(property);
		}
		return Optional.empty();
	}
	public boolean deleteConcept(String identifier) {
		Optional<ConceptClass> conceptClass = typeRepository.findByConceptId(identifier);
		if (conceptClass.isPresent()) {
			ConceptClass toDelete = conceptClass.get();
			// delete all children from index & db
			deleteChildren(toDelete.getChildElements());
			// delete from database
			typeRepository.delete(conceptClass.get());
			// delete from index
			indexer.remove(conceptClass.get());
			return true;
		}
		return false;
	}
	private void deleteChildren(List<ConceptClass> children) {
		for (ConceptClass child : children) {
			deleteChildren(child.getChildElements());
			// delete from index
			indexer.remove(child);
			// delete from db
			typeRepository.delete(child);
		}
		
	}


	@Override
	public List<Property> getProperties(String identifier) {
		Optional<ConceptClass> ccOpt = getConcept(identifier);
		if ( ccOpt.isPresent()) {
			ConceptClass cc = ccOpt.get();
			return getProperties(cc); 
		}
		return Collections.emptyList();
	}
	/**
	 * Helper method to collect properties from parent classes
	 * @param conceptClass
	 * @return
	 */
	private List<Property> getProperties(ConceptClass conceptClass) {
		List<Property> properties = new ArrayList<>();
		if ( conceptClass.getParentElement() != null) {
			properties.addAll(getProperties(conceptClass.getParentElement()));
		}
		properties.addAll(conceptClassPropertyRepository.getProperties(conceptClass));
		return properties;
	}

}
