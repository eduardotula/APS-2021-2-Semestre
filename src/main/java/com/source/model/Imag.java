package com.source.model;

import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Rect;
import org.bytedeco.opencv.opencv_core.RectVector;

public class Imag {

	private Integer idLabel;
	private String descri;
	private Mat label;
	private Mat imagem;
	private boolean proces;
	private RectVector rostos;
	private Rect rostoPrinc;
	
	
	public Imag() {
		label = new Mat(0,1,opencv_core.CV_32SC1);
		imagem = new Mat();
		proces = false;
		rostos = new RectVector();
		rostoPrinc = new Rect();
	}

	public Imag(Integer idModel, String descri ,Mat label,Mat imagem, boolean proces, RectVector rostos, Rect rect) {
		super();
		this.idLabel = idModel;
		this.descri = descri;
		if(label instanceof Mat) this.label = label.clone();  else this.label = new Mat();
		if(imagem instanceof Mat) this.imagem = imagem.clone(); else this.imagem = new Mat();
		this.proces = proces;
		if(rostos instanceof RectVector) this.rostos = cloneRectVector(rostos); else this.rostos = new RectVector();
		if(rect instanceof Rect) this.rostoPrinc = new Rect(rect.x(),rect.y(),rect.width(),rect.height());
	}

	public Integer getIdLabel() {
		return idLabel;
	}

	public Mat getLabel() {
		return label;
	}

	public String getDescri() {
		return descri;
	}

	public void setDescri(String descri) {
		this.descri = descri;
	}

	public void setLabel(Mat label) {
		this.label = label;
	}

	public void cloneLabel(Mat label) {
		if(label instanceof Mat) this.label = label.clone();  else this.label = new Mat();
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
	
	public void cloneImagem(Mat imagem) {
		if(imagem instanceof Mat) this.imagem = imagem.clone(); else this.imagem = new Mat();
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