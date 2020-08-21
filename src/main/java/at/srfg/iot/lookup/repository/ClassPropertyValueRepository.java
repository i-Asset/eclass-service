package at.srfg.iot.lookup.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import at.srfg.iot.classification.model.ConceptClassPropertyValue;
import at.srfg.iot.classification.model.ConceptClassPropertyValuePK;
import at.srfg.iot.classification.model.ConceptClass;
import at.srfg.iot.classification.model.ConceptProperty;
import at.srfg.iot.classification.model.ConceptPropertyValue;

public interface ClassPropertyValueRepository extends CrudRepository<ConceptClassPropertyValue, ConceptClassPropertyValuePK>{
	@Query("SELECT c.propertyValue FROM ConceptClassPropertyValue c WHERE c.classProperty.conceptClass = ?1 AND c.classProperty.property = ?2 ")
	List<ConceptPropertyValue> findByConceptClassAndProperty(ConceptClass classId, ConceptProperty propertyId);
}
