package it.contrader.hardwaretracking.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import it.contrader.hardwaretracking.entity.Loan;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class ItemDTO {
	
	private long id;
	private String code;
	private String model;
	private String license;
	private boolean isLent;
	@JsonFormat(pattern="dd-MM-yyyy HH:mm:ss")
	private Date dateOfRecord;
	
	List<Loan> loans; 
	
	public ItemDTO() {
		this.loans = new ArrayList<>();
	} 
	
}
