package com.view.controllers;

import com.source.control.WebcamThreadDetect;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

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
	private WebcamThreadDetect detec;
	
	public CLogin() {
		detec = new WebcamThreadDetect(imgView, null);
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
