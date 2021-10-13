
package com.view.controllers;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Rect;
import org.bytedeco.opencv.opencv_core.RectVector;
import org.bytedeco.opencv.opencv_face.EigenFaceRecognizer;
import org.bytedeco.opencv.opencv_face.FaceRecognizer;
import org.bytedeco.opencv.opencv_face.FisherFaceRecognizer;
import org.bytedeco.opencv.opencv_face.LBPHFaceRecognizer;
import org.bytedeco.opencv.opencv_objdetect.CascadeClassifier;
import org.bytedeco.opencv.opencv_videoio.VideoCapture;

import com.source.Aplicacao;
import com.source.control.Utilitarios;
import com.source.control.WebcamThreadDetect;
import com.source.control.WebcamThreadTrain;
import com.source.model.Imag;

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

	public CMainFrame() {
		cas = new CascadeClassifier();
		loadClassifiers("com/classifiers/haar", cas).start();
	}

	@FXML
	public void actBtnTreinaImg() {
		try {
			LBPHFaceReco reco = new LBPHFaceReco(cas);
			FileChooser cho = new FileChooser();
			List<File> files = cho.showOpenMultipleDialog(Aplicacao.stage);
			List<Imag> imagens = new ArrayList<Imag>();
			FaceRecognizer model = LBPHFaceRecognizer.create();
			model.read(new FileChooser().showOpenDialog(null).getAbsolutePath());
			for (File file : files) {
				
				imagens.add(new Imag(1,txtDescri.getText() , null, opencv_imgcodecs.imread(file.getAbsolutePath()), false, new RectVector(),
						new Rect()));
			}
			reco.updateRaw(model, imagens).write(new FileChooser().showSaveDialog(Aplicacao.stage).getAbsolutePath());
			//reco.trainRawFiles(files).write(new FileChooser().showSaveDialog(Aplicacao.stage).getAbsolutePath());
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
				LBPHFaceReco recog = new LBPHFaceReco(cas);
				WebcamThreadTrain web = new WebcamThreadTrain(img, capture, cas, recog, Integer.parseInt(txtId.getText()), txtDescri.getText());
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
		FaceRecognizer rec = LBPHFaceRecognizer.create();
		rec.read(new FileChooser().showOpenDialog(null).getAbsolutePath());
		try {
			if (!cameraStatus) {
				if (capture == null) {
					capture = new VideoCapture(0);
				}
				LBPHFaceReco recog = new LBPHFaceReco(cas);
				WebcamThreadDetect web = new WebcamThreadDetect(img, capture, cas, rec, recog);

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
