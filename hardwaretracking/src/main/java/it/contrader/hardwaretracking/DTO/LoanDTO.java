package it.contrader.hardwaretracking.DTO;

	import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import it.contrader.hardwaretracking.entity.Item;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
	public class LoanDTO {
		
		private long id;
		private String firstName;
		private String lastName;
		private String email;
		@JsonFormat(pattern="dd-MM-yyyy HH:mm:ss")
		private Date deliveryDate;
		@JsonFormat(pattern="dd-MM-yyyy HH:mm:ss")
		private Date returnDate;
		private String deliveryMode;
		private String operationCode;
		
		private Item item;
		
}
