package at.srfg.iot.eclass.repository;

import org.springframework.data.repository.CrudRepository;

import at.srfg.iot.classification.model.ConceptBase;

public interface ConceptRepository<T extends ConceptBase> extends CrudRepository<T, String> {
	
}
