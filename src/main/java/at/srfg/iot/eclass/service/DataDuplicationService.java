package at.srfg.iot.eclass.service;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;

import at.srfg.iot.classification.model.ClassProperty;
import at.srfg.iot.classification.model.ClassPropertyPK;
import at.srfg.iot.classification.model.ConceptClass;
import at.srfg.iot.classification.model.DataTypeEnum;
import at.srfg.iot.classification.model.Property;
import at.srfg.iot.classification.model.PropertyUnit;
import at.srfg.iot.classification.model.PropertyValue;
import at.srfg.iot.eclass.model.ClassificationClass;
import at.srfg.iot.eclass.model.PropertyDefinition;
import at.srfg.iot.eclass.repository.ClassificationClassPropertyRepository;
import at.srfg.iot.eclass.repository.ClassificationClassPropertyValueRepository;
import at.srfg.iot.eclass.repository.ClassificationClassRepository;
import at.srfg.iot.eclass.repository.PropertyRepository;
import at.srfg.iot.eclass.repository.UnitRepository;
import at.srfg.iot.eclass.repository.ValueRepository;
import at.srfg.iot.lookup.repository.ConceptClassPropertyRepository;
import at.srfg.iot.lookup.repository.ConceptClassRepository;
import at.srfg.iot.lookup.repository.ConceptRepository;
import at.srfg.iot.lookup.service.indexing.SemanticIndexer;

@Service
public class DataDuplicationService {
	@Autowired
	private SemanticIndexer indexer;

	@Autowired
	private ClassificationClassRepository classificationClassRepository;
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
	private ConceptClassPropertyRepository classPropertyRepo;
	
	public Optional<ConceptClass> copyClassificationClass(String irdi) {
		Optional<ClassificationClass> ccOpt = classificationClassRepository.findById(irdi);
		if (ccOpt.isPresent()) {
			// obtain the parent for the classification class
			ConceptClass parent = getParent(ccOpt.get());
			// now copy the classification class with the parent
			ConceptClass cc = fromClassificationClass(parent, ccOpt.get());
			return Optional.of(cc);
		}
		return Optional.empty();
	}
	public Optional<Property> copyProperty(String irdi) {
		Optional<PropertyDefinition> ccOpt = propertyRepo.findById(irdi);
		if (ccOpt.isPresent()) {
			// obtain the parent for the classification class
			
			// now copy the classification class with the parent
			Property cc = fromPropertyDefinition(ccOpt.get());
			return Optional.of(cc);
		}
		return Optional.empty();
	}
	public Optional<PropertyUnit> copyUnit(String irdi) {
		Optional<at.srfg.iot.eclass.model.PropertyUnit> ccOpt = unitRepo.findById(irdi);
		if (ccOpt.isPresent()) {
			// obtain the parent for the classification class
			
			// now copy the classification class with the parent
			PropertyUnit cc = fromPropertyUnit(ccOpt.get());
			return Optional.of(cc);
		}
		return Optional.empty();
	}
	public Optional<PropertyValue> copyValue(String irdi) {
		Optional<at.srfg.iot.eclass.model.PropertyValue> ccOpt = valueRepo.findById(irdi);
		if (ccOpt.isPresent()) {
			// obtain the parent for the classification class
			
			// now copy the classification class with the parent
			PropertyValue cc = fromPropertyValue(ccOpt.get());
			return Optional.of(cc);
		}
		return Optional.empty();
	}
	/**
	 * suche cc for classification
	 * 
	 * @param classificationClass
	 * @return
	 */
	private ConceptClass getParent(ClassificationClass classificationClass) {
		
		int level = classificationClass.getLevel()-1;
		String codedName = Strings.padEnd(classificationClass.getCodedName().substring(0, level*2), 8, '0');
		Optional<ClassificationClass> parent = classificationClassRepository.findParent(level, codedName);
		if ( parent.isPresent() ) {
			Optional<ConceptClass> ccParent = conceptRepository.findByConceptId(parent.get().getIrdiCC());
			if ( ccParent.isPresent() ) {
				return ccParent.get();
			}
			else {
				ConceptClass cc = getParent(parent.get());
				return fromClassificationClass(cc, parent.get());
			}
		}
		else {
			return null;

		}
	}



