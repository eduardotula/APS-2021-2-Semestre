
package com.view.controllers;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

import org.bytedeco.flycapture.FlyCapture2.CameraStats;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacv.OpenCVFrameGrabber;
import org.bytedeco.javacv.VideoInputFrameGrabber;
import org.bytedeco.opencv.global.opencv_features2d;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Point;
import org.bytedeco.opencv.opencv_core.Rect;
import org.bytedeco.opencv.opencv_core.RectVector;
import org.bytedeco.opencv.opencv_core.Scalar;
import org.bytedeco.opencv.opencv_face.Facemark;
import org.bytedeco.opencv.opencv_face.FacemarkKazemi;
import org.bytedeco.opencv.opencv_features2d.SIFT;
import org.bytedeco.opencv.opencv_objdetect.CascadeClassifier;
import org.bytedeco.opencv.opencv_videoio.VideoCapture;
import org.opencv.core.Core;
import org.opencv.core.MatOfRect;
import org.opencv.face.Face;

import com.source.control.Utilitarios;
import com.source.control.WebcamThread;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;
import javafx.stage.FileChooser;

/**
 * @author Eduardo Muterle
 * Classe controladora de listners interface gráfica MainFrame.fxml
 * Esta classe contem todas as funções necessarias para reconhecer uma imagem  
 *
 */
public class CMainFrame {

	@FXML
	public StackPane stack1;
	@FXML
	public CheckMenuItem rdnHaar;
	@FXML
	public CheckMenuItem rdnLpb;
	@FXML
	public ImageView img;
	@FXML
	public Button btnCarregar;
	@FXML
	public Button btnDetectFace;
	@FXML
	public Button btnStartCamera;
	@FXML
	public Button btnTeste;
	
	
	private Mat grabbedImage;
	private CascadeClassifier cas;
	private boolean cameraStatus = false;
	private VideoCapture capture;
	
	public CMainFrame() {
		cas = new CascadeClassifier();
		loadClassifiers("com/classifiers/haar").start();
		loadClassifiers("com/classifiers/lbp");

	}

	@FXML
	public void actStartCamera() {
		try {
			if(!cameraStatus) {
				if(capture == null) {
					capture = new VideoCapture(0);
				}
				
				WebcamThread web = new WebcamThread(img, capture, cas);
				new Thread(web).start();
				
				cameraStatus = true;
				btnCarregar.setDisable(true);
				btnStartCamera.setText("Parar Camera");

			}else {
				capture.close();
				capture = null;
				cameraStatus = false;
				btnStartCamera.setText("Abrir Camera");
				btnCarregar.setDisable(false);
				img.setImage(null);
			}
			img.fitWidthProperty().bind(stack1.widthProperty());
			img.fitHeightProperty().bind(stack1.heightProperty());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	@FXML
	public void actBtnTeste() {
		Utilitarios.fi(cas, "C:/FotoTeste.jpg");
	}
	
	@FXML
	public void actBtnCarregar() {
		try {
			
			img.fitWidthProperty().bind(stack1.widthProperty());
			img.fitHeightProperty().bind(stack1.heightProperty());
			FileChooser cho = new FileChooser();
			File fImg = cho.showOpenDialog(null);


			img.setImage(new Image(fImg.toURI().toURL().toString()));
			btnDetectFace.setDisable(false);
			grabbedImage = opencv_imgcodecs.imread(fImg.getAbsolutePath());
			int height = grabbedImage.rows();
			int width = grabbedImage.cols();
			System.out.println(height);
			System.out.println(width);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			new Alert(AlertType.ERROR, "Formato Inválido").showAndWait();
		}
	}

	@FXML
	public void actDetectFace() {
		try {
			Image imgRect = Utilitarios.detectFacesImage(cas, grabbedImage);
			img.setImage(imgRect);
		} catch (Exception e) {
			new Alert(AlertType.ERROR,"Falha ao converter imagem");
			e.printStackTrace();
		}
	}

	@FXML
	public void actHaarSelected() {
		loadClassifiers("com/classifiers/haar");
	}

	public void actLbpSelected() {
		loadClassifiers("com/classifiers/lbp");
	}

	private synchronized Thread loadClassifiers(String folderPath) {

		Task<Integer> loadClassifiers = new Task<Integer>() {

			@Override
			protected Integer call() throws Exception {
				File file;
				try {
					file = new File(getClass().getClassLoader().getResource(folderPath).toURI());
					File[] files = file.listFiles();
					for (File fier : files) {
						System.out.println(fier.getPath());
						cas.load(fier.getPath().toString());
					}
				} catch (Exception e) {

					new Alert(AlertType.ERROR, "Falha ao carregar Classificadores").showAndWait();
					e.printStackTrace();
					System.exit(0);
				}
				return null;
			}
		};
		return new Thread(loadClassifiers);
	}

}
