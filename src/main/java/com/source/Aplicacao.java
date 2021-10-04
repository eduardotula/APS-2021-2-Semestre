package com.source;

import java.io.File;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ConfigurableApplicationContext;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Aplicacao extends Application {
	private ConfigurableApplicationContext applicationContext;
	public static Stage stage;
	
	@Override
	public void init() throws Exception {
		super.init();
		applicationContext = new SpringApplicationBuilder(Aps20212SemestreApplication.class).run();
	}

	@Override
	public void start(Stage stage) throws Exception {
		applicationContext.publishEvent(new StageReadyEvent(stage));
		Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("models/MainFrame.fxml"));
		Aplicacao.stage = stage;
		stage.setTitle("pepega");
		stage.setScene(new Scene(root,600,500));
		
		stage.show();
		
	}
	
	

	@Override
	public void stop() throws Exception {
		super.stop();
		applicationContext.close();
	}



	public class StageReadyEvent extends ApplicationEvent {

		private static final long serialVersionUID = 1L;

		public StageReadyEvent(Stage stage) {
			super(stage);
		}

	}

}
