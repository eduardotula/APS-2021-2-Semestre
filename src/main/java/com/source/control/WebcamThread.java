package com.source.control;

import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_objdetect.CascadeClassifier;
import org.bytedeco.opencv.opencv_videoio.VideoCapture;

import javafx.concurrent.Task;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class WebcamThread extends Task<Void>{

	private ImageView view;
	private VideoCapture cap;
	private CascadeClassifier cas;
	
	public WebcamThread(ImageView view, VideoCapture cap, CascadeClassifier cas) {
		this.view = view;
		this.cap = cap;
		this.cas = cas;
	}

	@Override
	protected Void call() throws Exception {
		Mat frame = new Mat();
		while(cap != null && cap.isOpened() && view.isVisible()) {
			cap.read(frame);
			Image imgRect = Utilitarios.detectFacesImage(cas, frame);
			view.setImage(imgRect);
			System.out.println(cap.isOpened());
		}
		return null;
	}

	


}
