package com.view.controllers;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.springframework.stereotype.Component;

import com.source.Alerts;
import com.source.Aplicacao;
import com.source.control.ControllerBd;
import com.source.control.Utilitarios;
import com.source.model.Acesso;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Classe que é utilizada como controlador de listeners para interface GUI Login.fxml*/
@Component
public class CLogin {

	@FXML
	private BorderPane parentPane;
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

	}

	@FXML
	private void actBtnCarregar() {
		try {

			FileChooser cho = new FileChooser();
			File f = cho.showOpenDialog(null);
			imagem = opencv_imgcodecs.imread(f.getAbsolutePath(), opencv_imgcodecs.IMREAD_GRAYSCALE);
			Mat show = imagem.clone();
			opencv_imgproc.cvtColor(imagem, show, opencv_imgproc.COLOR_GRAY2BGR);
			imgViewInput.setImage(Utilitarios.convertMatToImage(show));
			imgViewResul.setImage(Utilitarios.convertMatToImage(show));
			imgViewInput.setFitWidth(stackPanel.getWidth());
			imgViewInput.setFitHeight(stackPanel.getHeight());
			imagem.close();
			

			
			btnIniciarRe.setDisable(false);
		} catch (Exception e) {
			Alert a = new Alert(AlertType.ERROR, "Formato de arquivo inválido");
			a.setHeaderText(null);
			a.showAndWait();
			e.printStackTrace();
		}
	}

	@FXML
	private void actBtnIniciarRe() {
		// TODO


	}

	@FXML
	private void actBtnLogin() {
		// TODO
		Alerts.showInformation("Login confirmado, nível de acesso: %s");
		Acesso ace = new Acesso(null, "Master", 3, null, 0, 0, 0, null);
		try {
			Stage stage = (Stage) parentPane.getScene().getWindow();
			FXMLLoader root = Aplicacao.listFrameRoot.get("Registro");
			stage.setScene(new Scene(root.load(), 600, 500));
			CRegistro controller = root.getController();
			controller.construtor(ace);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
