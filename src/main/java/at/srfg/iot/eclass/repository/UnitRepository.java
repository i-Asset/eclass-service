package at.srfg.iot.eclass.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import at.srfg.iot.eclass.model.Unit;

public interface UnitRepository extends CrudRepository<Unit, String>{
	Optional<Unit> findByEceCode(String eceCode);
	List<Unit> findByEceName(String eceName);
}
