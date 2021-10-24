/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.source.control;

import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.JavaFXFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Point;
import org.bytedeco.opencv.opencv_core.Rect;
import org.bytedeco.opencv.opencv_core.RectVector;
import org.bytedeco.opencv.opencv_core.Scalar;
import org.bytedeco.opencv.opencv_objdetect.CascadeClassifier;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Utilitarios {

	private static JavaFXFrameConverter fxConverter = new JavaFXFrameConverter();
	private static OpenCVFrameConverter.ToMat converter = new OpenCVFrameConverter.ToMat();

	/**
	 * Converte um vetor de pixels Mat para um objeto javafx image
	 * 
	 * @param Mat imagem para ser convertida
	 * @return imagem javafx
	 */
	public static Image convertMatToImage(org.bytedeco.opencv.opencv_core.Mat grabbedImage) {
		Frame fram = converter.convert(grabbedImage.clone());
		System.out.println("Fram" + fram.imageWidth);
		Image img = fxConverter.convert(fram);
		fram.close();
		return img;
	}

	public static void showImage(Image img) {
		Stage stage = new Stage();
		StackPane pa = new StackPane();
		ImageView vi = new ImageView();
		pa.getChildren().add(vi);
		stage.setScene(new Scene(pa, 500, 600));
		vi.setFitHeight(img.getHeight());
		vi.setFitWidth(img.getWidth());
		vi.setImage(img);
		stage.show();
	}

	public static void showImage(Mat img) {
		try {
			
			showImage(convertMatToImage(img));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * */
	/**
	 * @param cas          CascadeClassfier com classificadores
	 * @param grabbedImage Imagem para ser processada
	 * @return Image retorna uma imagem com rostos contornados por retangulos
	 * @throws Exception
	 */
	public static Image detectFaceRect(CascadeClassifier cas, Mat grabbedImage) throws Exception {
		RectVector facesDetect = detectFaces(cas, grabbedImage);
		System.out.println(facesDetect.get().length);
		for (Rect rect : facesDetect.get()) {
			System.out.println(rect.x() + "   " + rect.y() + "  " + rect.width() + "  " + rect.height());
			opencv_imgproc.rectangle(grabbedImage, new Point(rect.x(), rect.y()),
					new Point(rect.x() + rect.width(), rect.y() + rect.height()), Scalar.GREEN, 1, opencv_imgproc.LINE_AA,
					0);
			rect.close();
		}
		facesDetect.close();
		return Utilitarios.convertMatToImage(grabbedImage);

	}

	/**
	 * Detecta rostos em uma imagem grabbedImage detecta qual o contorno com maio tamanho e retorna uma imagem
	 * tipo JavaFx
	 * @param cas Cascade Classifier com classificadores
	 * @param grabbedImage imagem para ser processada
	 * @return Image retorna uma imagem com rosto contornado por um retangulo
	 * @throws Exception caso a imagem não contenha rostos ou imagem não estiver no formato correto*/
	public static Image detectFacePrincipalRect(CascadeClassifier cas, Mat grabbedImage) throws Exception{
		RectVector facesDetect = detectFaces(cas, grabbedImage);
		System.out.println(facesDetect.get().length);
		Rect rect = detectFacePrincipal(facesDetect);
		Mat img = grabbedImage.clone();
		System.out.println(rect.x() + "   " + rect.y() + "  " + rect.width() + "  " + rect.height());
		opencv_imgproc.rectangle(img, new Point(rect.x(), rect.y()),
				new Point(rect.x() + rect.width(), rect.y() + rect.height()), Scalar.GREEN, 1, opencv_imgproc.LINE_AA, 0);
		Image fxImg = convertMatToImage(img);
		
		img.close();rect.close();facesDetect.close();
		
		return fxImg;

	}

	/**
	 * Detecta a posição de rostos na imagem
	 * 
	 * @param cas          Classificador cascade
	 * @param grabbedImage imagem original
	 * @return retorna um vetor de posição de todas as faces do frame
	 */
	public static RectVector detectFaces(CascadeClassifier cas, Mat grabbedImage) {
		System.out.println("Metodo detectFaces");
		Mat imgGray = grabbedImage.clone();

		System.out.println(grabbedImage.rows() + " rows");
		System.out.println(grabbedImage.channels() + " channels");
		if (grabbedImage.type() > 0) {
			opencv_imgproc.cvtColor(grabbedImage, imgGray, opencv_imgproc.COLOR_BGR2GRAY);
		}
		RectVector facesDetect = new RectVector();
		cas.detectMultiScale(imgGray, facesDetect);
		System.out.println(facesDetect.size() + "  size");
		imgGray.close();
		
		return facesDetect;
	}

	public static RectVector cloneRectVector(RectVector vector) {
		RectVector ve = new RectVector(vector.size());
		for(int i = 0;i<vector.size();i++) {
			ve.put(i,vector.get(i));
		}
		return ve;
	}
	/**
	 * Detecta o rosto com maior resolução
	 * 
	 * @param facesDetectadas vetor contendo faces identificadas
	 * @return rosto com maior resolução
	 */
	public static Rect detectFacePrincipal(RectVector facesDetectadas) {

		Rect rostoPrimario = new Rect();

		// Obtem o rosto detectado com maior resolução do frame
		for (Rect f : facesDetectadas.get()) {
			if (f.width() > rostoPrimario.width() && f.height() > rostoPrimario.height()) {
				rostoPrimario = new Rect(f);
			}
			
		}
		System.out.println("RostoPrimaro " + rostoPrimario.width());
		return rostoPrimario;
	}
	
	/**
	 * Libera recursos
	 * @param args objetos para serem fechados*/
	public static void releaseResources(Pointer... args) {
		for(Pointer arg : args) {
			try {
				arg.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
}

}
