package at.srfg.iot.eclass.repository;

import org.springframework.data.repository.CrudRepository;

import at.srfg.iot.classification.model.ClassProperty;
import at.srfg.iot.classification.model.ClassPropertyPK;

public interface ClassPropertyRepository extends CrudRepository<ClassProperty, ClassPropertyPK>{

}
