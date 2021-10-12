package com.source.model;

import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Rect;
import org.bytedeco.opencv.opencv_core.RectVector;

public class Imag {

	private Integer idLabel;
	private Mat label;
	private Mat imagem;
	private boolean proces;
	private RectVector rostos;
	private Rect rostoPrinc;
	
	
	public Imag() {
		
	}

	public Imag(Integer idModel, Mat label,Mat imagem, boolean proces, RectVector rostos, Rect rect) {
		super();
		this.idLabel = idModel;
		this.label = label;
		this.imagem = imagem;
		this.proces = proces;
		this.rostos = rostos;
		this.rostoPrinc = rect;
	}

	public Integer getIdLabel() {
		return idLabel;
	}

	public Mat getLabel() {
		return label;
	}

	public void setLabel(Mat label) {
		this.label = label;
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

	public Rect getRostoPrinc() {
		return rostoPrinc;
	}

	public void setRostoPrinc(Rect rostoPrinc) {
		this.rostoPrinc = rostoPrinc;
	}
	
	public void close() {
			try {
				if(!imagem.isNull()) imagem.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if(!rostoPrinc.isNull()) rostoPrinc.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if(!rostos.isNull()) rostos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	
}
