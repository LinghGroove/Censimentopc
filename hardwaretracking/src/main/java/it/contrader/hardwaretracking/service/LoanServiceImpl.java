package it.contrader.hardwaretracking.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import it.contrader.hardwaretracking.DAO.ItemRepository;
import it.contrader.hardwaretracking.DAO.LoanRepository;
import it.contrader.hardwaretracking.DTO.ItemDTO;
import it.contrader.hardwaretracking.DTO.LoanDTO;
import it.contrader.hardwaretracking.converter.Converter;
import it.contrader.hardwaretracking.entity.Item;
import it.contrader.hardwaretracking.entity.Loan;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

@Service
@Transactional
public class LoanServiceImpl implements LoanService{
	
	@Autowired
	ItemRepository itemRepository;
	@Autowired
	LoanRepository loanRepository;
	@Autowired
	Converter<Item, ItemDTO> itemConverter;
	@Autowired
	Converter<Loan, LoanDTO> loanConverter;
	@Autowired
	EmailService emailService;
	
	SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
	String dateString = format.format(new Date());
	Path downloadPath = Paths.get(System.getProperty("user.home") + File.separator + "Downloads").toAbsolutePath().normalize();
	Path imagePath = Paths.get(ClassLoader.getSystemResource("logo.png").toURI());
	Image logo = Image.getInstance(imagePath.toAbsolutePath().toString());

	public LoanServiceImpl() throws URISyntaxException, IOException, BadElementException {
	}

	@Override
	public LoanDTO lent(String firstName, String lastName, String email, String deliveryMode, String id) throws MessagingException {
		
		long realId = Integer.parseInt(id);
		Item item = itemRepository.findById(realId).orElse(null);
		ItemDTO itemToLent = itemConverter.toDTO(item);
		String operationCode = RandomStringUtils.randomAlphanumeric(6);
		
		LoanDTO newLoan = new LoanDTO();
		newLoan.setFirstName(firstName);
		newLoan.setLastName(lastName);
		newLoan.setEmail(email);
		newLoan.setDeliveryMode(deliveryMode);
		newLoan.setDeliveryDate(new Date(System.currentTimeMillis() + 3600*1000));
		newLoan.setReturnDate(null);
		newLoan.setItem(item);
		newLoan.setOperationCode(operationCode);
		
		Loan entityLoan = loanConverter.toEntity(newLoan);
		loanRepository.save(entityLoan);
		itemToLent.setLent(true);
		emailService.confirmLoanEmail(firstName, operationCode, item.getModel(), email);
		itemRepository.save(itemConverter.toEntity(itemToLent)); 
		
		return newLoan;
	}

	@Override
	public List<LoanDTO> trackItems() {
		
		List<LoanDTO> list = new ArrayList<>();
		
		loanRepository.findAll().forEach(
				(temp) -> {
					if(temp.getReturnDate() == null)
						list.add(loanConverter.toDTO(temp));
				});
		
		return list;
	}

	@Override
	public void downloadAndReturn(String nome, String cognome, String cittaDiNascita, String dataDiNascita, String citta,
								   String via, String numero, String modello, String seriale, String loanId, String itemId) 
										   throws DocumentException, MalformedURLException, IOException, URISyntaxException {

		Document document = new Document();
		PdfWriter.getInstance(document, new FileOutputStream(downloadPath.toString() + File.separator + "modulo_restituzione" + nome + "_" + cognome + ".pdf"));
		
		document.open();
		Font titolo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK);
		
		Paragraph divisore = new Paragraph("______________________________________________________\n" +
											"RESTITUZIONE COMPUTER AZIENDALE", titolo);
		divisore.setAlignment(Element.ALIGN_CENTER);
		logo.setAlignment(Element.ALIGN_RIGHT);
		
		Font testo = FontFactory.getFont(FontFactory.HELVETICA, 14, BaseColor.BLACK);
		Paragraph testoPrincipale = new Paragraph("\nIl sottoscritto " + nome.toUpperCase() + " " + cognome.toUpperCase() +
						", nato a " + cittaDiNascita.toUpperCase() + " il " + dataDiNascita.toUpperCase() + " e residente in "
						+ citta.toUpperCase() + " alla " + via.toUpperCase() + ", n. " + numero + ", restituisce in data " + 
						dateString + " il PC aziendale avente le seguenti caratteristiche:\n" + 
						"\t- Marca e modello: " + modello.toUpperCase() + "\n\t- Numero di serie: " + 
						seriale.toUpperCase() + "\n\nBenevento, " + dateString, testo);
		testoPrincipale.setAlignment(Element.ALIGN_JUSTIFIED);
		
		Paragraph firma = new Paragraph("\n\n                                                                                                              Firma" + 
		 "\n\n                                                                              .......................................................................");

