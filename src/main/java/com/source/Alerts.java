package com.source;


import java.util.Optional;
import java.util.function.Consumer;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;

/**
 * Classe para facilitar a exibição e retorno de informações, todos os metodos são estaticos e podem ser utilizados em qualquer classe
 * desde que o thread responsavel por chamar estes metodos sejam threads de aplicação JavaFX*/

public class Alerts {
	
	private static Boolean b;
	
	/**
	 *Exibe na tela uma informação de erro
	 *@param mensaegm mensagem a ser exibida */
	public static void showError(String mensagem) {
		try {
			Alert a = new Alert(AlertType.ERROR, mensagem);
			a.setHeaderText(null);
			a.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Exibe na tela uma informação
	 * @param mensaem mensagem a ser exibida*/
	public static void showInformation(String mensagem) {
		try {
			Alert a = new Alert(AlertType.INFORMATION, mensagem);
			a.setHeaderText(null);
			a.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Exibe na tela uma mensagem com duas opções de OK ou CANCEL
	 * @param mensagem mensagem a ser exibida
	 * @return Boolean true ou false*/
	public static Boolean showConfirmation(String mensagem) {
		try {
			b = null;
			Alert a = new Alert(AlertType.CONFIRMATION,mensagem);
			a.setHeaderText(null);
			Optional<ButtonType> ob = a.showAndWait();
			ob.ifPresent(new Consumer<ButtonType>() {

				@Override
				public void accept(ButtonType t) {
					if(t == ButtonType.OK) b = true;
					else b = false;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}
	
	/**
	 * Exibe uma tela para usuario inserir dados em um TextField
	 * @param mensagem mensaegm á ser exibida
	 * @param valor caso != null valor será inserido no campo de input
	 * @return valor inserido*/
	public static String showInput(String mensagem, String valor) {
		try {
			if(valor == null) valor = "";
			TextInputDialog a = new TextInputDialog(valor);
			a.setHeaderText(null);
			a.setContentText(mensagem);
			Optional<String> ob = a.showAndWait();
			ob.orElse(null);
			return ob.get();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
}
