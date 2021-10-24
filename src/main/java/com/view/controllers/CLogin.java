package com.view.controllers;

import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;

import com.source.control.Utilitarios;
import com.source.model.Imag;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;

public class CLogin {

	@FXML
	private BorderPane borderPane;
	@FXML
	private TextField txtSenhaMestra;
	@FXML
	public Button btnLogin;
	@FXML
	private StackPane stackPanel;
	@FXML
	private ImageView imgView;
	@FXML
	private Button btnIniciarDetec;
	@FXML
	private Button btnPararDetec;
	
	public CLogin() {
	}
	
	@FXML
	private void actIniciarDetec() {
	}
	@FXML
	private void actPararDetec() {
		
	}
	@FXML
	private void actBtnLogin() {
		
	}
}