	private Property duplicateProperty(PropertyDefinition eClass) {
		Property prop = new Property(eClass.getIrdiPR());
		prop.setVersionDate(eClass.getVersionDate());
		prop.setRevisionNumber(eClass.getRevisionNumber());
		
		prop.setCoded(eClass.getAttributeType()=="INDIRECT"?Boolean.TRUE: Boolean.FALSE);
		//
		prop.setCategory(eClass.getCategory());
		prop.setDataType(DataTypeEnum.fromString(eClass.getDataType()));
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
		// 
		duplicatePropertyValues(prop, eClass);
		//
		conceptPropertyRepo.save(prop);
		// store property in the index
		indexer.store(prop);

		return prop;
	}
	private void duplicatePropertyValues(Property property, PropertyDefinition eClass) {
		List<Object[]> values = classificationClassPropertyValueRepo.findValuesForProperty(eClass.getIrdiPR(), "FALSE");
		for (Object[] value : values) {
			String irdiVA = value[0].toString();
			long min = new Long(value[1].toString()).longValue();
			long max = new Long(value[2].toString()).longValue();
			if ( min == max) {
				at.srfg.iot.eclass.model.PropertyValue v = valueRepo.findByIrdiVA(irdiVA);
				PropertyValue pv = fromPropertyValue(v);
				property.addPropertyValue(pv);
			}
		}
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
		target.setDataType(DataTypeEnum.fromString(source.getDataType()));
		target.setReference(source.getReference());
		target.setShortName(source.getShortName());
		target.setValue(source.getPreferredName());
		target.setDescription(source.getIsoLanguage(), source.getPreferredName(), source.getDefinition());
		conceptPropertyValueRepo.save(target);
		return target;
	}

	private ConceptClass fromClassificationClass(final ConceptClass parent, ClassificationClass eClass) {
		Optional<ConceptClass> opt = conceptRepository.findByConceptId(eClass.getIrdiCC());
		return opt.orElseGet(new Supplier<ConceptClass>() {
			public ConceptClass get() {
				return duplicateClass(parent, eClass);
			}
		});
	}
	private PropertyUnit fromPropertyUnit(at.srfg.iot.eclass.model.PropertyUnit eClass) {
		Optional<PropertyUnit> opt = conceptPropertyUnitRepo.findByConceptId(eClass.getIrdiUN());
		return opt.orElseGet(new Supplier<PropertyUnit>() {
			public PropertyUnit get() {
				return duplicatePropertyUnit(eClass);
			}
		});
	}

	private PropertyValue fromPropertyValue(at.srfg.iot.eclass.model.PropertyValue eClass) {
		Optional<PropertyValue> opt = conceptPropertyValueRepo.findByConceptId(eClass.getIrdiVA());
		return opt.orElseGet(new Supplier<PropertyValue>() {
			public PropertyValue get() {
				return duplicatePropertyValue(eClass);
			}
		});
	}

	private Property fromPropertyDefinition(PropertyDefinition eClass) {
		Optional<Property> opt = conceptPropertyRepo.findByConceptId(eClass.getIrdiPR());
		return opt.orElseGet(new Supplier<Property>() {
			public Property get() {
				return duplicateProperty(eClass);
			}
		});
	}
	private ClassProperty fromClassificationClassProperty(final ConceptClass cClass, final Property property, long usageCount) {
		Optional<ClassProperty> opt = classPropertyRepo.findById(new ClassPropertyPK(cClass, property));
		
		return opt.orElseGet(new Supplier<ClassProperty>() {
			public ClassProperty get() {
				ClassProperty classProperty = new ClassProperty(cClass, property);
				
				List<Object[]> values = classificationClassPropertyValueRepo.findValuesForPropertyAndCodedName(property.getConceptId(), cClass.getCodedName(), "TRUE", usageCount);
				for (Object[] pwc : values) {
					String irdiVA = pwc[0].toString();
					at.srfg.iot.eclass.model.PropertyValue value = valueRepo.findByIrdiVA(irdiVA);
					PropertyValue conceptValue = fromPropertyValue(value);
					classProperty.addPropertyValue(conceptValue);
					classProperty.setValueConstraint(true);
				}
				classPropertyRepo.save(classProperty);
				
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
		// search for property assignements
		duplicateClassProperties(cClass);
		//
		indexer.store(cClass);
		return cClass;
		
	}
	private void duplicateClassProperties(ConceptClass conceptClass) {
		int level = conceptClass.getLevel();
		String classCodedNamePrefix = conceptClass.getCodedName().substring(0, level*2) + "%";
		long subClassCount = classificationClassRepository.countBySubclassPresentAndCodedNameLike(false, classCodedNamePrefix);
		// check each property whether it is applied to all subclasses
		// find all property assignments
		List<Object[]> propertiesWithCount = classificationClassPropertyRepo.findPropertyWithUsageCount(classCodedNamePrefix, subClassCount);
		for (Object[] pwc : propertiesWithCount) {
			String irdiPR = pwc[0].toString();
			PropertyDefinition eClassProperty = propertyRepo.findByIrdiPR(irdiPR);
			Property conceptProperty = fromPropertyDefinition(eClassProperty);
			// 
			fromClassificationClassProperty(conceptClass, conceptProperty, subClassCount);
			
		}

	}
}
