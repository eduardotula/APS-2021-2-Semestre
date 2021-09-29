package com.view.controllers;

import java.io.File;

import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.image.ImageView;

public class CMainFrame{
	
	
	public CheckBox rdnHaar;
	public CheckBox rdnLpb;
	public ImageView img;
	public Button btnStart;
	
	private CascadeClassifier cas;
	
	public CMainFrame() {
		cas = new CascadeClassifier();
		btnStart.setDisable(true);
	}
	
	public void actBtnStart() {
	}
	
	
	public void actHaarSelected() {
		loadClassifiers("com/classifiers/haar");
        this.btnStart.setDisable(false);
	}
	
	public void actLbpSelected() {
		loadClassifiers("com/classifiers/lbp");
		this.btnStart.setDisable(false);
	}
	
	private void loadClassifiers(String folderPath) {
		File file = new File(getClass().getClassLoader().getResource(folderPath).getPath());
		File[] files = file.listFiles();
        for(File fier : files){
        	System.out.println(fier.getPath());
        	cas.load(fier.getPath().toString());
        }
	}

}
