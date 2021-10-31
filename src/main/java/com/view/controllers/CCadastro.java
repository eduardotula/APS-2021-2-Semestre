package com.view.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.source.Alerts;
import com.source.control.ControllerBd;
import com.source.model.Agrotoxico;
import com.source.model.AgrotoxicoProibi;
import com.source.model.Cadastro;
import com.source.model.TableHelpers;
import com.source.model.TableHelpers.TableAgroHelper;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Classe que é utilizada como controlador de listeners para interface GUI Cadastro.fxml*/
public class CCadastro implements Initializable {

	@FXML
	private GridPane parentPane;
	@FXML
	private TextField txtUnidade;
	@FXML
	private TextField txtProdAnual;
	@FXML
	private TextField txtNEmpregados;
	@FXML
	private ComboBox<String> comboDestino;
	
	@FXML
	private TextField txtNivelAuto;
	@FXML
	private TextField txtQMaquinas;

	@FXML
	private TextField txtCidade;
	@FXML
	private TextField txtCep;
	@FXML
	private TextField txtEndereco;
	@FXML
	private TextField txtPais;
	@FXML
	private TextField txtEstado;
	@FXML
	private Button btnSalvar;
	@FXML
	private Button btnCancelar;
	@FXML
	private Button btnBanir;
	@FXML
	private Button btnPermitir;
	@FXML
	private TextField txtFiscaisRece;
	@FXML
	private TextField txtMuniPagos;
	@FXML
	private TextField txtEstaReco;
	@FXML
	private TextField txtFedePagos;
	@FXML
	private TextField txtTaxasFed;
	@FXML
	private TextField txtAgro;
	@FXML
	private Button btnRemover;
	@FXML
	private Button btnAdicionar;
	@FXML
	private TableView<Agrotoxico> listAgro;
	private ObservableList<Agrotoxico> agroModel = FXCollections.observableArrayList();

	//TODO agrotoxicos
	
	private Cadastro c;
	private Integer nivel;

