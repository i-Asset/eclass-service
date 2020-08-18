package at.srfg.iot.lookup.service.indexing;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import at.srfg.indexing.model.common.ClassType;
import at.srfg.indexing.model.common.Concept;
import at.srfg.indexing.model.common.DynamicName;
import at.srfg.indexing.model.common.PropertyType;
import at.srfg.iot.classification.model.ConceptBase;
import at.srfg.iot.classification.model.ConceptClass;
import at.srfg.iot.classification.model.Description;
import at.srfg.iot.classification.model.Property;
import at.srfg.iot.lookup.dependency.SemanticIndexing;
import at.srfg.iot.lookup.repository.ConceptClassPropertyRepository;

@Component
public class ConceptIndexingHandler {
	@Autowired
	SemanticIndexing indexer;
	@Autowired
	ConceptClassPropertyRepository conceptClassPropertyRepository;

	/**
	 * Event handler, taking care of indexing the concept class Event
	 * @param event
	 */
	@EventListener @Async
	public void onConceptClassEvent(ConceptClassEvent event) {
		ConceptClass cc = event.getConcept();
		try {
			if ( event.isDelete() ) {
				indexer.deleteClassType(Collections.singletonList(cc.getConceptId()));
			}
			else {
				ClassType cType = asClassType(cc);
				indexer.setClassType(cType);
			}
		} catch (Exception e) {
			
		}
	}
	/**
	 * Event handler, taking care of indexing the concept class Event
	 * @param event
	 */
	@EventListener @Async
	public void onPropertyEvent(PropertyEvent event) {
		Property cc = event.getConcept();
		try {
			if ( event.isDelete() ) {
				indexer.deletePropertyType(Collections.singletonList(cc.getConceptId()));
			}
			else {
				PropertyType cType = asPropertyType(cc);
				indexer.setPropertyType(cType);
			}
		} catch (Exception e) {
			
		}
	}
	/**
	 * Helper method for mapping {@link ConceptClass } data to the indexed {@link ClassType}
	 * @param cc The {@link ConceptClass}
	 * @return The {@link ClassType} finally stored
	 */

	private ClassType asClassType(ConceptClass cc) {
		ClassType cType = new ClassType();
		cType.setUri(cc.getConceptId());
		cType.setNameSpace(cc.getNameSpace());
		cType.setLocalName(cc.getLocalName());
		cType.setCode(cc.getCodedName());
		// 
		cType.setParents(getParentIdentifier(cc.getParentElement(), false));
		cType.setAllParents(getParentIdentifier(cc.getParentElement(), true));
		cType.setChildren(getChildrenIdentifier(cc, false));
		cType.setAllChildren(getChildrenIdentifier(cc, true));
		cType.setLevel(cc.getLevel());
		// handle concept properties
		handleConceptProperties(cc, cType);
		
		// map the description
		handleConceptDescription(cc, cType);
		return cType;		
	}
	/**
	 * Map all description elements 
	 * @param base
	 * @param concept
	 */
	private void handleConceptDescription(ConceptBase base, Concept concept) {
		
		for (Description d : base.getDescription()) {
			concept.setLabel(d.getLanguage(), d.getPreferredName());
			if ( d.getDefinition()!= null ) {
				concept.addDescription(d.getLanguage(), d.getDefinition());
			}
		}
	}
	/**
	 * Map all properties of the {@link ConceptClass} and add them to the {@link ClassType}
	 * @param cc The {@link ConceptClass}
	 * @param cType {@link ClassType}
	 */
	private void handleConceptProperties(ConceptClass cc, ClassType cType) {
		Set<Property> properties = getProperties(cc);
		for (Property property : properties) {
			PropertyType pType = asPropertyType(property);
			cType.addProperty(pType);
		}
	}
	/**
	 * Helper method for mapping {@link Property} data to the indexed {@link PropertyType}
	 * @param property the {@link Property} to store in the index
	 * @return The {@link PropertyType} finally stored
	 */
	private PropertyType asPropertyType(Property property) {
		PropertyType pType = new PropertyType();
		pType.setUri(property.getConceptId());
		pType.setNameSpace(property.getNameSpace());
		pType.setLocalName(property.getLocalName());
		pType.setCode(property.getShortName());
		// add as item fieldNames: 
		// - the (preferred labels)
		// - the short name
		// - the localName
		for (String langString : property.getLanguages()) {
			pType.addItemFieldName(DynamicName.getDynamicFieldPart(property.getPreferredName(langString)));
		}
		// add 
		pType.addItemFieldName(DynamicName.getDynamicFieldPart(property.getShortName()));
		pType.addItemFieldName(DynamicName.getDynamicFieldPart(property.getLocalName()));
		// map the description
		handleConceptDescription(property, pType);
		//
		return pType;
	}
	/**
	 * Helper method to extract the {@link ConceptBase#getConceptId()} of the elements' children
	 * @param cc The {@link ConceptClass} 
	 * @param all <code>true</code> to include all children, <code>false</code> for the direct children only
	 * @return
	 */
	private Set<String> getChildrenIdentifier(ConceptClass cc, boolean all) {
		Set<String> children = new HashSet<>();
		for (ConceptClass child : cc.getChildElements()) {
			children.add(child.getConceptId());
			if ( all ) {
				children.addAll(getChildrenIdentifier(child, all));
			}
		}
		return children;
	}
	/**
	 * Helper method to extract the {@link ConceptBase#getConceptId()} of the elements' parents
	 * @param conceptClass The {@link ConceptClass}
	 * @param all <code>true</code> to include all parents, <code>false</code> for the direct parent only
	 * @return
	 */
	private Set<String> getParentIdentifier(ConceptClass conceptClass, boolean all) {
		Set<String> parent = new HashSet<>();
		if ( conceptClass != null) {
			// add the element's concept identifier
			parent.add(conceptClass.getConceptId());
			if ( all && conceptClass.getParentElement()!= null ) {
				parent.addAll(getParentIdentifier(conceptClass.getParentElement(),all));
			}
			
		}
		return parent;

	}
	/**
	 * Helper method to retrieve all properties (including the properties of parent elements)
	 * @param cc The {@link ConceptClass}
	 * @return
	 */
	private Set<Property> getProperties(ConceptClass cc) {
		Set<Property> properties = new HashSet<>();
		if ( cc.getParentElement() != null) {
			properties.addAll(getProperties(cc.getParentElement()));
		}
		properties.addAll(conceptClassPropertyRepository.getProperties(cc));
		return properties;

	}
}
