package com.view.controllers;

import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;

import com.source.Alerts;
import com.source.Aplicacao;
import com.source.control.Biometria;
import com.source.control.ControllerBd;
import com.source.control.Utilitarios;
import com.source.model.Acesso;

import javafx.application.Platform;
import javafx.concurrent.Task;
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
 * Classe que é utilizada como controlador de listeners para interface GUI
 * Login.fxml
 */
public class CLogin {

	@FXML
	private TextField txtAnaliza;
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

	public CLogin() {

	}

	@FXML
	private void actBtnCarregar() {
		try {

			FileChooser cho = new FileChooser();
			File f = cho.showOpenDialog(null);
			Mat img = opencv_imgcodecs.imread(f.getAbsolutePath());
			Mat show = img.clone();
			imagemInput = Biometria.processImagem(show);
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
		Stream<Acesso> stre = ControllerBd.getAcessoAsStream();
		ThreadCompara t = new ThreadCompara(stre);
		new Thread(t).start();
	}

	@FXML
	private void actBtnLogin() {
		String senha = txtSenhaMestra.getText();
		if(senha != null && !senha.isEmpty() && senha.contentEquals("1020304050")) {
			Acesso ace = new Acesso(null, "Master", 3, null, 0, 0, 0, null);
			loginConf(ace);
		}else {
			Alerts.showError("Senha Incorreta");
		}

	}

	private class ThreadCompara extends Task<Void> {

		private Stream<Acesso> stream;
		private Integer iteracoes = 0;
		private Biometria b = new Biometria();
		private Mat imagemResult;
		private Mat imagemCompa;
		private Utilitarios ut = new Utilitarios();

		public ThreadCompara(Stream<Acesso> stream) {
			this.stream = stream;
		}

		@Override
		protected Void call() throws Exception {
			txtAnaliza.setText(Integer.toString(iteracoes));
			stream.forEach(obj -> {

				Mat img = Utilitarios.createMatByByteArr(obj.getRows(), obj.getCol(), obj.getType(),
						obj.getImagemByte());
				Mat temp = b.compare(imagemInput, img, 20);
				if (temp != null) {
					acesso = obj;
					imagemResult = img.clone();
					imagemCompa = temp.clone();
				}
				iteracoes++;
				txtAnaliza.setText(Integer.toString(iteracoes));
			});

			if (imagemResult != null) {
				opencv_imgproc.cvtColor(imagemResult, imagemResult, opencv_imgproc.COLOR_GRAY2BGR);
				imgViewResul.setImage(ut.convertMatToImg(imagemResult));
				imgCompara.setImage(ut.convertMatToImg(imagemCompa));
				loginConf(acesso);
			} else {
				Platform.runLater(() -> {
					Alerts.showError("Acesso não autorizado");
				});
			}
			return null;
		}
	}

	private void loginConf(Acesso ass) {


		Platform.runLater(() -> {
			try {
				Alerts.showInformation(
						String.format("Login confirmado, nível de acesso: %d + %s", ass.getNivel(), ass.getNome()));
				Stage stage = (Stage) parentPane.getScene().getWindow();
				FXMLLoader root = Aplicacao.listFrameRoot.get("Registro");
				stage.setScene(new Scene(root.load(), 600, 500));
				stage.setResizable(true);
				CRegistro controller = root.getController();
				controller.construtor(ass);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

	}

}
