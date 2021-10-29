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
	@Column(name = "AGROTOXICO",columnDefinition = "VARCHAR(150)")
	private String agrotoxico;
	
	
	@ManyToOne
	@JoinColumn(name = "CADASTRO_ID")
	private Cadastro cadastro;
	
}
