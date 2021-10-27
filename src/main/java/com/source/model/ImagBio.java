package com.source.model;

import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Rect;
import org.bytedeco.opencv.opencv_core.RectVector;

public class ImagBio {

	private Integer idLabel;
	private String descri;
	private Mat imagem;
	private boolean proces = false;
	private RectVector rostos;
	private Rect rostoPrinc;
	
	
	public ImagBio() {
		imagem = new Mat();
		proces = false;
		rostos = new RectVector();
		rostoPrinc = new Rect();
	}

	public ImagBio(Integer idModel, String descri ,Mat imagem, boolean proces, RectVector rostos, Rect rect) {
		super();
		this.idLabel = idModel;
		this.descri = descri;
		if(imagem instanceof Mat) this.imagem = imagem.clone(); else this.imagem = new Mat();
		this.proces = proces;
		if(rostos instanceof RectVector) this.rostos = cloneRectVector(rostos); else this.rostos = new RectVector();
		if(rect instanceof Rect) this.rostoPrinc = new Rect(rect.x(),rect.y(),rect.width(),rect.height());
	}

	public Integer getIdLabel() {
		return idLabel;
	}

	public String getDescri() {
		return descri;
	}

	public void setDescri(String descri) {
		this.descri = descri;
	}

	
	public void setIdLabel(Integer idModel) {
		this.idLabel = idModel;
	}

	public Mat getImagem() {
		return imagem;
	}

	public void setImagem(Mat imagem) {
		this.imagem = imagem;
	}
	/**
	 * Resconstroi a imagem a partir de um array de bytes
	 * @param rows quantidade de linhas contida na imagem original
	 * @param cols quantidade de colunas contida na imagem original
	 * @param type tipo da imagem original
	 * @param arr array de bytes*/
	public void setImagemByByteArr(int rows, int cols, int type,byte[] arr) {
		imagem = new Mat(rows,cols,type);
		this.imagem.data().put(arr);
	}
	
	public void cloneImagem(Mat imagem) {
		if(imagem instanceof Mat) this.imagem = imagem.clone(); else this.imagem = new Mat();
	}
	
	/**
	 * Obtem um array de bytes contendo todos os pixels contidos na imagem, para a imagem ser reconstruida é necessario
	 * armazenar a quantidade rows, columns e tipo da imagem
	 * @return byte[] contendo a imagem menos header */
	public byte[] getImagemAsByteArr() {
		//Aloca um array de bytes de acordo com o tamanho da imagem
		byte[] bytes = new byte[((int) (imagem.total() * imagem.elemSize()) ) ];
		this.imagem.data().get(bytes);
		return bytes;
	}
	
	public boolean isProces() {
		return proces;
	}

	public void setProces(boolean proces) {
		this.proces = proces;
	}

	public RectVector getRostos() {
		return rostos;
	}

	public void setRostos(RectVector rostos) {
		this.rostos = rostos;
	}
	
	public void cloneRostos(RectVector rostos) {
		if(rostos instanceof RectVector) this.rostos = cloneRectVector(rostos); else this.rostos = new RectVector();
	}
	
	public Rect getRostoPrinc() {
		return rostoPrinc;
	}

	public void setRostoPrinc(Rect rostoPrinc) {
		this.rostoPrinc = rostoPrinc;
	}
	
	public void cloneRostoPrinc(Rect rostoPrinc) {
		if(rostoPrinc instanceof Rect) this.rostoPrinc = new Rect(rostoPrinc.x(),rostoPrinc.y(),rostoPrinc.width(),rostoPrinc.height());
	}
	
	public void close() {
			try {
				if(imagem != null &&!imagem.isNull()) imagem.close();
			} catch (Exception e) {
			}
			try {
				if(rostoPrinc != null&&!rostoPrinc.isNull()) rostoPrinc.close();
			} catch (Exception e) {
			}
			try {
				if(rostos != null&&!rostos.isNull()) rostos.close();
			} catch (Exception e) {
			}
	}
	
	private RectVector cloneRectVector(RectVector faces) {
		RectVector vec = new RectVector(faces.size());
		for(int i = 0;i<faces.size();i++) vec.put(i,faces.get(i));
		return vec;
	}
}