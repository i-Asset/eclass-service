package at.srfg.iot.eclass.repository;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import at.srfg.iot.eclass.model.ClassificationClass;
import at.srfg.iot.eclass.model.ClassificationClassPropertyValue;
import at.srfg.iot.eclass.model.ClassificationClassPropertyValuePK;
import at.srfg.iot.eclass.model.Property;

public interface ClassificationClassPropertyValueRepository extends CrudRepository<ClassificationClassPropertyValue, ClassificationClassPropertyValuePK> {

	List<ClassificationClassPropertyValue> findByClassificationClassAndProperty(ClassificationClass cc, Property pr);
}