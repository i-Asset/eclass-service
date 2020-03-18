package at.srfg.iot.eclass.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import at.srfg.iot.eclass.model.ClassificationClass;
import at.srfg.iot.eclass.model.Property;
/**
 * JPA Repository for working with {@link Property} entities.
 * @author dglachs
 *
 */
public interface PropertyRepository extends CrudRepository<Property, String>{
	Property findByIrdiPR(String irdiPR);
	List<Property> findByIdPR(String idPR);
	List<Property> findByIdentifier(String identifier);
	@Query("SELECT ccpr.property FROM ClassificationClassProperty ccpr WHERE ccpr.idCC = ?1")
	List<Property> findByIdCC(String idCC);
	@Query("SELECT ccpr.property FROM ClassificationClassProperty ccpr WHERE ccpr.classCodedName = ?1")
	List<Property> findByClassCodedName(String classCodedName);
	@Query("SELECT ccpr.property FROM ClassificationClassProperty ccpr WHERE ccpr.classificationClass = ?1")
	List<Property> findByClassificationClass(ClassificationClass classificationClass);
	@Query("SELECT ccpr.property FROM ClassificationClassProperty ccpr WHERE ccpr.id.irdiCC = ?1")
	List<Property> findByIrdiCC(String irdiCC);
	/**
	 * Retrieve all properties by category
	 * @param category
	 * @return
	 */
	List<Property> findByCategory(String category);
	
}
