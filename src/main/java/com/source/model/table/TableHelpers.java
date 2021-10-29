package com.source.model.table;

import com.source.model.Acesso;
import com.source.model.Cadastro;

import javafx.scene.control.Control;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Classe auxilar na criação das tabelas CRegistro.tablePro e CRegistro.tableAce*/
public class TableHelpers {
	
	
	public static class TableAceHelper {
		
		public TableColumn<Acesso, Integer> getIdColumn(){
			TableColumn<Acesso, Integer> col = new TableColumn<Acesso, Integer>("Id");
			col.setCellValueFactory(new PropertyValueFactory<Acesso, Integer>("id"));
			col.setMinWidth(60);
			return col;
		}
		public TableColumn<Acesso, String> getNomeColumn(){
			TableColumn<Acesso, String> col = new TableColumn<Acesso, String>("Nome");
			col.setCellValueFactory(new PropertyValueFactory<Acesso, String>("nome"));
			col.setMinWidth(100);
			col.setPrefWidth(Control.USE_COMPUTED_SIZE);
			col.setPrefWidth(20000);
			return col;
		}
		public TableColumn<Acesso, String> getNivelColumn(){
			TableColumn<Acesso, String> col = new TableColumn<Acesso, String>("Nível de Acesso");
			col.setCellValueFactory(new PropertyValueFactory<Acesso, String>("nivel"));
			col.setMinWidth(70);
			return col;
		}
		
	}

	public static class TableProHelper {

		public TableColumn<Cadastro, Integer> getIdColumn(){
			TableColumn<Cadastro, Integer> col = new TableColumn<Cadastro, Integer>("Id");
			col.setCellValueFactory(new PropertyValueFactory<Cadastro, Integer>("id"));
			col.setMinWidth(60);
			return col;
		}
		public TableColumn<Cadastro, String> getRazaoColumn(){
			TableColumn<Cadastro, String> col = new TableColumn<Cadastro, String>("Nome/Razão Social");
			col.setCellValueFactory(new PropertyValueFactory<Cadastro, String>("unidade"));
			col.setMinWidth(100);
			col.setPrefWidth(Control.USE_COMPUTED_SIZE);
			col.setMaxWidth(20000);

			return col;
		}
		public TableColumn<Cadastro, String> getEstadoColumn(){
			TableColumn<Cadastro, String> col = new TableColumn<Cadastro, String>("Cidade");
			col.setCellValueFactory(new PropertyValueFactory<Cadastro, String>("cidade"));
			col.setMinWidth(60);
			return col;
		}
		public TableColumn<Cadastro, String> getNivelColumn(){
			TableColumn<Cadastro, String> col = new TableColumn<Cadastro, String>("Estado");
			col.setCellValueFactory(new PropertyValueFactory<Cadastro, String>("estado"));
			col.setMinWidth(50);
			return col;
		}
		public TableColumn<Cadastro, String> getRamoColumn(){
			TableColumn<Cadastro, String> col = new TableColumn<Cadastro, String>("Destino");
			col.setCellValueFactory(new PropertyValueFactory<Cadastro, String>("destino"));
			col.setMinWidth(150);
			return col;
		}
	}

}




