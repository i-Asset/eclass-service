package at.srfg.iot.lookup.service.indexing;

import at.srfg.iot.classification.model.Property;

public class PropertyEvent extends IndexingEvent<Property> {

	public PropertyEvent(Property source) {
		super(source);
	}
	public PropertyEvent(Property source, boolean delete) {
		super(source, delete);
	}

}
