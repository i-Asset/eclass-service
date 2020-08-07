package at.srfg.iot.eclass.repository;

import org.springframework.data.repository.CrudRepository;

import at.srfg.iot.classification.model.ClassPropertyValue;
import at.srfg.iot.classification.model.ClassPropertyValuePK;

public interface ClassPropertyValueRepository extends CrudRepository<ClassPropertyValue, ClassPropertyValuePK>{

}
