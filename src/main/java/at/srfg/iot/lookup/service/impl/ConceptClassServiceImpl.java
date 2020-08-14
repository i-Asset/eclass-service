package at.srfg.iot.lookup.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import at.srfg.iot.classification.model.ConceptClass;
import at.srfg.iot.classification.model.Property;
import at.srfg.iot.eclass.service.DataDuplicationService;
import at.srfg.iot.lookup.repository.ConceptClassPropertyRepository;
import at.srfg.iot.lookup.service.ConceptClassService;

@Service
public class ConceptClassServiceImpl extends ConceptServiceImpl<ConceptClass> implements ConceptClassService {
	@Autowired
	DataDuplicationService duplexer;
	
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
	public Optional<ConceptClass> addConcept(ConceptClass newConcept) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public Optional<ConceptClass> setConcept(ConceptClass updatedConcept) {
		// TODO Auto-generated method stub
		return Optional.empty();
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
