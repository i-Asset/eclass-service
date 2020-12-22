package at.srfg.iot.lookup.repository;

import java.util.List;
import java.util.Optional;

import at.srfg.iot.common.datamodel.semanticlookup.model.ConceptClass;

public interface ConceptClassRepository extends ConceptRepository<ConceptClass>{
	List<ConceptClass> findByLevelAndCodedNameLike(int level, String codedName);
	Optional<ConceptClass> findByLevelAndCodedName(int level, String codedName);
	List<ConceptClass> findByParentElement(ConceptClass parent);
}
