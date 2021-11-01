package com.view.controllers;

import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Size;

import com.source.control.Utilitarios;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

public class CTeste {
	
	@FXML
	public ImageView img;
	
	@FXML
	public Button btn;
	@FXML
	public TextField campo1;
	@FXML
	public TextField campo2;
	@FXML
	public TextField campo3;
	@FXML
	public TextField campo4;

	Mat imt;
	Mat imt2;
	@FXML
	public void actBtn() {
		
		
			imt = opencv_imgcodecs.imread("C:\\Users\\eduar\\Desktop\\Train img\\013_8_1.tif",opencv_imgcodecs.IMREAD_GRAYSCALE);
		
		Mat temp2 = new Mat();
		Mat temp3 = new Mat();
		opencv_imgproc.equalizeHist(imt, imt);
		opencv_imgproc.GaussianBlur(imt, temp2, new Size(0,0), Double.parseDouble(campo1.getText()));
		opencv_core.addWeighted(temp2,  Double.parseDouble(campo2.getText()), imt, Double.parseDouble(campo3.getText()),  Double.parseDouble(campo4.getText()), temp3);
		
		opencv_imgproc.cvtColor(temp3, temp3, opencv_imgproc.COLOR_GRAY2BGR);
		img.setImage(Utilitarios.convertMatToImage(temp3));
	}
}
