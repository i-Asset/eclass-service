package at.srfg.iot.eclass.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import at.srfg.iot.classification.model.ConceptClass;

public interface ConceptClassRepository extends CrudRepository<ConceptClass, String>{
	List<ConceptClass> findByLevelAndCodedNameLike(int level, String codedName);
}
