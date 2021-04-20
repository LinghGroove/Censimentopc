package it.contrader.hardwaretracking.controller;

import com.itextpdf.text.DocumentException;
import it.contrader.hardwaretracking.DTO.LoanDTO;
import it.contrader.hardwaretracking.entity.HttpResponse;
import it.contrader.hardwaretracking.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping(value="/loan")
public class LoanController {
	
	@Autowired
	LoanService loanService;
	
	@PostMapping("/save")
	public ResponseEntity<LoanDTO> newLent(@RequestParam("firstName") String firstName,
										   @RequestParam("lastName") String lastName,
										   @RequestParam("email") String email,
										   @RequestParam("deliveryMode") String deliveryMode,
										   @RequestParam("itemId") String itemId) throws MessagingException {
		
		return new ResponseEntity<>(loanService.lent(firstName, lastName, email, deliveryMode, itemId), HttpStatus.OK);
	}
	
	@GetMapping("/track")
	public ResponseEntity<List<LoanDTO>> trackItems(){
		return new ResponseEntity<>(loanService.trackItems(), HttpStatus.OK);
	}
	
	@PostMapping("/downloadAndReturn")
	public ResponseEntity<HttpResponse> downloadAndReturn(@RequestParam("nome") String nome,
														  @RequestParam("cognome") String cognome,
														  @RequestParam("cittaDiNascita") String cittaDiNascita,
														  @RequestParam("dataDiNascita") String dataDiNascita,
														  @RequestParam("citta") String citta,
														  @RequestParam("numero") String numero,
														  @RequestParam("via") String via,
														  @RequestParam("modello") String modello,
														  @RequestParam("seriale") String seriale,
														  @RequestParam("loanId") String loanId,
														  @RequestParam("itemId") String itemId) 
			throws FileNotFoundException, MalformedURLException, DocumentException, IOException, URISyntaxException{
		
		
		loanService.downloadAndReturn(nome, cognome, cittaDiNascita, dataDiNascita, citta, via, numero, modello, seriale, loanId, itemId);
		return response(HttpStatus.OK, "Operation executed");
	}
	
	@GetMapping("/generalHistory")
	public ResponseEntity<List<LoanDTO>> generalHistory(){
		return new ResponseEntity<>(loanService.generalHistory(), HttpStatus.OK);
	}
	
	@GetMapping("/downloadHistory")
	public ResponseEntity<HttpResponse> downloadHistory() throws DocumentException, URISyntaxException, MalformedURLException, IOException{
		loanService.downloadHistory();
		return response(HttpStatus.OK, "File downloaded. Check your download directory");
	}
	
	@GetMapping("/downloadItemHistory/{id}")
	public ResponseEntity<HttpResponse> downloadItemHistory(@PathVariable("id") long id) throws DocumentException, URISyntaxException, MalformedURLException, IOException{
		loanService.downloadItemHistory(id);
		return response(HttpStatus.OK, "File downloaded. Check your download directory");
	}

	private ResponseEntity<HttpResponse> response(HttpStatus status, String message) {
		return new ResponseEntity<>(new HttpResponse(status.value(), status, 
				status.getReasonPhrase().toUpperCase(), message.toUpperCase()), status);
	}

}
