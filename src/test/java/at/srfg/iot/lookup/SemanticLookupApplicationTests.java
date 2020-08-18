package at.srfg.iot.lookup;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import at.srfg.iot.lookup.dependency.SemanticIndexing;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SemanticLookupApplicationTests {
	@Autowired
	private SemanticIndexing indexer;

	
	@Test
	public void contextLoads() {
	}

}
