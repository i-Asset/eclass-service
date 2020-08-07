package at.srfg.iot.eclass.repository;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import at.srfg.iot.eclass.model.ClassificationClass;
import at.srfg.iot.eclass.model.ClassificationClassProperty;
import at.srfg.iot.eclass.model.ClassificationClassPropertyPK;
import at.srfg.iot.eclass.model.PropertyDefinition;

public interface ClassificationClassPropertyRepository extends CrudRepository<ClassificationClassProperty, ClassificationClassPropertyPK> {
	int countByPropertyAndClassCodedNameLike(PropertyDefinition property, String classCodedName);
	List<ClassificationClassProperty> findByPropertyAndClassCodedNameLike(PropertyDefinition property, String classCodedName);
	
	List<ClassificationClassProperty> findByClassificationClassAndProperty(ClassificationClass cc, PropertyDefinition pr);
	@Query("SELECT DISTINCT(c.property) FROM ClassificationClassProperty c WHERE c.classCodedName LIKE ?1")
	List<PropertyDefinition> findDistinctPropertyByClassCodedNameLike(String classCodedNamePrefix);
}