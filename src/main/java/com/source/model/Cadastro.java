package com.source.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

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
	@Column(name = "UNIDADE", columnDefinition = "VARCHAR(150)",nullable = false)
	private String unidade;
	@Column(name = "PROD_ANUAL", columnDefinition = "DECIMAL(18,2)",nullable = false)
	private Double prodAnual;
	@Column(name = "N_EMPREGADOS",nullable = false)
	private Integer nEmpregados;
	@Column(name = "DESTINO_PROD", length = 30,nullable = false)
	private String destino;
	@Column(name = "NIVEL_AUTO",nullable = false)
	private Integer nivelAuto;
	@Column(name = "QUANT_MAQUINAS",nullable = false)
	private Integer qantiMaquinas;
	//Propriedade
	@Column(name = "CIDADE", length = 60,nullable = false)
	private String cidade;
	@Column(name = "CEP",length = 10,nullable = false)
	private String cep;
	@Column(name = "ENDERECO", length = 100,nullable = false)
	private String endereco;
	@Column(name = "ESTADO",columnDefinition = "CHAR(2)",nullable = false)
	private String estado;
	@Column(name = "PAIS",length = 30,nullable = false)
	private String pais;
	//Dados
	@Column(name = "INCE_FISCA_RECE",columnDefinition = "DECIMAL(18,2)",nullable = false)
	private Double inceFiscaRece;
	@Column(name = "IMP_MUNI_PAGO",columnDefinition = "DECIMAL(18,2)",nullable = false)
	private Double impMuniPagos;
	@Column(name = "IMP_ESTADU_RECOLHIDOS",columnDefinition = "DECIMAL(18,2)",nullable = false)
	private Double impEstaduRecolhidos;
	@Column(name = "IMP_FED_PAGO",columnDefinition = "DECIMAL(18,2)",nullable = false)
	private Double impFedPago;
	@Column(name = "TAXAS_FED",columnDefinition = "DECIMAL(18,2)",nullable = false)
	private Double taxasFed;
	@Column(name = "CONTEM_PROIB", columnDefinition = "BOOLEAN")
	private Boolean contemProibido = false;
	//Agrotoxico
	@OneToMany(mappedBy = "cadastro",fetch = FetchType.EAGER)
	private List<Agrotoxico> agrotoxicos = new ArrayList<Agrotoxico>();
	
	public Cadastro() {
		
	}

	public Cadastro(int id,String unidade ,Double prodAnual, Integer nEmpregados, String destino, Integer nivelAuto,
			Integer qantiMaquinas, String cidade, String cep, String endereco, String estado, String pais,
			Double inceFiscaRece, Double impMuniPagos, Double impEstaduRecolhidos, Double impFedPago, Double taxasFed,
			List<Agrotoxico> agrotoxicos, Boolean contemPro) {
		super();
		this.id = id;
		this.unidade = unidade;
		this.prodAnual = prodAnual;
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
		this.contemProibido = contemPro;
	}


	public Cadastro(int id, String unidade, String destino, String cidade, String estado) {
		super();
		this.id = id;
		this.unidade = unidade;
		this.destino = destino;
		this.cidade = cidade;
		this.estado = estado;
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

	public Double getProdAnual() {
		return prodAnual;
	}
	public String getProdAnualStr() {
		try {
			return Double.toString(prodAnual);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void setProdAnual(Double prodAtual) {
		this.prodAnual = prodAtual;
	}

	public Integer getnEmpregados() {
		return nEmpregados;
	}
	
	public String getnEmpregadosStr() {
		try {
			return Integer.toString(nEmpregados);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
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
	public String getNivelAutoStr() {
		try {
			return Integer.toString(nivelAuto);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	

	public void setNivelAuto(Integer nivelAuto) {
		this.nivelAuto = nivelAuto;
	}

	public Integer getQantiMaquinas() {
		return qantiMaquinas;
	}
	public String getQantiMaquinasStr() {
		try {
			return Integer.toString(qantiMaquinas);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
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
	public String getInceFiscaReceStr() {
		try {
			return Double.toString(inceFiscaRece);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	public void setInceFiscaRece(Double inceFiscaRece) {
		this.inceFiscaRece = inceFiscaRece;
	}

	public Double getImpMuniPagos() {
		return impMuniPagos;
	}
	public String getImpMuniPagosStr() {
		try {
			return Double.toString(impMuniPagos);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void setImpMuniPagos(Double impMuniPagos) {
		this.impMuniPagos = impMuniPagos;
	}

	public Double getImpEstaduRecolhidos() {
		return impEstaduRecolhidos;
	}
	public String getImpEstaduRecolhidosStr() {
		try {
			return Double.toString(impEstaduRecolhidos);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void setImpEstaduRecolhidos(Double impEstaduRecolhidos) {
		this.impEstaduRecolhidos = impEstaduRecolhidos;
	}

	public Double getImpFedPago() {
		return impFedPago;
	}
	public String getImpFedPagoStr() {
		try {
			return Double.toString(impFedPago);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void setImpFedPago(Double impFedPago) {
		this.impFedPago = impFedPago;
	}

	public Double getTaxasFed() {
		return taxasFed;
	}
	
	public String getTaxasFedStr() {
		try {
			return Double.toString(taxasFed);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
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
	
	public void addAgrotoxico(Agrotoxico agro) {
		agrotoxicos.add(agro);
		agro.setCadastro(this);
	}
	public void removeAgrotoxico(Agrotoxico agro) {
		agrotoxicos.remove(agro);
		agro.setCadastro(null);
	}

	public void checkProibido() {
		if(agrotoxicos != null) {
			contemProibido = false;
			for(Agrotoxico agr : agrotoxicos) {
				if(agr.getProibido()) contemProibido = true;
			}
		}
	}
	public Boolean getContemProibido() {
		return contemProibido;
	}

	public void setContemProibido(Boolean contemProibido) {
		this.contemProibido = contemProibido;
	}
	
	
	
}
