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
import com.source.model.TableHelpers;
import com.source.model.TableHelpers.TableAceHelper;
import com.source.model.TableHelpers.TableProHelper;

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

/**
 * Classe que é utilizada como controlador de listeners para interface GUI Registro.fxml*/

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
	
	private Stage frameAcesso = new Stage();
	private CAcesso controlerAcesso;
	
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
			
			root = Aplicacao.listFrameRoot.get("Acesso");
			frameAcesso.setScene(new Scene(root.load(), 360, 360));
			frameAcesso.setResizable(false);
			controlerAcesso = root.getController();
			frameAcesso.setTitle("Cadastro Acesso");

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	public void construtor(Acesso acesso) {
		this.acessoAtual = acesso;
		txtUsuario.setText(acesso.getNome());
		txtNivel.setText(Integer.toString(acesso.getNivel()));
	}

	@FXML
	public void actMenuAdicionar() {
		if (tabPane.getSelectionModel().getSelectedIndex() == 0) {
			controlerCadastro.resetTxt();
			controlerCadastro.construtor(null, acessoAtual.getNivel());
			controlerCadastro.setEditavel(true);
			frameCadastro.show();
			frameCadastro.centerOnScreen();
		} else {
			if(acessoAtual.getNivel() >= 3) {
				controlerAcesso.resetTxts();
				controlerAcesso.construtor(null);
				controlerAcesso.setEditavel(true);
				frameAcesso.show();
				frameAcesso.centerOnScreen();
			}else Alerts.showError("Apenas usuarios nível 3 podem cadastrar acessos");
		}

	}

	@FXML
	public void actMenuExibir() {
		if (tabPane.getSelectionModel().getSelectedIndex() == 0) {
			controlerCadastro.resetTxt();
			controlerCadastro.construtor(tablePro.getSelectionModel().getSelectedItem(), acessoAtual.getNivel());
			controlerCadastro.setEditavel(false);
			frameCadastro.show();
			frameCadastro.centerOnScreen();
		} else {
			if(acessoAtual.getNivel() >= 3) {
				controlerAcesso.resetTxts();
				controlerAcesso.construtor(tableAce.getSelectionModel().getSelectedItem());
				controlerAcesso.setEditavel(false);
				frameAcesso.show();
				frameAcesso.centerOnScreen();
			}else Alerts.showError("Apenas usuarios nível 3 podem Exibir acessos");
		}

	}

	@FXML
	public void actMenuEditar() {
		if (tabPane.getSelectionModel().getSelectedIndex() == 0) {
			controlerCadastro.resetTxt();
			controlerCadastro.construtor(tablePro.getSelectionModel().getSelectedItem(), acessoAtual.getNivel());
			controlerCadastro.setEditavel(true);
			
			frameCadastro.show();
		} else {
			if(acessoAtual.getNivel() >= 3) {
				controlerAcesso.resetTxts();
				controlerAcesso.construtor(tableAce.getSelectionModel().getSelectedItem());
				controlerAcesso.setEditavel(true);
				frameAcesso.show();
				frameAcesso.centerOnScreen();
			}else Alerts.showError("Apenas usuarios nível 3 podem Editar acessos");
		}
	}

	@FXML
	public void actMenuApagar() {
		try {
			if(Alerts.showConfirmation("Deseja apagar linha selecionada")) {
				
				if (tabPane.getSelectionModel().getSelectedIndex() == 0) {
					if(tablePro.getSelectionModel().getSelectedIndex() > -1) {
						Cadastro obj = tablePro.getSelectionModel().getSelectedItem();
						if(ControllerBd.checkPersist(obj)) {
							ControllerBd.delete(obj);
						}else {
							obj = (Cadastro) ControllerBd.findById(Cadastro.class, obj.getId());
							ControllerBd.delete(obj);
							
						}
						refreshTablePro();
					}else Alerts.showError("Nenhuma linha selecionada");

					
				} else {
					if(tableAce.getSelectionModel().getSelectedIndex() > -1) {
						Acesso obj = tableAce.getSelectionModel().getSelectedItem();
						if(ControllerBd.checkPersist(obj)) {
							ControllerBd.delete(obj);
						}else {
							obj = (Acesso) ControllerBd.findById(Acesso.class, obj.getId());
							ControllerBd.delete(obj);
							
						}
						refreshTableAce();
					}else Alerts.showError("Nenhuma linha selecionada");

				}
			}
		} catch (Exception e) {
			Alerts.showError("Não foi possivel apagar valores");
			e.printStackTrace();
		}
	}


	
	public static void refreshTablePro() {
		modelPropriedades.clear();
		ControllerBd.checkTrans();
		@SuppressWarnings("unchecked")
		List<Object[]> l = Aplicacao.em.createQuery("select a.id,a.unidade,a.cidade,a.estado,a.destino from CADASTRO a ").getResultList();
		if(l.size() > 0) 
			for(Object[] c : l) {
				modelPropriedades.add(new Cadastro((int)c[0], (String)c[1], (String)c[4], (String)c[2], (String)c[3]));
			}
	}

	public static void refreshTableAce() {
		modelAcessos.clear();
		ControllerBd.checkTrans();
		@SuppressWarnings("unchecked")
		List<Object[]> l = Aplicacao.em.createQuery("select a.id,a.nivel,a.nome from ACESSO a").getResultList();
		if(l.size() > 0) {
			for(Object[] a : l) {
				 modelAcessos.add(new Acesso((int)a[0], (String)a[2], (int)a[1]));
			}
			
		}
		
		

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
