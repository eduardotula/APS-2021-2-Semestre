package com.source.model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Transient;

import org.bytedeco.opencv.opencv_core.Mat;

/**
 * Modelo para objecto Acesso que é utilizado para representar dados na TableView CRegistro e este modelo esta mapeado com a tabela
 * do banco de dados ACESSO*/
@Entity(name = "ACESSO")
public class Acesso implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Integer id;
	@Column(name = "NOME", length = 100)
	private String nome;
	@Column(name = "NIVEL")
	private int nivel;
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "IMAGEM")
	private byte[] imagemByte;
	@Column(name = "ROWS")
	private int rows;
	@Column(name = "COLUMNS")
	private int col;
	@Column(name = "TYPE")
	private int type;
	@Transient
	private transient Mat imagemMat = new Mat();

	public Acesso() {

	}

	public Acesso(Integer id, String nome, int nivel, byte[] imagemByte, int rows, int col, int type, Mat imagemMat) {
		super();
		this.id = id;
		this.nome = nome;
		this.nivel = nivel;
		this.imagemByte = imagemByte;
		this.rows = rows;
		this.col = col;
		this.type = type;
		if (imagemMat != null) {
			this.imagemMat = imagemMat.clone();
		}

	}
	

	public Acesso(Integer id, String nome, int nivel) {
		super();
		this.id = id;
		this.nome = nome;
		this.nivel = nivel;
	}

	public void cloneImagem(Mat img) {
		this.imagemMat = img.clone();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getNivel() {
		return nivel;
	}

	public void setNivel(int nivel) {
		this.nivel = nivel;
	}

	public byte[] getImagemByte() {
		return imagemByte;
	}

	/**
	 * Obtem um array de bytes contendo todos os pixels contidos na imagem, para a
	 * imagem ser reconstruida é necessario armazenar a quantidade rows, columns e
	 * tipo da imagem
	 * 
	 * @return byte[] contendo a imagem menos header
	 */
	public byte[] getImagemAsByteArr() {
		// Aloca um array de bytes de acordo com o tamanho da imagem
		byte[] bytes = new byte[((int) (imagemMat.total() * imagemMat.elemSize()))];
		this.imagemMat.data().get(bytes);
		return bytes;
	}

	public void setImagemByte(byte[] imagemByte) {
		this.imagemByte = imagemByte;
	}

	public int getRows() {
		return rows;
	}

	/**
	 * Resconstroi a imagem a partir de um array de bytes
	 * 
	 * @param rows quantidade de linhas contida na imagem original
	 * @param cols quantidade de colunas contida na imagem original
	 * @param type tipo da imagem original
	 * @param arr  array de bytes
	 */
	public void setImagemByByteArr(int rows, int cols, int type, byte[] arr) {
		imagemMat = new Mat(rows, cols, type);
		this.imagemMat.data().put(arr);
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Mat getImagemMat() {
		return imagemMat;
	}

	public void setImagemMat(Mat imagemMat) {
		this.imagemMat = imagemMat;
		if(!imagemMat.empty()) {
			this.rows = imagemMat.rows();
			this.col = imagemMat.cols();
			this.type = imagemMat.type();
		}
	}

}
