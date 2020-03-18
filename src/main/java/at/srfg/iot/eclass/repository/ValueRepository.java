package at.srfg.iot.eclass.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import at.srfg.iot.eclass.model.ClassificationClass;
import at.srfg.iot.eclass.model.Property;
import at.srfg.iot.eclass.model.Value;

public interface ValueRepository extends CrudRepository<Value, String>{
	Value findByIrdiVA(String irdiVA);
	List<Value> findByIdentifier(String eceCode);
	/**
	 * Retrieve the available/allowed values for the combination of {@link ClassificationClass} and
	 * {@link Property}.
	 * @param irdiCC The IRDI pointing to the {@link ClassificationClass}
	 * @param irdiPR The IRDI pointing to the {@link Property}
	 * @return
	 */
	@Query("SELECT ccprva.value FROM ClassificationClassPropertyValue ccprva WHERE ccprva.id.irdiCC = ?1 and ccprva.id.irdiPR =?2 ")
	List<Value> getValues(String irdiCC, String irdiPR);
	/**
	 * Retrieve the available/allowed values for the combination of {@link ClassificationClass} and
	 * {@link Property}.
	 * @param irdiCC The {@link ClassificationClass}
	 * @param irdiPR The {@link Property}
	 * @return
	 */
	@Query("SELECT ccprva.value FROM ClassificationClassPropertyValue ccprva WHERE ccprva.classificationClass = ?1 and ccprva.property =?2 ")
	List<Value> getValues(ClassificationClass classificationClass, Property property);
}
