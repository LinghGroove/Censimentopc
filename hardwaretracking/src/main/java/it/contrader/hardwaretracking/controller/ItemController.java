package it.contrader.hardwaretracking.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.contrader.hardwaretracking.DTO.ItemDTO;
import it.contrader.hardwaretracking.entity.HttpResponse;
import it.contrader.hardwaretracking.service.ItemService;

@RestController
@RequestMapping(value = "/item")
public class ItemController {
	
	@Autowired
	ItemService itemService;
	
	@PostMapping("/save")
	public ResponseEntity<List<ItemDTO>> save(@RequestParam("model") String model,
										@RequestParam("license") String license,
										@RequestParam("codes") String codes) {
		 
		return new ResponseEntity<>(itemService.save(model, license,  codes), HttpStatus.OK);
	}
	
	@GetMapping("/getAll")
	public ResponseEntity<List<ItemDTO>> getAll(){
		return new ResponseEntity<>(itemService.getAll(), HttpStatus.OK);
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<HttpResponse> delete(@PathVariable("id") long id){
		itemService.delete(id);
		return response(HttpStatus.OK, "Item deleted");
	}
	
	//----------------------------------------------------------------------------------
	
	private ResponseEntity<HttpResponse> response(HttpStatus status, String message) {
		
		return new ResponseEntity<>(new HttpResponse(status.value(), status, 
				status.getReasonPhrase().toUpperCase(), message.toUpperCase()), status);
	}
	
}
