package it.contrader.hardwaretracking.converter;

import java.util.List;
import java.util.stream.Collectors;

import it.contrader.hardwaretracking.DTO.ItemDTO;
import it.contrader.hardwaretracking.entity.Item;

public class ItemConverter implements Converter<Item, ItemDTO> {

	@Override
	public Item toEntity(ItemDTO dto) {
		
		Item item = new Item();
		
		item.setId(dto.getId());
		item.setCode(dto.getCode());
		item.setLicense(dto.getLicense());
		item.setModel(dto.getModel());
		item.setLent(dto.isLent());
		item.setDateOfRecord(dto.getDateOfRecord());
		
		if(dto.getLoans() != null)
			item.setLoans(dto.getLoans());
		
		return item;
	}

	@Override
	public ItemDTO toDTO(Item entity) {
		
		ItemDTO itemDTO = new ItemDTO();
		
		itemDTO.setId(entity.getId());
		itemDTO.setCode(entity.getCode());
		itemDTO.setLicense(entity.getLicense());
		itemDTO.setModel(entity.getModel());
		itemDTO.setLent(entity.isLent());
		itemDTO.setDateOfRecord(entity.getDateOfRecord());
		
		if(entity.getLoans() != null)
			itemDTO.setLoans(entity.getLoans());
		
		return itemDTO;
	}

	@Override
	public List<ItemDTO> toDTOlist(List<Item> entityList) {
		
		return entityList.stream().map(item -> toDTO(item)).collect(Collectors.toList());
	}

}
