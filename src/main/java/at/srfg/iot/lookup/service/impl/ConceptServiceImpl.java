package at.srfg.iot.lookup.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;

import at.srfg.iot.classification.model.ConceptBase;
import at.srfg.iot.classification.model.ConceptBaseDescription;
import at.srfg.iot.lookup.repository.ConceptRepository;
import at.srfg.iot.lookup.service.ConceptService;
/**
 * Service for dealing with semantic lookup concepts (ConceptClass, Property, PropertyUnit, PropertyValue)
 * @author dglachs
 *
 * @param <T>
 */
@Service
public abstract class ConceptServiceImpl<T extends ConceptBase> implements ConceptService<T> {
	@Autowired
	private ConceptRepository<ConceptBase> baseRepository;
	@Autowired
	protected ConceptRepository<T> typeRepository;
	public <O extends ConceptBase> Optional<O> getConcept(String identifier, Class<O> clazz) {
		Optional<ConceptBase> concept = baseRepository.findByConceptId(identifier);
		if ( concept.isPresent() ) {
			ConceptBase base = concept.get();
			if ( clazz.isInstance(base)) {
				return Optional.of(clazz.cast(base));
			}
		}
		return Optional.empty();
	}
	protected Optional<T> getStoredConcept(T updated) {
		if (! Strings.isNullOrEmpty(updated.getConceptId())) {
			return typeRepository.findByConceptId(updated.getConceptId());
		}
		return Optional.empty();
	}
	public Optional<T> getConcept(String identifier) {
		return typeRepository.findByConceptId(identifier);
	}
	public Optional<T> getConcept(String nameSpace, String localName) {
		return typeRepository.findByNameSpaceAndLocalName(nameSpace, localName);
	}
	public boolean deleteConcept(String identifier) {
		Optional<T> base = typeRepository.findByConceptId(identifier);
		if (base.isPresent()) {
			typeRepository.delete(base.get());
			return true;
		}
		return false;
	}
	@Override
	public Optional<T> setDescription(String identifier, ConceptBaseDescription desc) {
		Optional<T> baseOpt = getConcept(identifier);
		if ( baseOpt.isPresent()) {
			T base = baseOpt.get();
			base.setDescription(desc.getLanguage(), desc.getPreferredName(), desc.getDefinition());
			return Optional.of(base);
		}
		return baseOpt;
	}
	@Override
	public boolean conceptExists(String identifier) {
		return baseRepository.existsByConceptId(identifier);
	}
}
