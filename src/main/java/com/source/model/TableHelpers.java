package com.source.model;

import javafx.scene.control.Control;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

public class TableHelpers {
	
	
	public static class TableAceHelper {
		
		public TableColumn<AcessosModel, Integer> getIdColumn(){
			TableColumn<AcessosModel, Integer> col = new TableColumn<AcessosModel, Integer>("Id");
			col.setCellValueFactory(new PropertyValueFactory<AcessosModel, Integer>("id"));
			col.setMinWidth(60);
			return col;
		}
		public TableColumn<AcessosModel, String> getNomeColumn(){
			TableColumn<AcessosModel, String> col = new TableColumn<AcessosModel, String>("Nome");
			col.setCellValueFactory(new PropertyValueFactory<AcessosModel, String>("nome"));
			col.setMinWidth(100);
			col.setPrefWidth(Control.USE_COMPUTED_SIZE);
			col.setPrefWidth(20000);
			return col;
		}
		public TableColumn<AcessosModel, String> getNivelColumn(){
			TableColumn<AcessosModel, String> col = new TableColumn<AcessosModel, String>("Nível de Acesso");
			col.setCellValueFactory(new PropertyValueFactory<AcessosModel, String>("nivel"));
			col.setMinWidth(70);
			return col;
		}
		
	}

	public static class TableProHelper {

		public TableColumn<PropriedadesModel, Integer> getIdColumn(){
			TableColumn<PropriedadesModel, Integer> col = new TableColumn<PropriedadesModel, Integer>("Id");
			col.setCellValueFactory(new PropertyValueFactory<PropriedadesModel, Integer>("id"));
			col.setMinWidth(60);
			return col;
		}
		public TableColumn<PropriedadesModel, String> getRazaoColumn(){
			TableColumn<PropriedadesModel, String> col = new TableColumn<PropriedadesModel, String>("Nome/Razão Social");
			col.setCellValueFactory(new PropertyValueFactory<PropriedadesModel, String>("razao"));
			col.setMinWidth(100);
			col.setPrefWidth(Control.USE_COMPUTED_SIZE);
			col.setMaxWidth(20000);

			return col;
		}
		public TableColumn<PropriedadesModel, String> getEstadoColumn(){
			TableColumn<PropriedadesModel, String> col = new TableColumn<PropriedadesModel, String>("Estado");
			col.setCellValueFactory(new PropertyValueFactory<PropriedadesModel, String>("estado"));
			col.setMinWidth(60);
			return col;
		}
		public TableColumn<PropriedadesModel, String> getNivelColumn(){
			TableColumn<PropriedadesModel, String> col = new TableColumn<PropriedadesModel, String>("Nível");
			col.setCellValueFactory(new PropertyValueFactory<PropriedadesModel, String>("nivel"));
			col.setMinWidth(50);
			return col;
		}
		public TableColumn<PropriedadesModel, String> getRamoColumn(){
			TableColumn<PropriedadesModel, String> col = new TableColumn<PropriedadesModel, String>("Ramo de Atividade");
			col.setCellValueFactory(new PropertyValueFactory<PropriedadesModel, String>("ramo"));
			col.setMinWidth(150);
			return col;
		}
	}

}




