package com.source.model;

public class PropriedadesModel {

	
	private Integer id;
	private String razao;
	private String estado;
	private String nivel;
	private String ramo;
	
	public PropriedadesModel() {
		
	}

	public PropriedadesModel(Integer id, String razao, String estado, String nivel, String ramo) {
		super();
		this.id = id;
		this.razao = razao;
		this.estado = estado;
		this.nivel = nivel;
		this.ramo = ramo;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRazao() {
		return razao;
	}

	public void setRazao(String razao) {
		this.razao = razao;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getNivel() {
		return nivel;
	}

	public void setNivel(String nivel) {
		this.nivel = nivel;
	}

	public String getRamo() {
		return ramo;
	}

	public void setRamo(String ramo) {
		this.ramo = ramo;
	}
	
	
}
