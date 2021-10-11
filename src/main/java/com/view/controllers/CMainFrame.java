
package com.view.controllers;

import java.io.File;
import java.net.MalformedURLException;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_face.EigenFaceRecognizer;
import org.bytedeco.opencv.opencv_face.FaceRecognizer;
import org.bytedeco.opencv.opencv_face.FisherFaceRecognizer;
import org.bytedeco.opencv.opencv_face.LBPHFaceRecognizer;
import org.bytedeco.opencv.opencv_objdetect.CascadeClassifier;
import org.bytedeco.opencv.opencv_videoio.VideoCapture;

import com.source.control.Utilitarios;
import com.source.control.WebcamThreadDetect;
import com.source.control.WebcamThreadTrain;

import facerecognizers.FisherRecog;
import facerecognizers.LBPHFaceReco;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
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
	public ImageView img;
	@FXML
	public Button btnCarregar;
	@FXML
	public Button btnDetectFace;
	@FXML
	public Button btnTreina;
	@FXML
	public Button btnTeste;
	@FXML 
	public Button btnDetec;
	@FXML
	public TextField txtNome;
	
	public static String txt = "a";
	
	
	
	private Mat grabbedImage;
	private CascadeClassifier cas;
	
	private boolean cameraStatus = false;
	private VideoCapture capture;
	private CascadeClassifier cas2;
	
	public CMainFrame() {
		cas = new CascadeClassifier();
		cas2 = new CascadeClassifier();
		loadClassifiers("com/classifiers/haar",cas).start();
		loadClassifiers("com/classifiers/haar", cas2).start();
	}

	@FXML
	public void actTreina() {
		try {
			if(!cameraStatus) {
				if(capture == null) {
					capture = new VideoCapture(0);
				}
				LBPHFaceReco recog = new LBPHFaceReco(cas);
				WebcamThreadTrain web = new WebcamThreadTrain(img, capture, cas, recog, 2);
				new Thread(web).start();
				
				cameraStatus = true;
				btnCarregar.setDisable(true);
				btnTreina.setText("Parar Camera");

			}else {
				capture.close();
				capture = null;
				cameraStatus = false;
				btnTreina.setText("Abrir Camera");
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
	public void actBtnDetec() {
		FaceRecognizer rec = LBPHFaceRecognizer.create();
		rec.read(new FileChooser().showOpenDialog(null).getAbsolutePath());
		try {
			if(!cameraStatus) {
				if(capture == null) {
					capture = new VideoCapture(0);
				}
				LBPHFaceReco recog = new LBPHFaceReco(cas);
				WebcamThreadDetect web = new WebcamThreadDetect(img, capture, cas,rec, recog);
				
				new Thread(web).start();
				
				cameraStatus = true;
				btnCarregar.setDisable(true);
				btnDetec.setText("Parar Camera");

			}else {
				capture.close();
				capture = null;
				cameraStatus = false;
				btnDetec.setText("Abrir Camera");
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
		txt = txtNome.getText();
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
			Image imgRect = Utilitarios.detectFaceRect(cas, grabbedImage);
			img.setImage(imgRect);
		} catch (Exception e) {
			new Alert(AlertType.ERROR,"Falha ao converter imagem");
			e.printStackTrace();
		}
	}


	private synchronized Thread loadClassifiers(String folderPath, CascadeClassifier cascas) {

		Task<Integer> loadClassifiers = new Task<Integer>() {

			@Override
			protected Integer call() throws Exception {
				File file;
				try {
					file = new File(getClass().getClassLoader().getResource(folderPath).toURI());
					File[] files = file.listFiles();
					for (File fier : files) {
						System.out.println(fier.getPath());
						cascas.load(fier.getPath().toString());
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
