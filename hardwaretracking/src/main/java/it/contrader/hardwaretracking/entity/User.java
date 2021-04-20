package it.contrader.hardwaretracking.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class User implements Serializable{
	
	private static final long serialVersionUID = 7547786093632018695L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@NotNull
	@NotBlank
	@Size(min=2, message="Username must be two characters long at least!")
	private String username;
	@NotNull
	@NotBlank
	private String password;
	@NotNull
	@NotBlank
	@Size(min=2, message="First name must be two characters long at least!")
	private String firstName;
	@NotNull
	@NotBlank
	@Size(min=2, message="Last name must be two characters long at least!")
	private String lastName;
	@NotNull
	@NotBlank
	private String email;
	@NotNull
	@NotBlank
	private String fiscalCode;
	@JsonFormat(pattern="dd-MM-yyyy HH:mm:ss")
	private Date joinDate;
	@JsonFormat(pattern="dd-MM-yyyy HH:mm:ss")
	private Date lastLogin;
	private String[] authorities;
	private boolean isActive;
	private boolean isNotLocked;
	

}
