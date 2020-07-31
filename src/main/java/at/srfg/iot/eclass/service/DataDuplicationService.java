package at.srfg.iot.eclass.service;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import at.srfg.iot.classification.model.ConceptClass;
import at.srfg.iot.classification.model.Property;
import at.srfg.iot.classification.model.PropertyUnit;
import at.srfg.iot.eclass.model.ClassificationClass;
import at.srfg.iot.eclass.model.PropertyDefinition;
import at.srfg.iot.eclass.model.PropertyValue;
import at.srfg.iot.eclass.repository.ClassificationClassRepository;
import at.srfg.iot.eclass.repository.ConceptClassRepository;
import at.srfg.iot.eclass.repository.ConceptRepository;
import at.srfg.iot.eclass.repository.PropertyRepository;
import at.srfg.iot.eclass.repository.UnitRepository;
import at.srfg.iot.eclass.repository.ValueRepository;

@Service
public class DataDuplicationService {
	@Autowired
	private ClassificationClassRepository classficationClassRepo;
	@Autowired
	private PropertyRepository propertyRepo;
	@Autowired
	private ValueRepository valueRepo;
	@Autowired
	private UnitRepository unitRepo;
	@Autowired
	private ConceptRepository<Property> conceptPropertyRepo;
	@Autowired
	private ConceptRepository<PropertyUnit> conceptPropertyUnitRepo;
	@Autowired
	private ConceptRepository<at.srfg.iot.classification.model.PropertyValue> conceptPropertyValueRepo;
	@Autowired 
	private ConceptClassRepository conceptRepository;
	
	public int copyProperty() {
		int count = 0;
		Iterator<PropertyDefinition> valueIterator = propertyRepo.findAll().iterator();
		while (valueIterator.hasNext()) {
			PropertyDefinition eClass = valueIterator.next();
			// just copy the values
			copyProperty(eClass);
			count++;
		}
		return count;
	}
	public int copyPropertyUnit() {
		int i =0;
		Iterator<at.srfg.iot.eclass.model.PropertyUnit> valueIterator = unitRepo.findAll().iterator();
		while (valueIterator.hasNext()) {
			at.srfg.iot.eclass.model.PropertyUnit eClass = valueIterator.next();
			// just copy the values
			copyPropertyUnit(eClass);
			i++;
		}
		return i;
	}

	public int copyPropertyValues() {
		int i =0;
		Iterator<PropertyValue> valueIterator = valueRepo.findAll().iterator();
		while (valueIterator.hasNext()) {
			PropertyValue eClass = valueIterator.next();
			// just copy the values
			copyPropertyValue(eClass);
			i++;
		}
		return i;
	}
	private Property copyProperty(PropertyDefinition eClass) {
		Property prop = new Property(eClass.getIrdiPR());
		prop.setVersionDate(eClass.getVersionDate());
		prop.setRevisionNumber(eClass.getRevisionNumber());
		//
		prop.setCategory(eClass.getCategory());
		prop.setDataType(eClass.getDataType());
		prop.setDescription(eClass.getIsoLanguageCode(), eClass.getPreferredName(), eClass.getDefinition());
		prop.setNote(eClass.getNote());
		prop.setRemark(eClass.getRemark());
		prop.setShortName(eClass.getShortName());
		prop.setSourceOfDefinition(eClass.getSourceOfDefinition());
		// process the values
		for (PropertyValue v : eClass.getValues() ) {
			// process property assignment
			at.srfg.iot.classification.model.PropertyValue vt = copyPropertyValue(v);
			prop.addPropertyValue(vt);
			
		};
		conceptPropertyRepo.save(prop);
		return prop;
	}
	private PropertyUnit copyPropertyUnit(at.srfg.iot.eclass.model.PropertyUnit eClass) {
		PropertyUnit target = new PropertyUnit(eClass.getIrdiUN());
		//target.setVersionDate(eClass.getVersionDate());
		//target.setRevisionNumber(eClass.get);
		target.setDescription("en", eClass.getStructuredNaming(), eClass.getDefinition());
		target.setSiName(eClass.getSiName());
		target.setSiNotation(eClass.getSiNotation());
		target.setEceCode(eClass.getEceCode());
		target.setEceName(eClass.getEceName());
		target.setNistName(eClass.getNistName());
		target.setNameOfDedicatedQuantity(eClass.getNameOfDedicatedQuantity());
		target.setSource(eClass.getSource());
		target.setStructuredNaming(eClass.getStructuredNaming());
		target.setIecClassification(eClass.getIecClassification());
		conceptPropertyUnitRepo.save(target);
		return target;
	}
	private at.srfg.iot.classification.model.PropertyValue copyPropertyValue(PropertyValue source) {
		at.srfg.iot.classification.model.PropertyValue target = new at.srfg.iot.classification.model.PropertyValue(source.getIrdiVA());
		target.setVersionDate(source.getVersionDate());
		target.setRevisionNumber(source.getRevisionNumber());
		target.setDataType(source.getDataType());
		target.setReference(source.getReference());
		target.setShortName(source.getShortName());
		target.setDescription(source.getIsoLanguage(), source.getPreferredName(), source.getDefinition());
		conceptPropertyValueRepo.save(target);
		return target;
	}
	private void processPropertyValues(PropertyDefinition eClass, Property property) {
		
	}
	public int copyConceptClass(ConceptClass parent, String prefix) {
		prefix = prefix.replace("%", "");
		int level = prefix.length()/2;
		if (! prefix.endsWith("%")) {
			prefix = prefix+"%";
		}
		if ( level > 0 && parent == null) {
			Optional<ConceptClass> p = conceptRepository.findByLevelAndCodedNameLike(level, prefix).stream().findFirst();
			if ( p.isPresent()) {
				parent = p.get();
			}
			else {
				return 0;
			}
		}
		List<ClassificationClass> classes = classficationClassRepo.findByLevelAndCodedNameLike(level+1, prefix);
		int count = 0;
		for ( ClassificationClass eClass : classes ) {
			ConceptClass cClass = duplicateClass(parent, eClass);
			count++;
			//
			String extendedPrefix = cClass.getCodedName().substring(0, (level+1)*2);
			int subCount = copyConceptClass(cClass, extendedPrefix);
			count+=subCount;
		}
		return count;
		
	}
	private ConceptClass duplicateClass(ConceptClass parent, ClassificationClass eClass) {
		ConceptClass cClass = new ConceptClass(parent, eClass.getIrdiCC());
		cClass.setVersionDate(eClass.getVersionDate());
		cClass.setRevisionNumber(eClass.getRevisionNumber());
		cClass.setDescription(eClass.getIsoLanguageCode(), eClass.getPreferredName(), eClass.getDefinition());
		cClass.setNote(eClass.getNote());
		cClass.setRemark(eClass.getRemark());
		cClass.setShortName(eClass.getCodedName());
		cClass.setCodedName(eClass.getCodedName());
		cClass.setLevel(eClass.getLevel());
		conceptRepository.save(cClass);
		return cClass;
		
	}
	public void copyClassificationClass(String irdiCC) {
		Optional<ClassificationClass> cc = classficationClassRepo.findById(irdiCC);
		if ( cc.isPresent()) {
			ClassificationClass eClass = cc.get();
			
			
		}
	}
}
