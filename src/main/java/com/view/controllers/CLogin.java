package com.view.controllers;

import java.io.File;

import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;

import com.source.Aplicacao;
import com.source.control.Utilitarios;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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
	private ImageView imgViewInput;
	@FXML
	private StackPane stackPanel1;
	@FXML
	private ImageView imgViewResul;
	@FXML
	private Button btnCarregar;
	@FXML
	private Button btnIniciarRe;
	
	private Mat imagem;
	
	public CLogin() {
		btnIniciarRe.setDisable(true);
		Aplicacao.stage.setResizable(false);
	}
	
	@FXML
	private void actBtnCarregar() {
		try {
			
			FileChooser cho = new FileChooser();
			File f = cho.showOpenDialog(Aplicacao.stage);
			imagem = opencv_imgcodecs.imread(f.getAbsolutePath(),opencv_imgcodecs.IMREAD_GRAYSCALE);
			Mat show = imagem.clone();
			opencv_imgproc.cvtColor(imagem, show, opencv_imgproc.COLOR_GRAY2BGR);
			imgViewInput.setImage(Utilitarios.convertMatToImage(show));
			imgViewInput.setFitWidth(stackPanel.getWidth());
			imgViewInput.setFitHeight(stackPanel.getHeight());
			imagem.close();
			show.close();
			btnIniciarRe.setDisable(false);
		} catch (Exception e) {
			new Alert(AlertType.ERROR,"Formato de imagem inválido");
			e.printStackTrace();
		}
	}
	
	@FXML
	private void actBtnIniciarRe() {
		//TODO 
		
	}
	
	@FXML
	private void actBtnLogin() {
		//TODO
	}
}
