package at.srfg.iot.lookup.service.onto.impl;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFParser;
import org.apache.jena.riot.system.ErrorHandlerFactory;
import org.apache.jena.vocabulary.DC;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.SKOS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import at.srfg.iot.classification.model.ConceptBase;
import at.srfg.iot.classification.model.ConceptClass;
import at.srfg.iot.classification.model.ConceptProperty;
import at.srfg.iot.classification.model.DataTypeEnum;
import at.srfg.iot.lookup.service.ConceptClassService;
import at.srfg.iot.lookup.service.PropertyService;
import at.srfg.iot.lookup.service.onto.OntologyService;


@Service
public class OntologyServiceImpl implements OntologyService {
	
	@Autowired
	PropertyService propertyService;
	@Autowired
	ConceptClassService conceptClassService;

	private static final Logger logger = LoggerFactory.getLogger(OntologyServiceImpl.class);


	@Override
	public boolean deleteNamespace(String namespace) {
		// @TODO: lookup.delete()
//		propRepo.deleteByNameSpace(namespace);
//		classRepository.deleteByNameSpace(namespace);
		return true;
	}
	
	@Override
	public void upload(String mimeType, String onto, List<String> nameSpaces) {
		//
		//List<String> nameSpaces = Arrays.asList(includedNamespaces);
	
		Lang l = Lang.RDFNULL;
		switch (mimeType) {
		case "application/rdf+xml":
			l = Lang.RDFXML;
			break;
		case "application/turtle":
			l = Lang.TURTLE;
			break;
		default:
		    // 
			return;
		}
		/*
		 * Create a Model with RDFS inferencing
		 */
		OntModel ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_RDFS_INF);
		try {
			//
			StringReader reader = new StringReader(onto);
			/*
			 * Read the input string into the Ontology Model
			 */
			RDFParser.create()
				.source(reader)
				.errorHandler(ErrorHandlerFactory.errorHandlerStrict)
				.lang(l)
				.base("http://www.salzburgresearch.at/hmdp/")
				.parse(ontModel);
			
			/*
			 * Keep a list of indexed properties, use this list for
			 * mapping with classes 
			 */
			List<ConceptProperty> indexedProp = new ArrayList<>();
			/*
			 * Process all ontology properties, index them and fill
			 * the list of indexedProp
			 */
			Iterator<OntProperty> properties = ontModel.listAllOntProperties();
			while ( properties.hasNext()) {
				OntProperty p = properties.next();
				// restrict import to namespace list provided
				if (nameSpaces.isEmpty() || nameSpaces.contains(p.getNameSpace())) {
					if ( !p.isOntLanguageTerm()) {
						//
						ConceptProperty prop = processProperty(ontModel, p);
						if ( prop != null) {
							// store the property
							indexedProp.add(prop);
						}
					}
				}
			}
			/*
			 * process all ontology classes, index them and map all
			 * properties applicable to the class 
			 */
			Iterator<OntClass> classes = ontModel.listClasses();
			while ( classes.hasNext()) {
				OntClass c = classes.next();
				// restrict import to namespace list provided
				if ( nameSpaces.isEmpty() || nameSpaces.contains(c.getNameSpace())) {
					
					if ( !c.isOntLanguageTerm()) {
						ConceptClass clazz = processConceptClass(ontModel, c, indexedProp);
						
//						if ( clazz != null) {
//							conceptClassService.setConcept(clazz);
//						}
					}
				}
			}
		} finally {
			ontModel.close();
		}

	}
	private ConceptClass processConceptClass(OntModel model, final OntClass clazz, List<ConceptProperty> availableProps) {
		if (! (clazz.isAnon() || clazz.isOntLanguageTerm())) {
			ConceptClass index = conceptClassService.getConcept(clazz.getURI())
					.orElseGet(new Supplier<ConceptClass>() {

						@Override
						public ConceptClass get() {
							// TODO Auto-generated method stub
							ConceptClass cc = new ConceptClass(clazz.getURI());
							if ( clazz.getSuperClass() != null ) {
								cc.setParentElement(processConceptClass(model, clazz.getSuperClass(), availableProps));
							}
							return cc;
						}
					});
			
			// process the labels
			processLabels(index, clazz);
			// use the local name as short name
			index.setShortName(clazz.getLocalName());
			conceptClassService.setConcept(index);
			return index;
		}
		// 
		return null;
	}
	private DataTypeEnum fromRange(OntResource range) {
		return DataTypeEnum.STRING;
	}

    private ConceptProperty processProperty(OntModel model, OntProperty prop) {
    	// find the existing property or create a new one
        ConceptProperty index = propertyService.getConcept(prop.getURI())
        		.orElse(new ConceptProperty(prop.getURI()));
        // process the labels
        processLabels(index, prop);
        //
        
        index.setDataType(fromRange(prop.getRange()));

        propertyService.setConcept(index);
        return index;
    }
    /**
     * helper method processing all the labels (preferred, alternate, hidden) including description & comments
     * @param concept
     * @param resource
     */
    private void processLabels(ConceptBase concept, OntResource resource) {
    	concept.setPreferredLabel(obtainMultilingualValues(resource, RDFS.label, DC.title, SKOS.prefLabel));
    	concept.setAlternateLabel(obtainMultilingualLabels(resource, SKOS.altLabel));
    	concept.setHiddenLabel(obtainMultilingualLabels(resource, SKOS.hiddenLabel));
    	concept.setDefinition(obtainMultilingualValues(resource, DC.description, SKOS.definition));
    	concept.setComment(obtainMultilingualValues(resource, RDFS.comment, SKOS.note));
    }
	/**
	 * Helper method to extract multilingual labels
	 * @param prop
	 * @param properties
	 * @return
	 */
	private Map<Locale, String> obtainMultilingualValues(OntResource prop, Property ... properties ) {
		Map<Locale,String> languageMap = new HashMap<>();
		for (Property property : properties) {
			NodeIterator nIter = prop.listPropertyValues(property);
			while ( nIter.hasNext()) {
				RDFNode node = nIter.next();
				if ( node.isLiteral()) {
					Locale lang = Locale.forLanguageTag(node.asLiteral().getLanguage());
					//String lang = node.asLiteral().getLanguage();
					
					if (! languageMap.containsKey(lang)) {
						languageMap.put(lang, node.asLiteral().getString());
					}
				}
			}
		}
		return languageMap;
		
	}

	/**
	 * Helper method to extract multilingual hidden and alternate labels
	 * @param prop
	 * @param properties
	 * @return
	 */
	private Map<Locale, Set<String>> obtainMultilingualLabels(OntResource prop, org.apache.jena.rdf.model.Property... properties) {

		Map<Locale, Set<String>> languageMap = new HashMap<Locale, Set<String>>();
		for (Property property : properties) {
			NodeIterator nIter = prop.listPropertyValues(property);
			while (nIter.hasNext()) {
				RDFNode node = nIter.next();
				if (node.isLiteral()) {
					Locale lang = Locale.forLanguageTag(node.asLiteral().getLanguage());
					if (languageMap.get(lang) != null) {
						Set<String> labelValues = languageMap.get(lang);
						labelValues.add(node.asLiteral().getString());
						languageMap.put(lang, labelValues);
					} else {
						Set<String> labelValues = new HashSet<String>();
						labelValues.add(node.asLiteral().getString());
						languageMap.put(lang, labelValues);
					}
				}
			}
		}
		return languageMap;

	}
	/**
	 * Extract all superclasses of a given class
	 * @param cls
	 * @return
	 */
	private Set<String> getSuperClasses(OntClass cls) {
		return getSuperClasses(cls, false);
	}
	private Set<String> getSuperClasses(OntClass cls, boolean direct) {
		Set<String> sup = new HashSet<>();
		Iterator<OntClass> iter = cls.listSuperClasses(direct);
		while (iter.hasNext()) {
			OntClass superClass = iter.next();
			if (! superClass.isAnon()) {
//				if (!superClass.getNameSpace().equals(RDFS.uri))
				// exclude rdfs, rdf, owl
				if (!superClass.isOntLanguageTerm()) {
					sup.add(superClass.getURI());
				}
			}
		}
		return sup;
	}

	private int getLevel(OntClass cls) {
		if ( cls.getSuperClass()!= null) {
			return 1 + getLevel(cls.getSuperClass());
		}
		return 0;
	}

	/**
	 * Helper method to identify all child classes
	 * @param cls
	 * @return
	 */
	private Set<String> getSubClasses(OntClass cls) {
		return getSubClasses(cls, false);
	}
	private Set<String> getSubClasses(OntClass cls, boolean direct) {
		Set<String> sub = new HashSet<>();
		Iterator<OntClass> iter = cls.listSubClasses(direct);
		while (iter.hasNext()) {
			OntClass subClass = iter.next();
			if (subClass.isURIResource()) {
				sub.add(subClass.getURI());
			}
		}
		return sub;
	}
}