		Font carattereTitoloFooter = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.BLUE);
		Paragraph titoloFooter = new Paragraph("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\nContrader s.r.l", carattereTitoloFooter);
		
		Font carattereFooter = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8, BaseColor.BLACK);
		Paragraph divisoreBottom = new Paragraph("______________________________________________________", 
				titolo);
		Paragraph footer = new Paragraph(
							"\nSede Legale e Amministrativa: 82016 Montesarchio (BN), Via Costantino Grillo, 2\n" + 
							"Capitale sociale Euro 10.000\nC.F. 01541070627 P.I. 01541070627\n" + 
							"REA: BN 129013", carattereFooter);
		
		document.add(logo);
		document.add(divisore);
		document.add(testoPrincipale);
		document.add(firma);
		document.add(titoloFooter);
		document.add(divisoreBottom);
		document.add(footer);
		
		document.close();
		
		//-----------------------------------------------------------------------------
		
		Loan loan = loanRepository.findById((long) Integer.parseInt(loanId)).orElse(null);
		loan.setReturnDate(new Date());
		loanRepository.save(loan);
		
		Item returnItem = itemRepository.findById((long) Integer.parseInt(itemId)).orElse(null);
		returnItem.setLent(false);
		itemRepository.save(returnItem);
		
	}
	
	@SuppressWarnings("unused")
	@Override
	public void downloadHistory() throws DocumentException, IOException {
		
		Document document = new Document();
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(downloadPath.toString() + File.separator + "storico_generale.pdf"));
		document.setPageSize(PageSize.A4.rotate());
        
		document.open();

		Font titolo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK);
		
		Paragraph divisore = new Paragraph("____________________________________________________________________________________\n" +
											"STORICO\n\n", titolo);
		logo.setAlignment(Element.ALIGN_RIGHT);
		divisore.setAlignment(Element.ALIGN_CENTER);
		
		PdfPTable table = new PdfPTable(8);
		
		Stream.of("Serial Number", "Model", "Operation Code", "First Name",
				"Last Name", "Delivery Mode", "Delivery Date", "Return Date")
	      .forEach(columnTitle -> {
	        PdfPCell header = new PdfPCell();
	        header.setBackgroundColor(BaseColor.LIGHT_GRAY);
	        header.setBorderWidth(1);
	        header.setHorizontalAlignment(Element.ALIGN_CENTER);
	        header.setVerticalAlignment(Element.ALIGN_TOP);
	        header.setPhrase(new Phrase(columnTitle));
	        table.addCell(header);
	    });
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

		generalHistory().forEach(
				temp -> {
					table.addCell(temp.getItem().getCode());
					table.addCell(temp.getItem().getModel());
					table.addCell(temp.getOperationCode());
					table.addCell(temp.getFirstName());
					table.addCell(temp.getLastName());
					table.addCell(temp.getDeliveryMode());
					table.addCell(formatter.format(temp.getDeliveryDate()));
					table.addCell(formatter.format(temp.getReturnDate()));
				});
		
		document.add(logo);
		document.add(divisore);
		document.add(table);
		
		document.close();
	}
	
	@SuppressWarnings("unused")
	@Override
	public void downloadItemHistory(long id) throws DocumentException, URISyntaxException, MalformedURLException, IOException {
		
		Item item = itemRepository.findById(id).orElse(null);
		String nomePdf = "storico_generale_" + item.getModel() + "_" + item.getCode() + ".pdf";
		
		Document document = new Document();
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(downloadPath.toString() + File.separator + nomePdf));
		document.setPageSize(PageSize.A4.rotate());
        
		document.open();

		Font titolo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK);
		
		Paragraph divisore = new Paragraph("____________________________________________________________________________________\n" +
											"STORICO " + item.getModel().toUpperCase() + " - " + item.getCode() + "\n\n", titolo);
		logo.setAlignment(Element.ALIGN_RIGHT);
		divisore.setAlignment(Element.ALIGN_CENTER);
		
		PdfPTable table = new PdfPTable(6);
		
		Stream.of("Operation Code", "First Name", "Last Name", "Delivery Mode", "Delivery Date", "Return Date")
	      .forEach(columnTitle -> {
	        PdfPCell header = new PdfPCell();
	        header.setBackgroundColor(BaseColor.LIGHT_GRAY);
	        header.setBorderWidth(1);
	        header.setHorizontalAlignment(Element.ALIGN_CENTER);
	        header.setVerticalAlignment(Element.ALIGN_TOP);
	        header.setPhrase(new Phrase(columnTitle));
	        table.addCell(header);
	    });
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

		itemHistory(id).forEach(
				temp -> {
					table.addCell(temp.getOperationCode());
					table.addCell(temp.getFirstName());
					table.addCell(temp.getLastName());
					table.addCell(temp.getDeliveryMode());
					table.addCell(formatter.format(temp.getDeliveryDate()));
					table.addCell(formatter.format(temp.getReturnDate()));
				});
		
		document.add(logo);
		document.add(divisore);
		document.add(table);
		
		document.close();
	}
	
	@Override
	public List<LoanDTO> itemHistory(long id){
		List<Loan> loans = itemRepository.findById(id).orElse(null).getLoans();
		return loanConverter.toDTOlist(loans);
	}
	
	
	@Override
	public List<LoanDTO> generalHistory(){

		List<LoanDTO> history = new ArrayList<>();
		
		loanRepository.findAll().forEach(
				(temp) -> {
					if(temp.getReturnDate() != null)
						history.add(loanConverter.toDTO(temp));
				});
		
		return history;
		
	}
	

}
