package com.view.controllers;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Point;
import org.bytedeco.opencv.opencv_core.Rect;
import org.bytedeco.opencv.opencv_core.RectVector;
import org.bytedeco.opencv.opencv_core.Scalar;
import org.bytedeco.opencv.opencv_objdetect.CascadeClassifier;
import org.opencv.core.Core;
import org.opencv.core.MatOfRect;

import com.source.Utilitarios;

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
	public ImageView img2;
	@FXML
	public Button btnCarregar;
	@FXML
	public Button btnDetectFace;

	Mat grabbedImage;
	private CascadeClassifier cas;

	public CMainFrame() {
		cas = new CascadeClassifier();
		loadClassifiers("com/classifiers/haar");
		// loadClassifiers("com/classifiers/lbp");
	}

	@FXML
	public void actBtnCarregar() {
		try {
			
			img.fitWidthProperty().bind(stack1.widthProperty());
			img.fitHeightProperty().bind(stack1.heightProperty());
			FileChooser cho = new FileChooser();
			File fImg = cho.showOpenDialog(null);

			System.out.println(stack1.getWidth());
			System.out.println(stack1.getHeight());

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
			Mat imgCinza = new Mat();
			opencv_imgproc.cvtColor(grabbedImage, imgCinza, opencv_imgproc.COLOR_BGR2GRAY);
			RectVector facesDetect = new RectVector();
			cas.detectMultiScale(imgCinza, facesDetect);
			System.out.println(facesDetect.get().length);
			for (Rect rect : facesDetect.get()) {
				System.out.println(rect.x() + "   " + rect.y() + "  " + rect.width() + "  " + rect.height());
				opencv_imgproc.rectangle(grabbedImage, new Point(rect.x(), rect.y()),
						new Point(rect.x() + rect.width(), rect.y() + rect.height()), Scalar.RED, 1, opencv_imgproc.LINE_AA,
						0);
			}
			Utilitarios ut = new Utilitarios();
			Image imgRect = ut.convertMatToImage(grabbedImage);
			img2.setImage(imgRect);
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

	private void loadClassifiers(String folderPath) {

		Task<Void> loadClassifiers = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
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
		new Thread(loadClassifiers).start();
	}

}
