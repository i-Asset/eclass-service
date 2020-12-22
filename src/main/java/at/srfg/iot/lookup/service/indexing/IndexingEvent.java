package at.srfg.iot.lookup.service.indexing;

import at.srfg.iot.common.datamodel.semanticlookup.model.ConceptBase;

public class IndexingEvent<T extends ConceptBase> {
	/**
	 * The concept synchronize with the index
	 */
	private final T concept;
	/**
	 * delete the concept from the index
	 */
	private final boolean delete;
	public IndexingEvent(T concept, boolean delete) {
		this.concept=concept;
		this.delete=delete;
	}
	public IndexingEvent(T concept) {
		this(concept, false);
	}
	public T getConcept() {
		return concept;
	}
	public boolean isDelete() {
		return delete;
	}

}
