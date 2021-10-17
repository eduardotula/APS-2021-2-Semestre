
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
			FisherRecog reco = new FisherRecog(cas);
			FileChooser cho = new FileChooser();
			List<File> files = cho.showOpenMultipleDialog(Aplicacao.stage);
<<<<<<< master
			// FaceRecognizer model = FisherFaceRecognizer.create();
			/// model.read(new FileChooser().showOpenDialog(null).getAbsolutePath());
			MatVector images = new MatVector(files.size()+1);

			Mat labels = new Mat(files.size()+1, 1, opencv_core.CV_32SC1);
			IntBuffer labelsBuf = labels.createBuffer();

			int counter = 1;
			images.put(0,new Mat(150,150,opencv_imgproc.COLOR_BGR2GRAY));
			labelsBuf.put(0,0);
=======
			List<Imag> imagens = new ArrayList<Imag>();
			FaceRecognizer model = FisherFaceRecognizer.create();
			///model.read(new FileChooser().showOpenDialog(null).getAbsolutePath());
>>>>>>> df93408 Commit
			for (File file : files) {

				Imag img = new Imag(1, txtDescri.getText(), null,
						opencv_imgcodecs.imread(file.getAbsolutePath(), opencv_imgcodecs.IMREAD_GRAYSCALE), false,
						new RectVector(), new Rect());

				images.put(counter, img.getImagem());
				labelsBuf.put(counter, img.getIdLabel());

				counter++;
			}
<<<<<<< master

			
			//FaceRecognizer faceRecognizer = FisherFaceRecognizer.create();
			//FaceRecognizer faceRecognizer = EigenFaceRecognizer.create();
			// FaceRecognizer faceRecognizer = LBPHFaceRecognizer.create();

			System.out.println(images.size());
			System.out.println(labels.arrayHeight());
			FisherRecog recog = new FisherRecog(cas);
			FaceRecognizer model = recog.trainRawFiles(files, 1, "yep");
			
			//faceRecognizer.train(images, labels);
			model.write(new FileChooser().showSaveDialog(null).getAbsolutePath());
			/*IntPointer label = new IntPointer(1);
			DoublePointer confidence = new DoublePointer(1);
			//faceRecognizer.predict(opencv_imgcodecs.imread("C:/imgs/aaa.jpg", opencv_imgcodecs.IMREAD_GRAYSCALE), label,
				//	confidence);
			int predictedLabel = label.get(0);
			System.out.println(confidence.get());
			System.out.println("Predicted label: " + predictedLabel);*/
=======
			//reco.updateRaw(model, imagens).write(new FileChooser().showSaveDialog(Aplicacao.stage).getAbsolutePath());
			reco.trainRaw(imagens).write(new FileChooser().showSaveDialog(Aplicacao.stage).getAbsolutePath());
			// FileChooser().showSaveDialog(Aplicacao.stage).getAbsolutePath());
>>>>>>> df93408 Commit
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
				// LBPHFaceReco recog = new LBPHFaceReco(cas);
				FisherRecog recog = new FisherRecog(cas);
<<<<<<< master

=======
>>>>>>> df93408 Commit
				WebcamThreadTrain web = new WebcamThreadTrain(img, capture, cas, recog,
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
		try {
			FisherRecog recog = new FisherRecog(cas);
			FisherFaceRecognizer model = FisherFaceRecognizer.create();
			model.read(new FileChooser().showOpenDialog(null).getAbsolutePath());
			Imag img = new Imag(Integer.parseInt(txtId.getText()), txtDescri.getText(), null,
					opencv_imgcodecs.imread(new FileChooser().showOpenDialog(null).getAbsolutePath()), false,
					new RectVector(), new Rect());
<<<<<<< master
			// model.setThreshold(80.0);
			img.setRostos(Utilitarios.detectFaces(cas, img.getImagem()));
			img.setRostoPrinc(Utilitarios.detectFacePrincipal(img.getRostos()));
			
			recog.identificarRosto(model, recog.processImage(img));
=======
			
			recog.identificarRosto(model, img);
>>>>>>> df93408 Commit
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (Exception e) {
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
