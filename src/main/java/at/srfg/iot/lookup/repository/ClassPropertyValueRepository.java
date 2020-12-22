package at.srfg.iot.lookup.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import at.srfg.iot.common.datamodel.semanticlookup.model.ConceptClass;
import at.srfg.iot.common.datamodel.semanticlookup.model.ConceptClassPropertyValue;
import at.srfg.iot.common.datamodel.semanticlookup.model.ConceptClassPropertyValuePK;
import at.srfg.iot.common.datamodel.semanticlookup.model.ConceptProperty;
import at.srfg.iot.common.datamodel.semanticlookup.model.ConceptPropertyValue;

public interface ClassPropertyValueRepository extends CrudRepository<ConceptClassPropertyValue, ConceptClassPropertyValuePK>{
	@Query("SELECT c.propertyValue FROM ConceptClassPropertyValue c WHERE c.classProperty.conceptClass = ?1 AND c.classProperty.property = ?2 ")
	List<ConceptPropertyValue> findByConceptClassAndProperty(ConceptClass classId, ConceptProperty propertyId);
}
