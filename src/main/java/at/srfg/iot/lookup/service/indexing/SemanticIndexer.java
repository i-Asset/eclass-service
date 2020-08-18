package at.srfg.iot.lookup.service.indexing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import at.srfg.iot.classification.model.ConceptClass;
import at.srfg.iot.classification.model.Property;
/**
 * Component triggering the indexation of modified data
 * with the indexing service.
 * @author dglachs
 *
 */
@Component
public class SemanticIndexer {
	@Autowired
	private ApplicationEventPublisher publisher;

	/**
	 * Asynchronously (re)index the {@link ConceptClass}
	 * @param conceptClass
	 */
	public void store(ConceptClass conceptClass) {
		publisher.publishEvent(new ConceptClassEvent(conceptClass));
	}
	/**
	 * Asynchronously (re)index the {@link Property}
	 * @param property
	 */
	public void store(Property property) {
		publisher.publishEvent(new PropertyEvent(property));
	}
	
	/**
	 * Asynchronously (re)index the {@link ConceptClass}
	 * @param conceptClass
	 */
	public void remove(ConceptClass conceptClass) {
		publisher.publishEvent(new ConceptClassEvent(conceptClass, true));
	}
	/**
	 * Asynchronously (re)index the {@link Property}
	 * @param property
	 */
	public void remove(Property property) {
		publisher.publishEvent(new PropertyEvent(property, true));
	}
	
}
