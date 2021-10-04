package com.view.controllers;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_objdetect.CascadeClassifier;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;

public class CMainFrame{
	
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
	
	private CascadeClassifier cas;
	
	public CMainFrame() {
		cas = new CascadeClassifier();
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
			Mat grabbedImage = opencv_imgcodecs.imread(fImg.getAbsolutePath());
			int height = grabbedImage.rows();
			int width = grabbedImage.cols();
			System.out.println(height);
			System.out.println(width);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			new Alert(AlertType.ERROR,"Formato Inválido").showAndWait();
		}
	}
	@FXML
	public void actDetectFace() {
		

		
	}
	@FXML
	public void actHaarSelected() {
		loadClassifiers("com/classifiers/haar");
	}
	
	public void actLbpSelected() {
		loadClassifiers("com/classifiers/lbp");
	}
	
	private void loadClassifiers(String folderPath) {
		File file;
		try {
			file = new File(getClass().getClassLoader().getResource(folderPath).toURI());
			File[] files = file.listFiles();
	        for(File fier : files){
	        	System.out.println(fier.getPath());
	        	cas.load(fier.getPath().toString());
	        }
		} catch (URISyntaxException e) {
			new Alert(AlertType.ERROR,"Falha ao carregar Classificadores").showAndWait();
			e.printStackTrace();
		}

	}

}
