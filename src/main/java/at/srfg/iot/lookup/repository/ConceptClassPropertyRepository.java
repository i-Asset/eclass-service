package at.srfg.iot.lookup.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import at.srfg.iot.classification.model.ClassProperty;
import at.srfg.iot.classification.model.ClassPropertyPK;
import at.srfg.iot.classification.model.ConceptClass;
import at.srfg.iot.classification.model.Property;

public interface ConceptClassPropertyRepository extends CrudRepository<ClassProperty, ClassPropertyPK>{
	@Query("SELECT c.property FROM ClassProperty c WHERE c.conceptClass = ?1 ")
	List<Property> getProperties(ConceptClass conceptClass);
	

}
