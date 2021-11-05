package com.view.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;

import com.source.Alerts;
import com.source.control.Biometria;
import com.source.control.ControllerBd;
import com.source.control.Utilitarios;
import com.source.model.Acesso;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Classe que é utilizada como controlador de listeners para interface GUI
 * Acesso.fxml
 */
public class CAcesso implements Initializable{

	@FXML
	private AnchorPane parentPane;
	@FXML
	private TextField txtUsuario;
	@FXML
	private ComboBox<String> comboNivel;
	@FXML
	private ImageView img;
	@FXML
	private Button btnCancelar;

	@FXML
	private Button btnSalvar;
	@FXML
	private Button btnSalvarImg;
	
	private Mat imagem;

	private Acesso acesso;
	
	private Border bordaVerm = new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(3),
			new BorderWidths(2), new Insets(-2)));
	private Border bordaDef;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		bordaDef = txtUsuario.getBorder();
		txtUsuario.textProperty().addListener(new StringList(txtUsuario));
		comboNivel.setItems(FXCollections.observableArrayList(new String[] {"1","2","3"}));
	}
	
	public void construtor(Acesso acesso) {
		this.acesso = acesso;
		if(this.acesso != null) {
			this.acesso = (Acesso) ControllerBd.findById(Acesso.class, acesso.getId());
			setValues();
		}
	}
	
	@FXML
	private void actBtnCancelar() {
		resetTxts();
		((Stage) parentPane.getScene().getWindow()).close();
	}

	@FXML
	private void actBtnSalvar() {
		try {
			boolean flag = true;

			if (acesso == null) {
				acesso = new Acesso();
				flag = false;
			} 
			
			ControllerBd.begin();
			
			if (txtUsuario.getText() == null && txtUsuario.getText().isEmpty() && txtUsuario.getText().isBlank()) {
				Alerts.showError("Campo Nome não pode estar vazio");
				throw new Exception();
			}
			
			acesso.setNome(txtUsuario.getText());
			acesso.setNivel(Integer.parseInt(comboNivel.getSelectionModel().getSelectedItem()));
			if(img.getImage() == null) {
				Alerts.showError("Campo Imagem não pode estar vazio");
				throw new Exception();
			}
			if(imagem != null) {
				acesso.setImagemMat(imagem);
				acesso.setCol(imagem.cols());
				acesso.setRows(imagem.rows());
				acesso.setType(imagem.type());
				acesso.setImagemByte(acesso.getImagemAsByteArr());
			}

			System.out.println(acesso.getNivel());
			if(!flag) ControllerBd.em.persist(acesso);
			ControllerBd.commit();
			
			CRegistro.refreshTableAce();
			resetTxts();
			((Stage) parentPane.getScene().getWindow()).close();
			
		} catch (NumberFormatException e) {
			e.printStackTrace();
		
		} catch (Exception e) {
			Alerts.showError("Falha ao cadastrar Acesso");
			e.printStackTrace();
		}

	}

	@FXML
	private void actBtnSalvarImg() {
		try {
			imagem = opencv_imgcodecs.imread(new FileChooser().showOpenDialog(null).getAbsolutePath(),
					opencv_imgcodecs.IMREAD_GRAYSCALE);
			Mat a = Biometria.processImagem(imagem);
			imagem.close();
			imagem = a.clone();
			Mat temp = new Mat();
			opencv_imgproc.cvtColor(a, temp, opencv_imgproc.COLOR_GRAY2BGR);
			img.setImage(Utilitarios.convertMatToImage(temp));
			temp.close();
			a.close();
		} catch (Exception e) {
			Alerts.showError("Falha ao abrir imagem");
			if (imagem != null || !imagem.empty())
				imagem.close();
			imagem = null;
			e.printStackTrace();
		}
	}
	private void setValues() {
		txtUsuario.setText(acesso.getNome());
		comboNivel.getSelectionModel().select(Integer.toString(acesso.getNivel()));
		acesso.setImagemByByteArr(acesso.getRows(), acesso.getCol(), acesso.getType(), acesso.getImagemByte());
		Mat temp = new Mat();
		opencv_imgproc.cvtColor(acesso.getImagemMat(), temp, opencv_imgproc.COLOR_GRAY2BGR);
		img.setImage(Utilitarios.convertMatToImage(temp));
		temp.close();
	}
	public void setEditavel(boolean bol) {
		txtUsuario.setEditable(bol);
		btnSalvar.setDisable(!bol);
		btnCancelar.setDisable(!bol);
		btnSalvarImg.setDisable(!bol);
		
	}

	public void resetTxts() {
		acesso = null;
		txtUsuario.setText("");
		comboNivel.getSelectionModel().select(0);
		if (imagem != null && imagem.empty())
			imagem.close();
		imagem = null;
		img.setImage(null);
	}

	public void setAcesso(Acesso acesso) {
		this.acesso = acesso;
		txtUsuario.setText(acesso.getNome());
		comboNivel.getSelectionModel().select(Integer.toString(acesso.getNivel()));
	}
	
	protected class StringList implements ChangeListener<String>{
		private TextField f;

		public StringList(TextField f) {
			this.f = f;
		}
		@SuppressWarnings("null")
		@Override
		public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
			if(newValue == null && newValue.isEmpty()) {
				f.setBorder(bordaVerm);
			}else {
				f.setBorder(bordaDef);
			}
		}
	}
}
