package com.view.controllers;

import com.source.model.AcessosModel;
import com.source.model.PropriedadesModel;
import com.source.model.TableHelpers;
import com.source.model.TableHelpers.TableAceHelper;
import com.source.model.TableHelpers.TableProHelper;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class CRegistro {

	@FXML
	public TabPane tabPane;
	@FXML
	public GridPane gridPane;
	@FXML
	public MenuItem menuAdicionar;
	@FXML
	public MenuItem menuExibir;
	@FXML
	public MenuItem menuEditar;
	@FXML
	public MenuItem menuApagar;
	@FXML
	public TableView<PropriedadesModel> tablePro;
	@FXML
	public TableView<AcessosModel> tableAce;
	@FXML
	public TextField txtUsuario;
	@FXML
	public TextField txtNivel;

	private ObservableList<PropriedadesModel> modelPropriedades;
	private ObservableList<AcessosModel> modelAcessos;

	public CRegistro() {
		refreshTableAce();
		refreshTablePro();
		setTables();
		tablePro.setItems(modelPropriedades);
		tableAce.setItems(modelAcessos);
	}

	@FXML
	public void actMenuAdicionar() {
		if (tabPane.getSelectionModel().getSelectedIndex() == 0) {
			
		} else {

		}

	}

	@FXML
	public void actMenuExibir() {
		if (tabPane.getSelectionModel().getSelectedIndex() == 0) {

		} else {

		}

	}

	@FXML
	public void actMenuEditar() {
		if (tabPane.getSelectionModel().getSelectedIndex() == 0) {

		} else {

		}
	}

	@FXML
	public void actMenuApagar() {
		if (tabPane.getSelectionModel().getSelectedIndex() == 0) {

		} else {

		}
	}

	private void refreshTablePro() {

	}

	private void refreshTableAce() {

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
