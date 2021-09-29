package com.source;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import javafx.application.Application;

@SpringBootApplication
public class Aps20212SemestreApplication {

	public static void main(String[] args) {
		
		Application.launch(ChartApplication.class, args); //Inicia a Aplicação
		nu.pattern.OpenCV.loadLocally();
	}

}
