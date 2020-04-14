package at.srfg.iot.eclass.web.controller;

import java.util.List;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import at.srfg.iot.eclass.service.ClassService;

@RestController
@Api(value = "Eclass Controller",
		description = "API to perform Eclass operations")
@Deprecated
public class EClassController {
	@Autowired
	private ClassService classService;


	@Produces(MediaType.APPLICATION_JSON)
	@GetMapping("/class")
	public ResponseEntity<?> getClass(@RequestParam("irdiCC") String irdi) {
		return ResponseEntity.of(classService.getClassification(irdi));
	}
	@Produces(MediaType.APPLICATION_JSON)
	@GetMapping("/property")
	public ResponseEntity<?> getProperty(@RequestParam("irdiPR") String irdi) {
		return ResponseEntity.of(classService.getProperty(irdi));
	}
	@Produces(MediaType.APPLICATION_JSON)
	@GetMapping("/properties")
	public ResponseEntity<?> getValues(@RequestParam("irdiCC") String irdi) {
		return ResponseEntity.ok(classService.getProperties(irdi));
	}
	@Produces(MediaType.APPLICATION_JSON)
	@GetMapping("/value")
	public ResponseEntity<?> getValue(@RequestParam("irdiVA") String irdi) {
		return ResponseEntity.of(classService.getValue(irdi));
	}

	@Produces(MediaType.APPLICATION_JSON)
	@GetMapping("/values")
	public ResponseEntity<List<?>> getPropertyValues(@RequestParam("irdiCC") String irdicc, @RequestParam("irdiPR") String irdipr) {
		return ResponseEntity.ok(classService.getPropertyValues(irdicc, irdipr));
	}
	@Produces(MediaType.APPLICATION_JSON)
	@GetMapping("/unit")
	public ResponseEntity<?> getUnit(@RequestParam("irdiUN") String irdi) {
		return ResponseEntity.of(classService.getUnit(irdi));
	}

}