	private Border bordaVerm = new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(3),
			new BorderWidths(2), new Insets(-2)));
	private Border bordaDef;

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		bordaDef = txtUnidade.getBorder();
		setBordas();
		listAgro.setItems(agroModel);
		//Adiciona colunas na tabela
		TableAgroHelper h = new TableHelpers.TableAgroHelper();
		listAgro.getColumns().addAll(h.getColumAgro(),h.getColumnProib());
		

	}
	public void construtor(Cadastro cadastro, Integer nivel) {
		this.c = cadastro;
		this.nivel = nivel;
		comboDestino.setItems(FXCollections.observableArrayList(new String[] {"Externo", "Interno"}));
		

		ControllerBd.begin();
		if(c != null) {
			c = (Cadastro) ControllerBd.findById(Cadastro.class, c.getId());
			setValues();
		}else {
			c = new Cadastro();
		}
	}


	@FXML
	public void actBtnSalvar() {
		try {
			boolean flag = true;
			
			if(c.getId() == 0) { flag = false;}
			
			c.setUnidade(txtUnidade.getText());
			c.setProdAnual(Double.parseDouble(txtProdAnual.getText()));
			c.setnEmpregados(Integer.parseInt(txtNEmpregados.getText()));
			c.setDestino(comboDestino.getValue());
			c.setNivelAuto(Integer.parseInt(txtNivelAuto.getText()));
			c.setQantiMaquinas(Integer.parseInt(txtQMaquinas.getText()));
			c.setCidade(txtCidade.getText());
			c.setCep(txtCep.getText());
			c.setEndereco(txtEndereco.getText());
			c.setPais(txtPais.getText());
			c.setEstado(txtEstado.getText());
			if(nivel >= 2) {
				c.setInceFiscaRece(Double.parseDouble(txtFiscaisRece.getText()));
				c.setImpMuniPagos(Double.parseDouble(txtMuniPagos.getText()));
				c.setImpEstaduRecolhidos(Double.parseDouble(txtEstaReco.getText()));
				c.setImpFedPago(Double.parseDouble(txtFedePagos.getText()));
				c.setTaxasFed(Double.parseDouble(txtTaxasFed.getText()));
			}else {
				c.setInceFiscaRece(0.0);
				c.setImpMuniPagos(0.0);
				c.setImpEstaduRecolhidos(0.0);
				c.setImpFedPago(0.0);
				c.setTaxasFed(0.0);
			}
			
			if(!flag) ControllerBd.em.persist(c);  
			else ControllerBd.em.merge(c);
			
			ControllerBd.commit();
			
			
			CRegistro.refreshTablePro();
			c = null;
			resetTxt();
			((Stage) parentPane.getScene().getWindow()).close();
		} catch (Exception e) {
			e.printStackTrace();
			Alerts.showError("Falha ao inserir dados, cheque os campos");
		}
	}

	@FXML
	public void actBtnCancelar() {
		resetTxt();
		ControllerBd.checkTrans();
		((Stage) parentPane.getScene().getWindow()).close();
		
	}

	@FXML
	public void actBtnRemover() {
		int index = listAgro.getSelectionModel().getSelectedIndex();
		if(index > -1) {
			Agrotoxico agro = c.getAgrotoxicos().get(index);
			agroModel.remove(agro);
			c.removeAgrotoxico(agro);
			c.checkProibido();
		}
	}

	@FXML
	public void actBtnBanir() {
		int row = listAgro.getSelectionModel().getSelectedIndex();
		if(row > -1) {
			Agrotoxico agro = agroModel.get(row);
			AgrotoxicoProibi proib = ControllerBd.findAgroProib(agro.getAgrotoxico());
			if(proib == null) {
				AgrotoxicoProibi pro = new AgrotoxicoProibi(null, agro.getAgrotoxico());
				ControllerBd.em.persist(pro);
				agro.setProibido(true);
				c.setContemProibido(true);
				listAgro.refresh();
			}
		}
	}
	@FXML
	public void actBtnPermitir() {
		int row = listAgro.getSelectionModel().getSelectedIndex();
		if(row > -1) {
			Agrotoxico agro = agroModel.get(row);
			AgrotoxicoProibi proib = ControllerBd.findAgroProib(agro.getAgrotoxico());
			if(proib != null) {
				ControllerBd.em.remove(proib);
				agro.setProibido(false);
				c.checkProibido();
				listAgro.refresh();
			}

		}
	}
	
	@FXML
	public void actBtnAdicionar() {
		try {
			String agro = txtAgro.getText();
			if(!agro.isEmpty()) {
				AgrotoxicoProibi proibido = ControllerBd.findAgroProib(agro);
				boolean pr = false;
				if(proibido != null) pr = true;
				Agrotoxico a = new Agrotoxico(null, agro,pr,c);
				ControllerBd.em.persist(a);
				c.addAgrotoxico(a);
				c.setContemProibido(false);
				agroModel.add(a);
				c.checkProibido();
				txtAgro.clear();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void resetTxt() {
		txtUnidade.setText("");
		txtProdAnual.setText("");
		txtNEmpregados.setText("");
		comboDestino.getSelectionModel().select(0);
		txtNivelAuto.setText("");
		txtQMaquinas.setText("");
		txtCidade.setText("");
		txtCep.setText("");
		txtEndereco.setText("");
		txtPais.setText("");
		txtEstado.setText("");
		txtFiscaisRece.setText("");
		txtMuniPagos.setText("");
		txtEstaReco.setText("");
		txtFedePagos.setText("");
		txtTaxasFed.setText("");
		nivel = null;
		c = null;
		agroModel.clear();
		txtAgro.clear();
		listAgro.refresh();
	}

	private void setValues() {
		txtUnidade.setText(c.getUnidade());
		txtProdAnual.setText(c.getProdAnualStr());
		txtNEmpregados.setText(c.getnEmpregadosStr());
		comboDestino.getSelectionModel().select(c.getDestino());
		txtNivelAuto.setText(c.getNivelAutoStr());
		txtQMaquinas.setText(c.getQantiMaquinasStr());
		txtCidade.setText(c.getCidade());
		txtCep.setText(c.getCep());
		txtEndereco.setText(c.getEndereco());
		txtPais.setText(c.getPais());
		txtEstado.setText(c.getEstado());
		if(nivel >= 2) {
			txtFiscaisRece.setText(c.getInceFiscaReceStr());
			txtMuniPagos.setText(c.getImpMuniPagosStr());
			txtEstaReco.setText(c.getImpEstaduRecolhidosStr());
			txtFedePagos.setText(c.getImpFedPagoStr());
			txtTaxasFed.setText(c.getTaxasFedStr());
		}
		if(nivel >= 3) {
			agroModel.addAll(c.getAgrotoxicos());
		}

		
		
	}
	public void setEditavel(boolean bol) {
		txtUnidade.setEditable(bol);
		txtProdAnual.setEditable(bol);
		txtNEmpregados.setEditable(bol);
		txtNivelAuto.setEditable(bol);
		txtQMaquinas.setEditable(bol);
		txtCidade.setEditable(bol);
		txtCep.setEditable(bol);
		txtEndereco.setEditable(bol);
		txtPais.setEditable(bol);
		txtEstado.setEditable(bol);
		if(bol && nivel >=2) {
			txtFiscaisRece.setEditable(bol);
			txtMuniPagos.setEditable(bol);
			txtEstaReco.setEditable(bol);
			txtFedePagos.setEditable(bol);
			txtTaxasFed.setEditable(bol);
		}else {
			txtFiscaisRece.setEditable(false);
			txtMuniPagos.setEditable(false);
			txtEstaReco.setEditable(false);
			txtFedePagos.setEditable(false);
			txtTaxasFed.setEditable(false);
		}
		if(bol && nivel >= 3) {
			btnRemover.setDisable(false);
			btnAdicionar.setDisable(false);
		}else {
			btnRemover.setDisable(true);
			btnAdicionar.setDisable(true);
		}
		btnCancelar.setDisable(!bol);
		btnSalvar.setDisable(!bol);
	}

	private void setBordas() {
		// Bordas customizadas para checar valores que não são double ou int ou string
		txtUnidade.textProperty().addListener(new StringList(txtUnidade));
		txtCidade.textProperty().addListener(new StringList(txtCidade));
		txtCep.textProperty().addListener(new StringList(txtCep));
		txtEndereco.textProperty().addListener(new StringList(txtEndereco));
		txtPais.textProperty().addListener(new StringList(txtPais));
		txtEstado.textProperty().addListener(new StringList(txtEstado));
		txtProdAnual.textProperty().addListener(new DoubleList(txtProdAnual));
		txtNEmpregados.textProperty().addListener(new IntList(txtNEmpregados));
		txtNivelAuto.textProperty().addListener(new IntList(txtNivelAuto));
		txtQMaquinas.textProperty().addListener(new IntList(txtQMaquinas));
		txtFiscaisRece.textProperty().addListener(new DoubleList(txtFiscaisRece));
		txtMuniPagos.textProperty().addListener(new DoubleList(txtMuniPagos));
		txtEstaReco.textProperty().addListener(new DoubleList(txtEstaReco));
		txtFedePagos.textProperty().addListener(new DoubleList(txtFedePagos));
		txtTaxasFed.textProperty().addListener(new DoubleList(txtTaxasFed));
	}

	protected class StringList implements ChangeListener<String>{
		private TextField f;

		public StringList(TextField f) {
			this.f = f;
		}
		@Override
		public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
			if(newValue == null || newValue.isEmpty()) {
				f.setBorder(bordaVerm);
			}else {
				f.setBorder(bordaDef);
			}
		}
	}
	protected class DoubleList implements ChangeListener<String> {

		private TextField f;

		public DoubleList(TextField f) {
			this.f = f;
		}

		@Override
		public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
			try {
				Double.parseDouble(newValue);
				f.setBorder(bordaDef);
			} catch (NumberFormatException e) {
				f.setBorder(bordaVerm);
			}
		}

	}

	protected class IntList implements ChangeListener<String> {

		private TextField f;

		public IntList(TextField f) {
			this.f = f;
		}

		@Override
		public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
			try {
				if(newValue.matches("\\d+")) f.setBorder(bordaDef);
				else {
					f.setBorder(bordaVerm);
				}
			} catch (NumberFormatException e) {
				f.setBorder(bordaVerm);
			}
		}

	}

}
