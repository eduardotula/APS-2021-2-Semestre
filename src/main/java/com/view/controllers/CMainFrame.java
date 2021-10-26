
package com.view.controllers;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.KeyPointVector;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.MatVector;
import org.bytedeco.opencv.opencv_core.Rect;
import org.bytedeco.opencv.opencv_core.RectVector;
import org.bytedeco.opencv.opencv_face.EigenFaceRecognizer;
import org.bytedeco.opencv.opencv_face.FaceRecognizer;
import org.bytedeco.opencv.opencv_face.FisherFaceRecognizer;
import org.bytedeco.opencv.opencv_face.LBPHFaceRecognizer;
import org.bytedeco.opencv.opencv_features2d.FlannBasedMatcher;
import org.bytedeco.opencv.opencv_objdetect.CascadeClassifier;
import org.bytedeco.opencv.opencv_videoio.VideoCapture;
import org.bytedeco.opencv.opencv_xfeatures2d.SURF;

import com.source.Aplicacao;
import com.source.control.FisherRecog;
import com.source.control.Utilitarios;
import com.source.control.WebcamThreadDetect;
import com.source.control.WebcamThreadTrain;
import com.source.model.Imag;

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
 * @author Eduardo Muterle Classe controladora de listners interface gráfica
 *         MainFrame.fxml Esta classe contem todas as funções necessarias para
 *         reconhecer uma imagem
 *
 */
public class CMainFrame {

	@FXML
	public StackPane stack1;
	@FXML
	public ImageView img;
	@FXML
	public Button btnTreina;
	@FXML
	public Button btnTeste;
	@FXML
	public Button btnDetec;
	@FXML
	public Button btnTreinaImg;
	@FXML
	public TextField txtId;
	@FXML
	public TextField txtDescri;





	public CMainFrame() {
		
	}

	@FXML
	public void actBtnTreinaImg() {
		Mat img1 = opencv_imgcodecs.imread(new FileChooser().showOpenDialog(null).getAbsolutePath(),opencv_imgcodecs.IMREAD_GRAYSCALE);
		Mat img2 = opencv_imgcodecs.imread(new FileChooser().showOpenDialog(null).getAbsolutePath(),opencv_imgcodecs.IMREAD_GRAYSCALE);
		SURF a = SURF.create();
		KeyPointVector keyPointVector = new KeyPointVector();
		Mat objectDescr = new Mat();
		a.detectAndCompute(img1, new Mat(), keyPointVector, objectDescr, false);
		System.out.println(keyPointVector.size());
		System.out.println(objectDescr.cols());
		FlannBasedMatcher matcher = FlannBasedMatcher.create();
	
		
	}

	@FXML
	public void actTreina() {
	
			
	}

	@FXML
	public void actBtnDetec() {
		
	}

	@FXML
	public void actBtnTeste() {
		
	}

	
	

}
