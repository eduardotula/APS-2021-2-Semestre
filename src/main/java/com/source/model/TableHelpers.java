package com.source.model;

import javafx.scene.control.Control;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;

/**
 * Classe auxilar na criação das tabelas CRegistro.tablePro e CRegistro.tableAce
 */
public class TableHelpers {

	public static class TableAceHelper {

		public TableColumn<Acesso, Integer> getIdColumn() {
			TableColumn<Acesso, Integer> col = new TableColumn<Acesso, Integer>("Id");
			col.setCellValueFactory(new PropertyValueFactory<Acesso, Integer>("id"));
			col.setMinWidth(60);
			return col;
		}

		public TableColumn<Acesso, String> getNomeColumn() {
			TableColumn<Acesso, String> col = new TableColumn<Acesso, String>("Nome");
			col.setCellValueFactory(new PropertyValueFactory<Acesso, String>("nome"));
			col.setMinWidth(100);
			col.setPrefWidth(Control.USE_COMPUTED_SIZE);
			col.setPrefWidth(20000);
			return col;
		}

		public TableColumn<Acesso, String> getNivelColumn() {
			TableColumn<Acesso, String> col = new TableColumn<Acesso, String>("Nível de Acesso");
			col.setCellValueFactory(new PropertyValueFactory<Acesso, String>("nivel"));
			col.setMinWidth(70);
			return col;
		}

	}

	public static class TableProHelper {
		
		private String iconMorte = getClass().getResource("/morte.png").toString();
		private String iconSafe = getClass().getResource("/confirm.png").toString();

		public TableColumn<Cadastro, Integer> getIdColumn() {
			TableColumn<Cadastro, Integer> col = new TableColumn<Cadastro, Integer>("Id");
			col.setCellValueFactory(new PropertyValueFactory<Cadastro, Integer>("id"));
			col.setMinWidth(60);
			return col;
		}

		public TableColumn<Cadastro, String> getRazaoColumn() {
			TableColumn<Cadastro, String> col = new TableColumn<Cadastro, String>("Nome/Razão Social");
			col.setCellValueFactory(new PropertyValueFactory<Cadastro, String>("unidade"));
			col.setMinWidth(100);
			col.setPrefWidth(Control.USE_COMPUTED_SIZE);
			col.setMaxWidth(20000);

			return col;
		}

		public TableColumn<Cadastro, String> getEstadoColumn() {
			TableColumn<Cadastro, String> col = new TableColumn<Cadastro, String>("Cidade");
			col.setCellValueFactory(new PropertyValueFactory<Cadastro, String>("cidade"));
			col.setMinWidth(60);
			return col;
		}

		public TableColumn<Cadastro, String> getNivelColumn() {
			TableColumn<Cadastro, String> col = new TableColumn<Cadastro, String>("Estado");
			col.setCellValueFactory(new PropertyValueFactory<Cadastro, String>("estado"));
			col.setMinWidth(50);
			return col;
		}

		public TableColumn<Cadastro, String> getRamoColumn() {
			TableColumn<Cadastro, String> col = new TableColumn<Cadastro, String>("Destino");
			col.setCellValueFactory(new PropertyValueFactory<Cadastro, String>("destino"));
			col.setMinWidth(150);
			return col;
		}
		public TableColumn<Cadastro, Boolean> getSafeColumn() {
			TableColumn<Cadastro, Boolean> col = new TableColumn<Cadastro, Boolean>("Status");
			col.setCellValueFactory(new PropertyValueFactory<Cadastro, Boolean>("contemProibido"));
			col.setMinWidth(40);
			col.setCellFactory(new Callback<TableColumn<Cadastro,Boolean>, TableCell<Cadastro,Boolean>>() {
				
				@Override
				public TableCell<Cadastro, Boolean> call(TableColumn<Cadastro, Boolean> param) {
					return new TableCell<Cadastro, Boolean>(){
						@Override
						protected void updateItem(Boolean item, boolean empty) {
							super.updateItem(item, empty);
							if(!empty) {
								if(item != null) {
									if(item){
										setGraphic(new StackPane(new ImageView(iconMorte)));
									}else {
										setGraphic(new StackPane(new ImageView(iconSafe)));
									}
								}
							}else setGraphic(null);

						}
					};
				}
			});
			
			return col;
		}
		
	}
	
	public static class TableAgroHelper{
		
		private String iconMorte = getClass().getResource("/morte.png").toString();
		private String iconSafe = getClass().getResource("/confirm.png").toString();
		
		
		public TableColumn<Agrotoxico, String> getColumAgro() {
			TableColumn<Agrotoxico, String> col = new TableColumn<Agrotoxico, String>("Agrotoxico");
			col.setCellValueFactory(new PropertyValueFactory<Agrotoxico, String>("agrotoxico"));
			col.setMinWidth(210);
			return col;
		}
		public TableColumn<Agrotoxico, Boolean> getColumnProib() {
			TableColumn<Agrotoxico, Boolean> col = new TableColumn<Agrotoxico, Boolean>("Liberado");
			col.setCellValueFactory(new PropertyValueFactory<Agrotoxico, Boolean>("proibido"));
			col.setMinWidth(60);
			col.setCellFactory(new Callback<TableColumn<Agrotoxico,Boolean>, TableCell<Agrotoxico,Boolean>>() {
				@Override
				public TableCell<Agrotoxico, Boolean> call(TableColumn<Agrotoxico, Boolean> param) {
					return new TableCell<Agrotoxico, Boolean>(){
						@Override
						protected void updateItem(Boolean item, boolean empty) {
							super.updateItem(item, empty);
							if(!empty) {
								if(item != null) {
									if(item){
										setGraphic(new StackPane(new ImageView(iconMorte)));
									}else {
										setGraphic(new StackPane(new ImageView(iconSafe)));
									}
								}
							}else setGraphic(null);

						}
					};
				}
			});
			return col;
		}
	}



}
