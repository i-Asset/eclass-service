package at.srfg.iot.eclass.repository;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import at.srfg.iot.eclass.model.ClassificationClass;
import at.srfg.iot.eclass.model.ClassificationClassPropertyValue;
import at.srfg.iot.eclass.model.ClassificationClassPropertyValuePK;
import at.srfg.iot.eclass.model.PropertyDefinition;

public interface ClassificationClassPropertyValueRepository extends CrudRepository<ClassificationClassPropertyValue, ClassificationClassPropertyValuePK> {
	@Query("SELECT c FROM ClassificationClassPropertyValue c WHERE c.property =?1 and c.classificationClass.codedName LIKE ?2 ")
	List<ClassificationClassPropertyValue> findByPropertyDefinitionAndCodedName(PropertyDefinition pr, String coded);
	List<ClassificationClassPropertyValue> findByClassificationClassAndProperty(ClassificationClass cc, PropertyDefinition pr);
}