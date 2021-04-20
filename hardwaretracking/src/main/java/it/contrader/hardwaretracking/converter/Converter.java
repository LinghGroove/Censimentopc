package it.contrader.hardwaretracking.converter;

import java.util.List;

public interface Converter<Entity, DTO> {
	
	Entity toEntity(DTO dto);
	DTO toDTO(Entity entity);
	List<DTO> toDTOlist(List<Entity> entityList);

}
