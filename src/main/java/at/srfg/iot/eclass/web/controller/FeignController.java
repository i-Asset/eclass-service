package at.srfg.iot.eclass.web.controller;

import java.util.List;
import java.util.Optional;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import at.srfg.iot.eclass.api.EClassService;
import at.srfg.iot.eclass.model.ClassificationClass;
import at.srfg.iot.eclass.model.PropertyDefinition;
import at.srfg.iot.eclass.model.PropertyUnit;
import at.srfg.iot.eclass.model.PropertyValue;
import at.srfg.iot.eclass.service.ClassService;

@RestController
public class FeignController implements EClassService {
	@Autowired
	private ClassService classService;


	@Produces(MediaType.APPLICATION_JSON)
	@GetMapping("/feign/class")
	public Optional<ClassificationClass> getClass(@RequestParam("irdiCC") String irdi) {
		return classService.getClassification(irdi);//.orElse(null);
	}
	@Produces(MediaType.APPLICATION_JSON)
	@GetMapping("/feign/property")
	public Optional<PropertyDefinition> getProperty(@RequestParam("irdiPR") String irdi) {
		return classService.getProperty(irdi);//.orElse(null);
	}
	@Produces(MediaType.APPLICATION_JSON)
	@GetMapping("/feign/properties")
	public List<PropertyDefinition> getValues(@RequestParam("irdiCC") String irdi) {
		return classService.getProperties(irdi);
	}
	@Produces(MediaType.APPLICATION_JSON)
	@GetMapping("/feign/value")
	public Optional<PropertyValue> getValue(@RequestParam("irdiVA") String irdi) {
		return classService.getValue(irdi);//.orElse(null);
	}
	
	@Produces(MediaType.APPLICATION_JSON)
	@GetMapping("/feign/values")
	public List<PropertyValue> getPropertyValues(@RequestParam("irdiCC") String irdicc, @RequestParam("irdiPR") String irdipr) {
		return classService.getPropertyValues(irdicc, irdipr);
	}
	@Produces(MediaType.APPLICATION_JSON)
	@GetMapping("/feign/unit")
	public Optional<PropertyUnit> getUnit(@RequestParam("irdiUN") String irdi) {
		return classService.getUnit(irdi);//.orElse(null);
	}
	
}
