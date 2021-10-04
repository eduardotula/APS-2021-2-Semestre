package com.source;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

import javafx.application.Application;

@SpringBootApplication(exclude = {
	    DataSourceAutoConfiguration.class, 
	    DataSourceTransactionManagerAutoConfiguration.class, 
	    HibernateJpaAutoConfiguration.class
	})
public class Aps20212SemestreApplication {

	public static void main(String[] args) {
		
		Application.launch(ChartApplication.class, args); //Inicia a Aplicação
	}

}
