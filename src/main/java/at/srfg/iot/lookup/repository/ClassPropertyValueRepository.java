package at.srfg.iot.lookup.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import at.srfg.iot.classification.model.ClassPropertyValue;
import at.srfg.iot.classification.model.ClassPropertyValuePK;
import at.srfg.iot.classification.model.ConceptClass;
import at.srfg.iot.classification.model.Property;
import at.srfg.iot.classification.model.PropertyValue;

public interface ClassPropertyValueRepository extends CrudRepository<ClassPropertyValue, ClassPropertyValuePK>{
	@Query("SELECT c.propertyValue FROM ClassPropertyValue c WHERE c.classProperty.conceptClass = ?1 AND c.classProperty.property = ?2 ")
	List<PropertyValue> findByConceptClassAndProperty(ConceptClass classId, Property propertyId);
}
