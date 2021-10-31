package com.source.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Modelo para objecto Acesso que é utilizado para representar uma lista de agrotoxicos e este modelo esta mapeado com a tabela
 * do banco de dados AGROTOXICO*/

@Entity(name = "AGROTOXICO")
public class Agrotoxico implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Integer id;
	@Column(name = "AGROTOXICO",columnDefinition = "VARCHAR(150)",nullable = false)
	private String agrotoxico;
	@Column(name = "PROIBIDO",columnDefinition = "BOOLEAN",nullable = false)
	private Boolean proibido;
	
	
	@ManyToOne
	@JoinColumn(name = "CADASTRO_ID")
	private Cadastro cadastro;
	
	public Agrotoxico() {
		
	}

	

	public Agrotoxico(Integer id, String agrotoxico, Boolean proibido, Cadastro cadastro) {
		super();
		this.id = id;
		this.agrotoxico = agrotoxico;
		this.proibido = proibido;
		this.cadastro = cadastro;
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

	public Cadastro getCadastro() {
		return cadastro;
	}

	public void setCadastro(Cadastro cadastro) {
		this.cadastro = cadastro;
	}

	public Boolean getProibido() {
		return proibido;
	}

	public void setProibido(Boolean proibido) {
		this.proibido = proibido;
	}
	
	
}
