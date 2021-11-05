package com.source;



import com.view.models.SplashScreen;

import javafx.application.Application;

public class Aps20212SemestreApplication {

	private static boolean preloaderFlag = true;
	public static void main(String[] args) {
		
		new Thread(() -> {
			SplashScreen spl = new SplashScreen();
			while(preloaderFlag) {
				spl.setVisible(true);
			}
			spl.close();
		}).start();
		
		Application.launch(Aplicacao.class, args); //Inicia a Aplicação
	}
	
	public static void setPreloaderFlag(boolean flag) {
		preloaderFlag = flag;
	}
	

}
