package it.contrader.hardwaretracking.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@Entity
@Table(name="items")
@AllArgsConstructor
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class Item {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	private String code;
	private String model;
	private String license;
	private boolean isLent;
	@JsonFormat(pattern="dd-MM-yyyy HH:mm:ss")
	private Date dateOfRecord;
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="item", cascade= {CascadeType.PERSIST, CascadeType.MERGE, 
			CascadeType.DETACH, CascadeType.REFRESH})
	@JsonBackReference
	List<Loan> loans;

	public Item() {
		this.loans = new ArrayList<>();
	} 
	
	
	
}
