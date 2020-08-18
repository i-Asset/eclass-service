package at.srfg.iot.lookup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
//github.com/i-Asset/eclass-service.git
import org.springframework.web.bind.annotation.RestController;

import at.srfg.iot.lookup.dependency.SemanticIndexing;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(exclude = { 
		SecurityAutoConfiguration.class , 
		ManagementWebSecurityAutoConfiguration.class
		})
@EnableDiscoveryClient
@EnableSwagger2
@EntityScan({
		// holds the "updatable" model 
		"at.srfg.iot.classification.model",
		// holds the E-Class Entities for data duplication - eClass is readonly
		"at.srfg.iot.eclass.model"})
@ComponentScan({
		"at.srfg.iot.eclass.repository",
		"at.srfg.iot.eclass.service",
		"at.srfg.iot.lookup" })
@EnableJpaRepositories({
		"at.srfg.iot.eclass.repository",
		"at.srfg.iot.lookup.repository"
})
@EnableAsync
@EnableFeignClients(clients = {
		SemanticIndexing.class
})
@RestController
public class SemanticLookupApplication {

	public static void main(String[] args) {
		SpringApplication.run(SemanticLookupApplication.class, args);
	}

}
