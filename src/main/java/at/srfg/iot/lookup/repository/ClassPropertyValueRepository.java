package at.srfg.iot.lookup.repository;

import org.springframework.data.repository.CrudRepository;

import at.srfg.iot.classification.model.ClassPropertyValue;
import at.srfg.iot.classification.model.ClassPropertyValuePK;

public interface ClassPropertyValueRepository extends CrudRepository<ClassPropertyValue, ClassPropertyValuePK>{

}
