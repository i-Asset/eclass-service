package at.srfg.iot.eclass.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import at.srfg.iot.classification.model.ClassProperty;
import at.srfg.iot.classification.model.ClassPropertyPK;
import at.srfg.iot.classification.model.ClassPropertyValue;
import at.srfg.iot.classification.model.ClassPropertyValuePK;
import at.srfg.iot.classification.model.ConceptClass;
import at.srfg.iot.classification.model.Property;
import at.srfg.iot.classification.model.PropertyUnit;
import at.srfg.iot.classification.model.PropertyValue;
import at.srfg.iot.eclass.model.ClassificationClass;
import at.srfg.iot.eclass.model.ClassificationClassPropertyValue;
import at.srfg.iot.eclass.model.PropertyDefinition;
import at.srfg.iot.eclass.repository.ClassPropertyRepository;
import at.srfg.iot.eclass.repository.ClassPropertyValueRepository;
import at.srfg.iot.eclass.repository.ClassificationClassPropertyRepository;
import at.srfg.iot.eclass.repository.ClassificationClassPropertyValueRepository;
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
	private ClassificationClassPropertyRepository classificationClassPropertyRepo;
	@Autowired
	private ClassificationClassPropertyValueRepository classificationClassPropertyValueRepo;
	@Autowired
	private PropertyRepository propertyRepo;
	@Autowired
	private ValueRepository valueRepo;
	@Autowired
	private UnitRepository unitRepo;
	// 
	@Autowired
	private ConceptRepository<Property> conceptPropertyRepo;
	@Autowired
	private ConceptRepository<PropertyUnit> conceptPropertyUnitRepo;
	@Autowired
	private ConceptRepository<at.srfg.iot.classification.model.PropertyValue> conceptPropertyValueRepo;
	@Autowired 
	private ConceptClassRepository conceptRepository;
	
	
	@Autowired
	private ClassPropertyRepository classPropertyRepo;
	@Autowired
	private ClassPropertyValueRepository classPropertyValueRepo;
	
	
	public int copyProperty() {
		int count = 0;
		Iterator<PropertyDefinition> valueIterator = propertyRepo.findAll().iterator();
		while (valueIterator.hasNext()) {
			PropertyDefinition eClass = valueIterator.next();
			
			Property property = fromPropertyDefinition(eClass);
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
			duplicatePropertyUnit(eClass);
			i++;
		}
		return i;
	}

	public int copyPropertyValues() {
		int i =0;
		Iterator<at.srfg.iot.eclass.model.PropertyValue> valueIterator = valueRepo.findAll().iterator();
		while (valueIterator.hasNext()) {
			at.srfg.iot.eclass.model.PropertyValue eClass = valueIterator.next();
			// just copy the values
			duplicatePropertyValue(eClass);
			i++;
		}
		return i;
	}
	private Property duplicateProperty(PropertyDefinition eClass) {
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
		for (at.srfg.iot.eclass.model.PropertyValue v : eClass.getValues() ) {
			// process property assignment
			PropertyValue vt = fromPropertyValue(v);
			prop.addPropertyValue(vt);
			
		};
		if ( eClass.getUnit() != null ) {
			PropertyUnit unit = fromPropertyUnit(eClass.getUnit());
			prop.setUnit(unit);
		}
		conceptPropertyRepo.save(prop);
		return prop;
	}
	private PropertyUnit duplicatePropertyUnit(at.srfg.iot.eclass.model.PropertyUnit eClass) {
		PropertyUnit target = new PropertyUnit(eClass.getIrdiUN());
		//target.setVersionDate(eClass.getVersionDate());
		//target.setRevisionNumber(eClass.get);
		target.setDescription("en", eClass.getShortName(), eClass.getDefinition());
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
	private PropertyValue duplicatePropertyValue(at.srfg.iot.eclass.model.PropertyValue source) {
		PropertyValue target = new PropertyValue(source.getIrdiVA());
		target.setVersionDate(source.getVersionDate());
		target.setRevisionNumber(source.getRevisionNumber());
		target.setDataType(source.getDataType());
		target.setReference(source.getReference());
		target.setShortName(source.getShortName());
		target.setDescription(source.getIsoLanguage(), source.getPreferredName(), source.getDefinition());
		conceptPropertyValueRepo.save(target);
		return target;
	}
	/**
	 * Copy all concept classes
	 * @param parent
	 * @return
	 */
	public int copyConceptClass(final ConceptClass parent) {
		
		String prefix = (parent != null ? parent.getCodedName().substring(0, parent.getLevel()*2) : "");
		int level = prefix.length()/2;
		if (! prefix.endsWith("%")) {
			prefix = prefix+"%";
		}
		List<ClassificationClass> classes = classficationClassRepo.findByLevelAndCodedNameLike(level+1, prefix);
		int count = 0;
		for ( ClassificationClass eClass : classes ) {
			ConceptClass cClass = fromClassificationClass(parent, eClass);
			// process the property assignment at the proper level
			
			count++;
			//
			if ( eClass.isSubclassPresent()) {
				int subCount = copyConceptClass(cClass);
				count+=subCount;
			}
		}
		return count;
		
	}
	
	private ConceptClass fromClassificationClass(final ConceptClass parent, ClassificationClass eClass) {
		Optional<ConceptClass> opt = conceptRepository.findById(eClass.getIrdiCC());
		if ( opt.isPresent() ) {
			System.out.println(opt.get().getId());
		}
		return opt.orElseGet(new Supplier<ConceptClass>() {
			public ConceptClass get() {
				return duplicateClass(parent, eClass);
			}
		});
	}
	private PropertyUnit fromPropertyUnit(at.srfg.iot.eclass.model.PropertyUnit eClass) {
		Optional<PropertyUnit> opt = conceptPropertyUnitRepo.findById(eClass.getIrdiUN());
		return opt.orElseGet(new Supplier<PropertyUnit>() {
			public PropertyUnit get() {
				return duplicatePropertyUnit(eClass);
			}
		});
	}

	private PropertyValue fromPropertyValue(at.srfg.iot.eclass.model.PropertyValue eClass) {
		Optional<PropertyValue> opt = conceptPropertyValueRepo.findById(eClass.getIrdiVA());
		return opt.orElseGet(new Supplier<PropertyValue>() {
			public PropertyValue get() {
				return duplicatePropertyValue(eClass);
			}
		});
	}

	private Property fromPropertyDefinition(PropertyDefinition eClass) {
		Optional<Property> opt = conceptPropertyRepo.findById(eClass.getIrdiPR());
		if ( opt.isPresent() ) {
			System.out.println(opt.get().getId());
		}
		return opt.orElseGet(new Supplier<Property>() {
			public Property get() {
				return duplicateProperty(eClass);
			}
		});
	}
	private ClassProperty fromClassificationClassProperty(final ConceptClass cClass, final Property property) {
		Optional<ClassProperty> opt = classPropertyRepo.findById(new ClassPropertyPK(cClass.getId(), property.getId()));
		
		return opt.orElseGet(new Supplier<ClassProperty>() {
			public ClassProperty get() {
				ClassProperty classProperty = new ClassProperty(cClass, property);
				classPropertyRepo.save(classProperty);
				return classProperty;
			}
		});
	}
	private ClassPropertyValue fromClassificationClassPropertyValue(final ClassProperty context, final PropertyValue value) {
		Optional<ClassPropertyValue> opt = classPropertyValueRepo.findById(new ClassPropertyValuePK(context, value));
		
		return opt.orElseGet(new Supplier<ClassPropertyValue>() {
			public ClassPropertyValue get() {
				ClassPropertyValue classProperty = new ClassPropertyValue(context, value);
				classPropertyValueRepo.save(classProperty);
				return classProperty;
			}
		});
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

	public int copyClassPropertyAssignment(ConceptClass parent, Set<String> parentProcessed) {
		String prefix = (parent != null ? parent.getCodedName().substring(0, parent.getLevel()*2) : "");
		int level = prefix.length()/2;
		if (! prefix.endsWith("%")) {
			prefix = prefix+"%";
		}
		//
		int count = 0;
		level++;
		Set<ConceptClass> postProcess = new HashSet<ConceptClass>();
		List<ClassificationClass> classes = classficationClassRepo.findByLevelAndCodedNameLike(level, prefix);
		for ( ClassificationClass eClass : classes ) {
			Set<String> processed = new HashSet<String>(parentProcessed);
			ConceptClass conceptClass = fromClassificationClass(parent, eClass);
			String classCodedNamePrefix = eClass.getCodedName().substring(0, level*2) + "%";
			List<PropertyDefinition> properties = classificationClassPropertyRepo.findDistinctPropertyByClassCodedNameLike(classCodedNamePrefix);
			for ( PropertyDefinition eClassProperty: properties ) {
				if ( ! processed.contains(eClassProperty.getIrdiPR())) {
					long usageCount = classificationClassPropertyRepo.countByPropertyAndClassCodedNameLike(eClassProperty, classCodedNamePrefix);
					long subClassCount = classficationClassRepo.countBySubclassPresentAndCodedNameLike(false, classCodedNamePrefix);
					// 
					
					if ( usageCount == subClassCount) {
						// obtain the property
						Property conceptProperty = fromPropertyDefinition(eClassProperty);
						
						// 
						ClassProperty cp = fromClassificationClassProperty(conceptClass, conceptProperty);
//						List<ClassificationClassPropertyValue> cpvList = classificationClassPropertyValueRepo.findByPropertyDefinitionAndCodedName(eClassProperty, classCodedNamePrefix);
//						// 
//						List<ClassificationClassPropertyValue> ccvList = classificationClassPropertyValueRepo.findByClassificationClassAndProperty(eClass, eClassProperty);
//						if (! ccvList.isEmpty()) {
//							List<ClassPropertyValue> valueList = new ArrayList<ClassPropertyValue>();
//							for (ClassificationClassPropertyValue ccv : ccvList) {
//								PropertyValue pv = fromPropertyValue(ccv.getValue());
//								ClassPropertyValue cpv = fromClassificationClassPropertyValue(cp, pv);
//								valueList.add(cpv);
//								
//							}
//							// reset the value list
//							cp.setValues(valueList);
//							classPropertyRepo.save(cp);
//						}
						
						processed.add(eClassProperty.getIrdiPR());
					}
					else {
						// a class is not completely processed - add it to postProcess
						postProcess.add(conceptClass);
						// try it one level down 
					}
				}				
			}
			for ( ConceptClass cClass : postProcess) {
				copyClassPropertyAssignment(cClass, processed);
			}
		}
		
		return count;
	}
	private boolean copyValueList(ClassProperty classProperty) {
		// coded name ...
		// 
		
		return false;
	}
}
