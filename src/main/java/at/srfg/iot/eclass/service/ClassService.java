package at.srfg.iot.eclass.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import at.srfg.iot.eclass.model.ClassificationClass;
import at.srfg.iot.eclass.model.Property;
import at.srfg.iot.eclass.model.Unit;
import at.srfg.iot.eclass.model.Value;
import at.srfg.iot.eclass.repository.ClassificationClassRepository;
import at.srfg.iot.eclass.repository.PropertyRepository;
import at.srfg.iot.eclass.repository.UnitRepository;
import at.srfg.iot.eclass.repository.ValueRepository;

@Service
public class ClassService {
	@Autowired
	private ClassificationClassRepository classficationClassRepo;
	@Autowired
	private PropertyRepository propertyRepo;
	@Autowired
	private ValueRepository valueRepo;
	@Autowired
	private UnitRepository unitRepo;
	
	public Optional<ClassificationClass> getClassification(String irdiCC) {
		return classficationClassRepo.findById(irdiCC);
	}
	public Optional<Property> getProperty(String irdiPR) {
		return propertyRepo.findById(irdiPR);		
	}
	public Optional<Value> getValue(String irdiVA) {
		return valueRepo.findById(irdiVA);
	}
	public List<Value> getPropertyValues(String irdiCC, String irdiPR) {
		return valueRepo.getValues(irdiCC, irdiPR);
	}

	public Optional<Unit> getUnit(String irdiUN) {
		return unitRepo.findById(irdiUN);
	}
	public List<Property> getProperties(String irdiCC) {
		return propertyRepo.findByIrdiCC(irdiCC);
	}
}
