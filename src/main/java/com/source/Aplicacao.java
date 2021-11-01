package com.source;


import java.util.HashMap;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ConfigurableApplicationContext;


import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Em conjunto com a classe Aps20212SemestreApplication essas classes são responsaveis por carregar recursos e inicia a aplicação
 * Esta classe contem Stage primaryStage e objetos para operações CRUD com banco de dados EntityManager e EntittyManagerFactory*/

public class Aplicacao extends Application {
	public static ConfigurableApplicationContext applicationContext;
	public static HashMap<String, FXMLLoader> listFrameRoot = new HashMap<String, FXMLLoader>();
	private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("aps");
	public static EntityManager em = emf.createEntityManager();
	public static Stage primaryStage;
	
	@Override
	public void init() throws Exception {
		super.init();
		applicationContext = new SpringApplicationBuilder(Aps20212SemestreApplication.class).run();

		
	}

	@Override
	public void start(Stage stage){
		
		try {
			applicationContext.publishEvent(new StageReadyEvent(stage));
			primaryStage = stage;
			listFrameRoot.put("Login",new FXMLLoader(Thread.currentThread().getContextClassLoader().getResource("com/view/models/Login.fxml")));
			listFrameRoot.put("Cadastro",new FXMLLoader(Thread.currentThread().getContextClassLoader().getResource("com/view/models/Cadastro.fxml")));
			listFrameRoot.put("Registro",new FXMLLoader(Thread.currentThread().getContextClassLoader().getResource("com/view/models/Registro.fxml")));
			listFrameRoot.put("Acesso",new FXMLLoader(Thread.currentThread().getContextClassLoader().getResource("com/view/models/Acesso.fxml")));
			listFrameRoot.put("teste",new FXMLLoader(Thread.currentThread().getContextClassLoader().getResource("com/view/models/teste.fxml")));
			stage.setScene(new Scene(listFrameRoot.get("Login").load(),460,487));
			stage.setResizable(false);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	

	@Override
	public void stop() throws Exception {
		super.stop();
		applicationContext.close();
		Platform.exit();
		System.exit(0);
	}
	



	public class StageReadyEvent extends ApplicationEvent {

		private static final long serialVersionUID = 1L;

		public StageReadyEvent(Stage stage) {
			super(stage);
		}

	}

}
