package com.source;


import java.io.File;
import java.net.URL;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ConfigurableApplicationContext;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Aplicacao extends Application {
	public static ConfigurableApplicationContext applicationContext;
	public static Stage stage;
	@Override
	public void init() throws Exception {
		super.init();
		applicationContext = new SpringApplicationBuilder(Aps20212SemestreApplication.class).run();
	}

	@Override
	public void start(Stage stage){
		
		try {
			applicationContext.publishEvent(new StageReadyEvent(stage));
			
			Parent root = FXMLLoader.load(Thread.currentThread().getContextClassLoader().getResource("com/view/models/login.fxml"));
			Aplicacao.stage = stage;
			stage.setTitle("pepega");
			stage.setScene(new Scene(root,600,500));
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	

	@Override
	public void stop() throws Exception {
		super.stop();
		applicationContext.close();
		System.exit(0);
	}



	public class StageReadyEvent extends ApplicationEvent {

		private static final long serialVersionUID = 1L;

		public StageReadyEvent(Stage stage) {
			super(stage);
		}

	}

}
