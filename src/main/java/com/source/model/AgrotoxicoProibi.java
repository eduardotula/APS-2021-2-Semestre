package com.source.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "AGROTOXICO_PROIBI")
public class AgrotoxicoProibi implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Integer id;
	@Column(name = "AGROTOXICO",columnDefinition = "VARCHAR(150)", unique = true, nullable = false)
	private String agrotoxico;
	
	

	
	public AgrotoxicoProibi() {
		
	}

	public AgrotoxicoProibi(Integer id, String agrotoxico) {
		super();
		this.id = id;
		this.agrotoxico = agrotoxico;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAgrotoxico() {
		return agrotoxico;
	}

	public void setAgrotoxico(String agrotoxico) {
		this.agrotoxico = agrotoxico;
	}




}
	