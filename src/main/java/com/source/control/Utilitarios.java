/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.source.control;

import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.IntBuffer;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.JavaFXFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.MatVector;
import org.bytedeco.opencv.opencv_core.Point;
import org.bytedeco.opencv.opencv_core.Rect;
import org.bytedeco.opencv.opencv_core.RectVector;
import org.bytedeco.opencv.opencv_core.Scalar;
import org.bytedeco.opencv.opencv_core.Size;
import org.bytedeco.opencv.opencv_face.FaceRecognizer;
import org.bytedeco.opencv.opencv_face.FisherFaceRecognizer;
import org.bytedeco.opencv.opencv_face.LBPHFaceRecognizer;
import org.bytedeco.opencv.opencv_objdetect.CascadeClassifier;

import facerecognizers.FisherRecog;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author Jones
 */
public class Utilitarios {

	public static Image convertMatToImage(org.bytedeco.opencv.opencv_core.Mat grabbedImage) {
		OpenCVFrameConverter.ToMat converter = new OpenCVFrameConverter.ToMat();
		Frame fram = converter.convert(grabbedImage);
		JavaFXFrameConverter fxConverter = new JavaFXFrameConverter();
		Image img = fxConverter.convert(fram);
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

	/**
	 * */
	/**
	 * @param cas          CascadeClassfier com classificadores
	 * @param grabbedImage Imagem para ser processada
	 * @return Image retorna uma imagem com rostos contornado por um retangulo
	 * @throws Exception
	 */
	public static Image detectFacesImage(CascadeClassifier cas, Mat grabbedImage) throws Exception {
		RectVector facesDetect = detectFaces(cas, grabbedImage);
		System.out.println(facesDetect.get().length);
		for (Rect rect : facesDetect.get()) {
			System.out.println(rect.x() + "   " + rect.y() + "  " + rect.width() + "  " + rect.height());
			opencv_imgproc.rectangle(grabbedImage, new Point(rect.x(), rect.y()),
					new Point(rect.x() + rect.width(), rect.y() + rect.height()), Scalar.RED, 1, opencv_imgproc.LINE_AA,
					0);
		}
		Utilitarios ut = new Utilitarios();
		Image imgRect = Utilitarios.convertMatToImage(grabbedImage);
		return imgRect;
	}
	
	public static RectVector detectFaces(CascadeClassifier cas, Mat grabbedImage) {
		Mat imgCinza = new Mat();
		opencv_imgproc.cvtColor(grabbedImage, imgCinza, opencv_imgproc.COLOR_BGR2GRAY);
		showImage(convertMatToImage(grabbedImage));
		RectVector facesDetect = new RectVector();
		cas.detectMultiScale(imgCinza, facesDetect);
		return facesDetect;
	}
	

	public static void fi(CascadeClassifier cas, String path) {
		Mat img = opencv_imgcodecs.imread(path);
		Mat img2 = opencv_imgcodecs.imread("C:/aaa.png");

		MatVector vec = new MatVector();
		vec.push_back(img);
		vec.push_back(img2);
		try {
	        IntPointer label = new IntPointer(1);
	        DoublePointer confidence = new DoublePointer(1);
			FisherRecog fisher = new FisherRecog(cas);
			System.out.println(vec.get().length);
			FaceRecognizer rec = fisher.train(vec);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
