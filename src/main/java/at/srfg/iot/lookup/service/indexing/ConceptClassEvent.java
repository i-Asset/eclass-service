package at.srfg.iot.lookup.service.indexing;

import at.srfg.iot.classification.model.ConceptClass;

public class ConceptClassEvent extends IndexingEvent<ConceptClass> {

	public ConceptClassEvent(ConceptClass source) {
		super(source);
	}
	
	public ConceptClassEvent(ConceptClass source, boolean delete) {
		super(source, delete);
	}	
}
