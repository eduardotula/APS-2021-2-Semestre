package com.view.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.source.Aplicacao;
import com.source.model.table.AcessosModel;
import com.source.model.table.PropriedadesModel;
import com.source.model.table.TableHelpers;
import com.source.model.table.TableHelpers.TableAceHelper;
import com.source.model.table.TableHelpers.TableProHelper;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class CRegistro implements Initializable{

	@FXML public TabPane tabPane;
	@FXML public GridPane parentPane;
	@FXML public MenuItem menuAdicionar;
	@FXML public MenuItem menuExibir;
	@FXML public MenuItem menuEditar;
	@FXML public MenuItem menuApagar;
	@FXML public TableView<PropriedadesModel> tablePro;
	@FXML public TableView<AcessosModel> tableAce;
	@FXML public TextField txtUsuario;
	@FXML public TextField txtNivel;

	private Stage frameCadastro = new Stage();
	
	private ObservableList<PropriedadesModel> modelPropriedades;
	private ObservableList<AcessosModel> modelAcessos;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setTables();
		refreshTableAce();
		refreshTablePro();
		try {
			frameCadastro.setScene(new Scene(Aplicacao.listFrameRoot.get("Cadastro").load(),600,498));
			frameCadastro.setResizable(false);
			frameCadastro.setTitle("Cadastro");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	@FXML
	public void actMenuAdicionar() {
		if (tabPane.getSelectionModel().getSelectedIndex() == 0) {
			//TODO tela de cadastro
			frameCadastro.show();
			frameCadastro.centerOnScreen();
		} else {

		}

	}

	@FXML
	public void actMenuExibir() {
		if (tabPane.getSelectionModel().getSelectedIndex() == 0) {
			//TODO Exibir tela de cadastro não editavel
		} else {

		}

	}

	@FXML
	public void actMenuEditar() {
		if (tabPane.getSelectionModel().getSelectedIndex() == 0) {
			//TODO Exibir tela de cadastro editavel
		} else {

		}
	}

	@FXML
	public void actMenuApagar() {
		if (tabPane.getSelectionModel().getSelectedIndex() == 0) {
			//TODO apagar registro selecionado
		} else {

		}
	}

	public static void refreshTablePro() {

	}

	public static void refreshTableAce() {

	}

	@SuppressWarnings("unchecked")
	private void setTables() {
		TableAceHelper aceH = new TableHelpers.TableAceHelper();
		TableProHelper proH = new TableHelpers.TableProHelper();
		tableAce.getColumns().addAll(aceH.getIdColumn(), aceH.getNivelColumn(), aceH.getNomeColumn());
		tablePro.getColumns().addAll(proH.getIdColumn(), proH.getRazaoColumn(), proH.getEstadoColumn(),
				proH.getNivelColumn(), proH.getRamoColumn());
	}





}
