package at.srfg.iot.eclass.repository;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import at.srfg.iot.eclass.model.ClassificationClass;

public interface ClassificationClassRepository extends CrudRepository<ClassificationClass, String> {

	ClassificationClass findByIrdiCC(String irdiCC);
	List<ClassificationClass> findByIdCC(String idCC);
	List<ClassificationClass> findByIdentifier(String identifier);
	List<ClassificationClass> findByCodedNameLike(String codedName);
	List<ClassificationClass> findByLevelAndCodedNameLike(int level, String codedName);
}