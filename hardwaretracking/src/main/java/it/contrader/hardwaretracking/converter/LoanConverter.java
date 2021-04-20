package it.contrader.hardwaretracking.converter;

import it.contrader.hardwaretracking.DTO.LoanDTO;
import it.contrader.hardwaretracking.entity.Loan;

import java.util.List;
import java.util.stream.Collectors;

public class LoanConverter implements Converter<Loan, LoanDTO>{

	@Override
	public Loan toEntity(LoanDTO dto) {
		
		Loan loan = new Loan();
		
		loan.setId(dto.getId());
		loan.setDeliveryDate(dto.getDeliveryDate());
		loan.setDeliveryMode(dto.getDeliveryMode());
		loan.setEmail(dto.getEmail());
		loan.setFirstName(dto.getFirstName());
		loan.setLastName(dto.getLastName());
		loan.setItem(dto.getItem());
		loan.setOperationCode(dto.getOperationCode());
		
		return loan;
	}

	@Override
	public LoanDTO toDTO(Loan entity) {
		
		LoanDTO loanDTO = new LoanDTO();
		
		loanDTO.setId(entity.getId());
		loanDTO.setDeliveryDate(entity.getDeliveryDate());
		loanDTO.setDeliveryMode(entity.getDeliveryMode());
		loanDTO.setEmail(entity.getEmail());
		loanDTO.setFirstName(entity.getFirstName());
		loanDTO.setLastName(entity.getLastName());
		loanDTO.setItem(entity.getItem());
		loanDTO.setReturnDate(entity.getReturnDate());
		loanDTO.setOperationCode(entity.getOperationCode());
		
		return loanDTO;
	}

	@Override
	public List<LoanDTO> toDTOlist(List<Loan> entityList) {
		//return entityList.stream().map(loan -> toDTO(loan)).collect(Collectors.toList());
		return entityList.stream().map(this::toDTO).collect(Collectors.toList());
	}

}
