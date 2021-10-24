
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
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.MatVector;
import org.bytedeco.opencv.opencv_core.Rect;
import org.bytedeco.opencv.opencv_core.RectVector;
import org.bytedeco.opencv.opencv_face.EigenFaceRecognizer;
import org.bytedeco.opencv.opencv_face.FaceRecognizer;
import org.bytedeco.opencv.opencv_face.FisherFaceRecognizer;
import org.bytedeco.opencv.opencv_face.LBPHFaceRecognizer;
import org.bytedeco.opencv.opencv_objdetect.CascadeClassifier;
import org.bytedeco.opencv.opencv_videoio.VideoCapture;

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

	public static String txt = "a";

	private Mat grabbedImage;
	private CascadeClassifier cas;
	private boolean cameraStatus = false;
	private VideoCapture capture;
	private FisherRecog recog;

	public CMainFrame() {

		recog = new FisherRecog();
	}

	@FXML
	public void actBtnTreinaImg() {
		try {
			FileChooser cho = new FileChooser();
			List<File> files = cho.showOpenMultipleDialog(Aplicacao.stage);
			// FaceRecognizer model = FisherFaceRecognizer.create();
			/// model.read(new FileChooser().showOpenDialog(null).getAbsolutePath());
			MatVector images = new MatVector(files.size()+1);

			Mat labels = new Mat(files.size()+1, 1, opencv_core.CV_32SC1);
			IntBuffer labelsBuf = labels.createBuffer();

			int counter = 1;
			images.put(0,new Mat(150,150,opencv_imgproc.COLOR_BGR2GRAY));
			labelsBuf.put(0,0);
			for (File file : files) {

				Imag img = new Imag(1, txtDescri.getText(), null,
						opencv_imgcodecs.imread(file.getAbsolutePath(), opencv_imgcodecs.IMREAD_GRAYSCALE), false,
						new RectVector(), new Rect());

				images.put(counter, img.getImagem());
				labelsBuf.put(counter, img.getIdLabel());

				counter++;
			}

			
			//FaceRecognizer faceRecognizer = FisherFaceRecognizer.create();
			//FaceRecognizer faceRecognizer = EigenFaceRecognizer.create();
			// FaceRecognizer faceRecognizer = LBPHFaceRecognizer.create();

			System.out.println(images.size());
			System.out.println(labels.arrayHeight());
			
			//faceRecognizer.train(images, labels);
			/*IntPointer label = new IntPointer(1);
			DoublePointer confidence = new DoublePointer(1);
			//faceRecognizer.predict(opencv_imgcodecs.imread("C:/imgs/aaa.jpg", opencv_imgcodecs.IMREAD_GRAYSCALE), label,
				//	confidence);
			int predictedLabel = label.get(0);
			System.out.println(confidence.get());
			System.out.println("Predicted label: " + predictedLabel);*/
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@FXML
	public void actTreina() {
		try {
			if (!cameraStatus) {
				if (capture == null) {
					capture = new VideoCapture(0);
				}
				
				WebcamThreadTrain web = new WebcamThreadTrain(img, capture, recog,
						Integer.parseInt(txtId.getText()), txtDescri.getText());
				new Thread(web).start();

				cameraStatus = true;
				btnTreina.setText("Parar Treinamento");

			} else {
				capture.close();
				capture = null;
				cameraStatus = false;
				btnTreina.setText("Treinamento");
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
		try {
			if (!cameraStatus) {
				if (capture == null) {
					capture = new VideoCapture(0);
				}
				WebcamThreadDetect web = new WebcamThreadDetect(img, capture, cas, recog);

				new Thread(web).start();

				cameraStatus = true;
				btnDetec.setText("Parar Camera");

			} else {
				capture.close();
				capture = null;
				cameraStatus = false;
				btnDetec.setText("Abrir Camera");
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
		try {
			recog.startTrain();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	

}
