package it.contrader.hardwaretracking.service;

import it.contrader.hardwaretracking.DTO.ItemDTO;

import java.util.List;

public interface ItemService {
	
	List<ItemDTO> save(String model, String license, String codes);
	
	List<ItemDTO> getAll();
	
	void delete(long id);
}
