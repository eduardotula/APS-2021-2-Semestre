package com.view.controllers;

import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.springframework.stereotype.Component;

import com.source.Alerts;
import com.source.Aplicacao;
import com.source.control.Biometria;
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
	private ImageView imgViewInput;
	@FXML
	private ImageView imgCompara;
	@FXML
	private ImageView imgViewResul;
	@FXML
	private Button btnCarregar;
	@FXML
	private Button btnIniciarRe;

	private Acesso acesso;
	private Mat imagemInput;
	private Mat imagemResult;
	
	public CLogin() {

	}

	@FXML
	private void actBtnCarregar() {
		try {

			FileChooser cho = new FileChooser();
			File f = cho.showOpenDialog(null);
			Mat img = opencv_imgcodecs.imread(f.getAbsolutePath(), opencv_imgcodecs.IMREAD_GRAYSCALE);
			Biometria.cropImg(img);
			Mat show = img.clone();
			imagemInput = Biometria.processTeste(show);
			opencv_imgproc.cvtColor(imagemInput, show, opencv_imgproc.COLOR_GRAY2BGR);
			
			imgViewInput.setImage(Utilitarios.convertMatToImage(show));

			
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
		Biometria b = new Biometria();
		Stream<Acesso> stre = ControllerBd.getAcessoAsStream();
		stre.forEach(obj -> {
			
			Mat img = Utilitarios.createMatByByteArr(obj.getRows(), obj.getCol(), obj.getType(), obj.getImagemByte());
			Mat resul = b.compareTeste(imagemInput, img,20);
			if(resul != null) {
				acesso = obj;
				imagemResult = img;
				imgCompara.setImage(Utilitarios.convertMatToImage(resul));
			}
		});
		if(imagemResult != null) {
			opencv_imgproc.cvtColor(imagemResult, imagemResult, opencv_imgproc.COLOR_GRAY2BGR);
			imgViewResul.setImage(Utilitarios.convertMatToImage(imagemResult));
			try {
				Thread.sleep(5000);
				loginConf(acesso);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}else {
			Alerts.showError("Acesso não autorizado");
		}
	}

	@FXML
	private void actBtnLogin() {
		
		Acesso ace = new Acesso(null, "Master", 3, null, 0, 0, 0, null);
		loginConf(ace);
	}
	
	private void loginConf(Acesso ass) {
		try {
			Alerts.showInformation(String.format("Login confirmado, nível de acesso: %d + %s",ass.getNivel(), ass.getNome()));
			Stage stage = (Stage) parentPane.getScene().getWindow();
			FXMLLoader root = Aplicacao.listFrameRoot.get("Registro");
			stage.setScene(new Scene(root.load(), 600, 500));
			stage.setResizable(true);
			CRegistro controller = root.getController();
			controller.construtor(ass);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
