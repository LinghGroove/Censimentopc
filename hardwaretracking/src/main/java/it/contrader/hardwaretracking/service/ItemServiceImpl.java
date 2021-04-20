package it.contrader.hardwaretracking.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.contrader.hardwaretracking.DAO.ItemRepository;
import it.contrader.hardwaretracking.DTO.ItemDTO;
import it.contrader.hardwaretracking.converter.Converter;
import it.contrader.hardwaretracking.entity.Item;

@Service
@Transactional
public class ItemServiceImpl implements ItemService{
	
	@Autowired
	Converter<Item, ItemDTO> itemConverter;
	@Autowired
	ItemRepository itemRepository;
	
	@Override
	public List<ItemDTO> save(String model, String license, String codes) {
			
		/* L'utente immette diversi codici di serie separati da una virgola;
		 * dunque separo ognuno dei codici con split() applicando inoltre trim()
		 * per eliminare probabili spazi bianchi.
		 * A questo punto creo un ciclo for pari alla lunghezza del vettore di stringhe
		 * ottenuto dallo split e per ogni iterazione creo un nuovo oggetto sia da salvare
		 * sul database che da aggiungere ad una lista che otterrò come risultato
		 * della funzione da allegare alla ResponseEntity sul controller
		 */
		
		List<ItemDTO> newItems = new ArrayList<>();
		
		String[] newCodes = codes.split(",");
		
		for(int i = 0; i < newCodes.length; i++) {
			ItemDTO newItem = new ItemDTO();
			newItem.setModel(model);
			newItem.setLicense(license);
			newItem.setLent(false);
			newItem.setDateOfRecord(new Date(System.currentTimeMillis() + 3600*1000));
			newItem.setCode(newCodes[i].trim());
			
			newItems.add(newItem);
			itemRepository.save(itemConverter.toEntity(newItem));
			
		}
		
		return newItems;

	} 

	@Override
	public List<ItemDTO> getAll() {
		return itemConverter.toDTOlist(itemRepository.findAll());
	}

	@Override
	public void delete(long id) {
		itemRepository.deleteById(id);
	}
	
	
	
	
}
