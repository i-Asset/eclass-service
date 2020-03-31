package at.srfg.iot.eclass;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(exclude = { 
		SecurityAutoConfiguration.class , 
		ManagementWebSecurityAutoConfiguration.class
		})
@EntityScan({
		// holds the E-Class Entities 
		"at.srfg.iot.eclass.model"})
@ComponentScan({
		"at.srfg.iot.eclass" })
@RestController
public class EClassApplication {

	public static void main(String[] args) {
		SpringApplication.run(EClassApplication.class, args);
	}

}
