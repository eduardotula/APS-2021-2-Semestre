package com.source;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ConfigurableApplicationContext;

import javafx.application.Application;
import javafx.stage.Stage;

public class ChartApplication extends Application {
	private ConfigurableApplicationContext applicationContext;

	
	@Override
	public void init() throws Exception {
		applicationContext = new SpringApplicationBuilder(Aps20212SemestreApplication.class).run();
	}

	@Override
	public void start(Stage stage) throws Exception {
		applicationContext.publishEvent(new StageReadyEvent(stage));
	}

	public class StageReadyEvent extends ApplicationEvent {

		private static final long serialVersionUID = 1L;

		public StageReadyEvent(Stage stage) {
			super(stage);
		}

	}
}
