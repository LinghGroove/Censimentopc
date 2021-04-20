package it.contrader.hardwaretracking.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;

import com.itextpdf.text.DocumentException;

import it.contrader.hardwaretracking.DTO.LoanDTO;

import javax.mail.MessagingException;

public interface LoanService {
	
	LoanDTO lent(String firstName, String lastName, String email, String deliveryMode, String id) throws MessagingException;

	List<LoanDTO> trackItems();

	void downloadAndReturn(String nome, String cognome, String nascita, String dataDiNascita, String citta,
							String via, String numero, String modello, String seriale, String loanId, String itemId) 
					   throws FileNotFoundException, DocumentException, MalformedURLException, IOException, URISyntaxException;
	
	List<LoanDTO> generalHistory();
	
	void downloadHistory() throws FileNotFoundException, DocumentException, URISyntaxException, MalformedURLException, IOException;
	
	List<LoanDTO> itemHistory(long id);
	
	void downloadItemHistory(long id) throws DocumentException, URISyntaxException, MalformedURLException, IOException;
}
