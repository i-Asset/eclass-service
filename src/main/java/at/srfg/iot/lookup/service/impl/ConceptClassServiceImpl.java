package at.srfg.iot.lookup.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Strings;

import at.srfg.iot.classification.model.ConceptClass;
import at.srfg.iot.classification.model.ConceptClassProperty;
import at.srfg.iot.classification.model.ConceptProperty;
import at.srfg.iot.classification.model.ConceptPropertyValue;
import at.srfg.iot.eclass.service.DataDuplicationService;
import at.srfg.iot.lookup.repository.ConceptClassPropertyRepository;
import at.srfg.iot.lookup.service.ConceptClassService;
import at.srfg.iot.lookup.service.PropertyService;
import at.srfg.iot.lookup.service.indexing.SemanticIndexer;

@Service
public class ConceptClassServiceImpl extends ConceptServiceImpl<ConceptClass> implements ConceptClassService {
	@Autowired
	private SemanticIndexer indexer;
	
	@Autowired
	private DataDuplicationService duplexer;
	@Autowired
	private PropertyService propertyService;
	@Autowired
	private ConceptClassPropertyRepository conceptClassPropertyRepository;
	
	public Optional<ConceptClass> getConcept(String identifier) {
		Optional<ConceptClass> ccOpt = typeRepository.findByConceptId(identifier);
		if (!ccOpt.isPresent()) {
			return duplexer.copyClassificationClass(identifier);
			
		}
		else {
			return ccOpt;
		}
	}
	private Optional<ConceptClass> addConcept(ConceptClass parent, ConceptClass newConcept) {
		ConceptClass toStore = new ConceptClass(parent, newConcept.getConceptId());
		toStore.setShortName(newConcept.getShortName());
		toStore.setPreferredLabel(newConcept.getPreferredLabel());
		toStore.setAlternateLabel(newConcept.getAlternateLabel());
		toStore.setHiddenLabel(newConcept.getHiddenLabel());
		toStore.setDefinition(newConcept.getDefinition());
		toStore.setComment(newConcept.getComment());
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
	public Optional<ConceptClass> addConcept(String parentConceptIdentifier, ConceptClass newConcept) {
		ConceptClass parent = null;
		if (! Strings.isNullOrEmpty(parentConceptIdentifier)) {
			Optional<ConceptClass> parentClass = getConcept(parentConceptIdentifier);
			if (parentClass.isPresent()) {
				parent = parentClass.get();
			}
		}
		return addConcept(parent, newConcept);
//		ConceptClass toStore = new ConceptClass(parent, newConcept.getConceptId());
//		toStore.setShortName(newConcept.getShortName());
//		toStore.setPreferredLabel(newConcept.getPreferredLabel());
//		toStore.setAlternateLabel(newConcept.getAlternateLabel());
//		toStore.setHiddenLabel(newConcept.getHiddenLabel());
//		toStore.setDefinition(newConcept.getDefinition());
//		toStore.setComment(newConcept.getComment());
//		toStore.setNote(newConcept.getNote());
//		toStore.setRemark(newConcept.getRemark());
//		toStore.setRevisionNumber(newConcept.getRevisionNumber());
//		// 
//		toStore.setCodedName(newConcept.getCodedName());
//		toStore.setLevel(newConcept.getLevel());
//		// store the unit
//		ConceptClass stored = typeRepository.save(toStore);
//		indexer.store(toStore);
//		return Optional.of(stored);
	}

	@Override
	public Optional<ConceptClass> addConcept(ConceptClass newConcept) {
		ConceptClass parent = newConcept.getParentElement();
		if ( parent != null ) {
			Optional<ConceptClass> parentClass = getStoredConcept(parent);
			return addConcept(parentClass.orElse(null), newConcept);
		}
		return addConcept(parent,newConcept);
	}

	@Override
	public ConceptClass setConcept(ConceptClass property, ConceptClass updated) {
		property.setPreferredLabel(updated.getPreferredLabel());
		property.setAlternateLabel(updated.getAlternateLabel());
		property.setHiddenLabel(updated.getHiddenLabel());
		property.setDefinition(updated.getDefinition());
		property.setComment(updated.getComment());
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
		// deal with parent element
		if ( updated.getParentElement()!= null) {
			Optional<ConceptClass> parent = setConcept(updated.getParentElement()); 
			// parent "should be" present
			if (parent.isPresent()) {
				property.setParentElement(parent.get());
			}
		}
		//
//		indexer.store(property);
		//
		return typeRepository.save(property);
	}

	@Transactional
	@Override
	public Optional<ConceptClass> setConcept(ConceptClass updated) {
		Optional<ConceptClass> stored = getStoredConcept(updated);
		if ( stored.isPresent()) {
			ConceptClass saved = setConcept(stored.get(),  updated);
			indexer.store(saved);
			return Optional.of(saved);
		}
		else {
			return addConcept(updated);
		}
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
	public List<ConceptProperty> getProperties(String identifier) {
		return getProperties(identifier, true);
	}
	public List<ConceptProperty> getProperties(String identifier, boolean complete) {
		Optional<ConceptClass> ccOpt = getConcept(identifier);
		if ( ccOpt.isPresent()) {
			ConceptClass cc = ccOpt.get();
			return getProperties(cc, complete); 
		}
		return Collections.emptyList();
	}
	/**
	 * Helper method to collect properties from parent classes
	 * @param conceptClass
	 * @return
	 */
	private List<ConceptProperty> getProperties(ConceptClass conceptClass, boolean complete) {
		List<ConceptProperty> properties = new ArrayList<>();
		if ( complete && conceptClass.getParentElement() != null) {
			properties.addAll(getProperties(conceptClass.getParentElement(), complete));
		}
		properties.addAll(conceptClassPropertyRepository.getProperties(conceptClass));
		return properties;
	}
	

	@Override
	public Collection<ConceptProperty> setPropertiesById(String identifier, List<String> properties) {
		Optional<ConceptClass> conceptClass = typeRepository.findByConceptId(identifier);
		if ( conceptClass.isPresent()) {
			ConceptClass cc = conceptClass.get();
			List<ConceptProperty> existing = conceptClassPropertyRepository.getProperties(cc);
			
			for (String property : properties) {
				
				Optional<ConceptProperty> p = getConcept(property, ConceptProperty.class);
				
				if ( p.isPresent() ) {
					if (! existing.contains(p.get())) {
						ConceptClassProperty ccp = new ConceptClassProperty(cc, p.get()); 
						ccp = conceptClassPropertyRepository.save(ccp);
						//
						existing.add(ccp.getProperty());
					}
				}
			}
			return existing;
		}
		throw new IllegalArgumentException("Provided ConceptClass cannot be found by it's id: " + identifier);
	}

	@Override
	public Collection<ConceptProperty> setProperties(String identifier, List<ConceptProperty> properties) {
		Optional<ConceptClass> conceptClass = typeRepository.findByConceptId(identifier);
		if ( conceptClass.isPresent()) {
			ConceptClass cc = conceptClass.get();
			List<ConceptProperty> existing = conceptClassPropertyRepository.getProperties(cc);
			for (ConceptProperty property : properties) {
				if ( existing.contains(property)) {
					
					Optional<ConceptProperty> stored = propertyService.setConcept(property);
					if ( stored.isPresent() ) {
						existing.remove(property);
						existing.add(stored.get());
					}
				}
				else {
					Optional<ConceptProperty> storedProperty = propertyService.getConcept(property.getConceptId());
					if (!storedProperty.isPresent()) {
						Optional<ConceptProperty> stored = propertyService.addConcept(storedProperty.get());
						if ( stored.isPresent()) {
							existing.add(stored.get());
							ConceptClassProperty ccp = new ConceptClassProperty(cc, stored.get());
							conceptClassPropertyRepository.save(ccp);
						}
					}
					else {
						// 
						ConceptProperty changed = propertyService.setConcept(storedProperty.get(),property);
						// add property
						existing.add(changed);
						ConceptClassProperty ccp = new ConceptClassProperty(cc, changed);
						conceptClassPropertyRepository.save(ccp);							
						
					}
				}
			}
			typeRepository.save(cc);
			return existing;
		}
		throw new IllegalArgumentException("Provided ConceptClass cannot be found by it's id: " + identifier);

	}

	@Transactional
	@Override
	public long deleteNameSpace(String nameSpace) {
		long deleteCount = super.deleteNameSpace(nameSpace);
		if ( deleteCount > 0 ) {
//			indexer.
		}
		return deleteCount;
	}
	@Override
	public Collection<ConceptPropertyValue> setPropertyValuesForConceptClassById(String conceptClassIdentifier,
			String conceptPropertyIdentifier, List<String> propertyValueIds) {
		Optional<ConceptClass> conceptClass = typeRepository.findByConceptId(conceptClassIdentifier);
		if ( conceptClass.isPresent()) {
		}
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<ConceptPropertyValue> setPropertyValuesForConceptClass(String conceptClassIdentifier,
			String conceptPropertyIdentifier, List<ConceptPropertyValue> conceptPropertyList) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
