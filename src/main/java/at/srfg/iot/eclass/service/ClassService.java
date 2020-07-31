package at.srfg.iot.eclass.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import at.srfg.iot.eclass.model.ClassificationClass;
import at.srfg.iot.eclass.model.PropertyDefinition;
import at.srfg.iot.eclass.model.PropertyUnit;
import at.srfg.iot.eclass.model.PropertyValue;
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
	public Optional<PropertyDefinition> getProperty(String irdiPR) {
		return propertyRepo.findById(irdiPR);		
	}
	public Optional<PropertyValue> getValue(String irdiVA) {
		return valueRepo.findById(irdiVA);
	}
	public List<PropertyValue> getPropertyValues(String irdiCC, String irdiPR) {
		return valueRepo.getValues(irdiCC, irdiPR);
	}

	public Optional<PropertyUnit> getUnit(String irdiUN) {
		return unitRepo.findById(irdiUN);
	}
	public List<PropertyDefinition> getProperties(String irdiCC) {
		return propertyRepo.findByIrdiCC(irdiCC);
	}
	public void setClassification(ClassificationClass classificationClass) {
		classficationClassRepo.save(classificationClass);
	}
	public void setProperty(PropertyDefinition property) {
		propertyRepo.save(property);
		if ( property.getUnit() != null ) {
			
			setUnit(property.getUnit());
		}
	}
	public void setValue(PropertyValue value) {
		valueRepo.save(value);
	}
	public void setUnit(PropertyUnit unit) {
		unitRepo.save(unit);
	}
}
