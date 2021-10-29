package com.source;


import java.util.Optional;
import java.util.function.Consumer;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;

public class Alerts {
	
	private static Boolean b;
	
	public static void showError(String mensagem) {
		Alert a = new Alert(AlertType.ERROR, mensagem);
		a.setHeaderText(null);
		a.showAndWait();
	}

	public static void showInformation(String mensagem) {
		Alert a = new Alert(AlertType.INFORMATION, mensagem);
		a.setHeaderText(null);
		a.showAndWait();
	}
	public static Boolean showConfirmation(String mensagem) {
		b = null;
		Alert a = new Alert(AlertType.CONFIRMATION);
		a.setHeaderText(null);
		Optional<ButtonType> ob = a.showAndWait();
		ob.ifPresent(new Consumer<ButtonType>() {

			@Override
			public void accept(ButtonType t) {
				if(t == ButtonType.OK) b = true;
				else b = false;
			}
		});
		return b;
	}
	
	public static String showInput(String mensagem, String valor) {
		if(valor == null) valor = "";
		TextInputDialog a = new TextInputDialog(valor);
		a.setHeaderText(null);
		a.setContentText(mensagem);
		Optional<String> ob = a.showAndWait();
		ob.orElse(null);
		return ob.get();
		
	}
}
