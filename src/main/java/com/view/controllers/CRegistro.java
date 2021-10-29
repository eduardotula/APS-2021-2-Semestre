package com.view.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.source.Alerts;
import com.source.Aplicacao;
import com.source.control.ControllerBd;
import com.source.model.Acesso;
import com.source.model.Cadastro;
import com.source.model.table.TableHelpers;
import com.source.model.table.TableHelpers.TableAceHelper;
import com.source.model.table.TableHelpers.TableProHelper;

import javafx.application.Platform;
import javafx.collections.FXCollections;
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

public class CRegistro implements Initializable {

	@FXML
	public TabPane tabPane;
	@FXML
	public GridPane parentPane;
	@FXML
	public MenuItem menuAdicionar;
	@FXML
	public MenuItem menuExibir;
	@FXML
	public MenuItem menuEditar;
	@FXML
	public MenuItem menuApagar;
	@FXML
	public TableView<Cadastro> tablePro;
	@FXML
	public TableView<Acesso> tableAce;
	@FXML
	public TextField txtUsuario;
	@FXML
	public TextField txtNivel;

	private Acesso acessoAtual;
	private Stage frameCadastro = new Stage();
	private CCadastro controlerCadastro;

	private static ObservableList<Cadastro> modelPropriedades = FXCollections.observableArrayList();
	private static ObservableList<Acesso> modelAcessos = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Aplicacao.primaryStage.setOnCloseRequest(event -> {
			Platform.exit();
		});
		Aplicacao.primaryStage.centerOnScreen();
		setTables();
		refreshTableAce();
		refreshTablePro();

		try {
			FXMLLoader root = Aplicacao.listFrameRoot.get("Cadastro");
			
			frameCadastro.setScene(new Scene(root.load(), 600, 600));
			frameCadastro.setResizable(false);
			controlerCadastro = root.getController();
			frameCadastro.setTitle("Cadastro");

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@FXML
	public void actMenuAdicionar() {
		if (tabPane.getSelectionModel().getSelectedIndex() == 0) {
			// TODO tela de cadastro
			controlerCadastro.setCadastro(null, 1);
			frameCadastro.show();
			frameCadastro.centerOnScreen();
		} else {

		}

	}

	@FXML
	public void actMenuExibir() {
		if (tabPane.getSelectionModel().getSelectedIndex() == 0) {
			// TODO Exibir tela de cadastro não editavel
			controlerCadastro.setEditavel(false);
			controlerCadastro.setCadastro(tablePro.getSelectionModel().getSelectedItem(), acessoAtual.getNivel());
			frameCadastro.show();
		} else {

		}

	}

	@FXML
	public void actMenuEditar() {
		if (tabPane.getSelectionModel().getSelectedIndex() == 0) {
			// TODO Exibir tela de cadastro editavel
			controlerCadastro.setEditavel(true);
			controlerCadastro.setCadastro(tablePro.getSelectionModel().getSelectedItem(), acessoAtual.getNivel());
			frameCadastro.show();
		} else {

		}
	}

	@FXML
	public void actMenuApagar() {
		try {
			if(Alerts.showConfirmation("Deseja apagar linha selecionada")) {
				
				if (tabPane.getSelectionModel().getSelectedIndex() == 0) {
					Cadastro obj = tablePro.getSelectionModel().getSelectedItem();
					if(ControllerBd.checkPersist(obj)) {
						ControllerBd.delete(obj);
					}else {
						obj = (Cadastro) ControllerBd.findById(Cadastro.class, obj.getId());
						ControllerBd.delete(obj);
					}
					
				} else {
					Acesso obj = tableAce.getSelectionModel().getSelectedItem();
					if(ControllerBd.checkPersist(obj)) {
						ControllerBd.delete(obj);
					}else {
						obj = (Acesso) ControllerBd.findById(Acesso.class, obj.getId());
						ControllerBd.delete(obj);
					}
				}
			}
		} catch (Exception e) {
			Alerts.showError("Não foi possivel apagar valores");
			e.printStackTrace();
		}
	}

	public static void refreshTablePro() {
		List<Cadastro> l = Aplicacao.em.createQuery("select a from CADASTRO a",Cadastro.class).getResultList();
		if(l.size() > 0) modelPropriedades.addAll(l);
	}

	public static void refreshTableAce() {
		List<Acesso> l = Aplicacao.em.createQuery("select a from ACESSO a",Acesso.class).getResultList();
		if(l.size() > 0) modelAcessos.addAll(l);
		
		

	}

	@SuppressWarnings("unchecked")
	private void setTables() {
		tableAce.setItems(modelAcessos);
		tablePro.setItems(modelPropriedades);
		TableAceHelper aceH = new TableHelpers.TableAceHelper();
		TableProHelper proH = new TableHelpers.TableProHelper();
		tableAce.getColumns().addAll(aceH.getIdColumn(), aceH.getNivelColumn(), aceH.getNomeColumn());
		tablePro.getColumns().addAll(proH.getIdColumn(), proH.getRazaoColumn(), proH.getEstadoColumn(),
				proH.getNivelColumn(), proH.getRamoColumn());

	}

}
