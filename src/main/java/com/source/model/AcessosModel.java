package com.source.model;

public class AcessosModel {

	private Integer id;
	private String nome;
	private String nivel;
	
	public AcessosModel() {
		
	}

	public AcessosModel(Integer id, String nome, String nivel) {
		super();
		this.id = id;
		this.nome = nome;
		this.nivel = nivel;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNivel() {
		return nivel;
	}

	public void setNivel(String nivel) {
		this.nivel = nivel;
	}
	
	
}
