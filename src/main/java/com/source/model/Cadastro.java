package com.source.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * Modelo para objecto Acesso que é utilizado para representar dados na TableView CRegistro e este modelo esta mapeado com a tabela
 * do banco de dados CADASTRO*/

@Entity(name = "CADASTRO")
public class Cadastro implements Serializable{
	private static final long serialVersionUID = 1L
			;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private int id;
	@Column(name = "UNIDADE", columnDefinition = "VARCHAR(150)")
	private String unidade;
	@Column(name = "PROD_ATUAL", columnDefinition = "DECIMAL(18,2)")
	private Double prodAtual;
	@Column(name = "N_EMPREGADOS")
	private Integer nEmpregados;
	@Column(name = "DESTINO_PROD", length = 30)
	private String destino;
	@Column(name = "NIVEL_AUTO")
	private Integer nivelAuto;
	@Column(name = "QUANT_MAQUINAS")
	private Integer qantiMaquinas;
	//Propriedade
	@Column(name = "CIDADE", length = 60)
	private String cidade;
	@Column(name = "CEP",length = 10)
	private String cep;
	@Column(name = "ENDERECO", length = 100)
	private String endereco;
	@Column(name = "ESTADO",columnDefinition = "CHAR(2)")
	private String estado;
	@Column(name = "PAIS",length = 30)
	private String pais;
	//Dados
	@Column(name = "INCE_FISCA_RECE",columnDefinition = "DECIMAL(18,2)")
	private Double inceFiscaRece;
	@Column(name = "IMP_MUNI_PAGO",columnDefinition = "DECIMAL(18,2)")
	private Double impMuniPagos;
	@Column(name = "IMP_ESTADU_RECOLHIDOS",columnDefinition = "DECIMAL(18,2)")
	private Double impEstaduRecolhidos;
	@Column(name = "IMP_FED_PAGO",columnDefinition = "DECIMAL(18,2)")
	private Double impFedPago;
	@Column(name = "TAXAS_FED",columnDefinition = "DECIMAL(18,2)")
	private Double taxasFed;
	//Agrotoxico
	@OneToMany(mappedBy = "cadastro")
	private List<Agrotoxico> agrotoxicos;
	
	public Cadastro() {
		
	}

	public Cadastro(int id,String unidade ,Double prodAtual, Integer nEmpregados, String destino, Integer nivelAuto,
			Integer qantiMaquinas, String cidade, String cep, String endereco, String estado, String pais,
			Double inceFiscaRece, Double impMuniPagos, Double impEstaduRecolhidos, Double impFedPago, Double taxasFed,
			List<Agrotoxico> agrotoxicos) {
		super();
		this.id = id;
		this.unidade = unidade;
		this.prodAtual = prodAtual;
		this.nEmpregados = nEmpregados;
		this.destino = destino;
		this.nivelAuto = nivelAuto;
		this.qantiMaquinas = qantiMaquinas;
		this.cidade = cidade;
		this.cep = cep;
		this.endereco = endereco;
		this.estado = estado;
		this.pais = pais;
		this.inceFiscaRece = inceFiscaRece;
		this.impMuniPagos = impMuniPagos;
		this.impEstaduRecolhidos = impEstaduRecolhidos;
		this.impFedPago = impFedPago;
		this.taxasFed = taxasFed;
		this.agrotoxicos = agrotoxicos;
	}

	public int getId() {
		return id;
	}

	public String getUnidade() {
		return unidade;
	}

	public void setUnidade(String unidade) {
		this.unidade = unidade;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Double getProdAtual() {
		return prodAtual;
	}

	public void setProdAtual(Double prodAtual) {
		this.prodAtual = prodAtual;
	}

	public Integer getnEmpregados() {
		return nEmpregados;
	}

	public void setnEmpregados(Integer nEmpregados) {
		this.nEmpregados = nEmpregados;
	}

	public String getDestino() {
		return destino;
	}

	public void setDestino(String destino) {
		this.destino = destino;
	}

	public Integer getNivelAuto() {
		return nivelAuto;
	}

	public void setNivelAuto(Integer nivelAuto) {
		this.nivelAuto = nivelAuto;
	}

	public Integer getQantiMaquinas() {
		return qantiMaquinas;
	}

	public void setQantiMaquinas(Integer qantiMaquinas) {
		this.qantiMaquinas = qantiMaquinas;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public Double getInceFiscaRece() {
		return inceFiscaRece;
	}

	public void setInceFiscaRece(Double inceFiscaRece) {
		this.inceFiscaRece = inceFiscaRece;
	}

	public Double getImpMuniPagos() {
		return impMuniPagos;
	}

	public void setImpMuniPagos(Double impMuniPagos) {
		this.impMuniPagos = impMuniPagos;
	}

	public Double getImpEstaduRecolhidos() {
		return impEstaduRecolhidos;
	}

	public void setImpEstaduRecolhidos(Double impEstaduRecolhidos) {
		this.impEstaduRecolhidos = impEstaduRecolhidos;
	}

	public Double getImpFedPago() {
		return impFedPago;
	}

	public void setImpFedPago(Double impFedPago) {
		this.impFedPago = impFedPago;
	}

	public Double getTaxasFed() {
		return taxasFed;
	}

	public void setTaxasFed(Double taxasFed) {
		this.taxasFed = taxasFed;
	}

	public List<Agrotoxico> getAgrotoxicos() {
		return agrotoxicos;
	}

	public void setAgrotoxicos(List<Agrotoxico> agrotoxicos) {
		this.agrotoxicos = agrotoxicos;
	}
	
	
	
	
}
