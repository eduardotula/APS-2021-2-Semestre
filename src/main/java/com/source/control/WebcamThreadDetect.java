package com.source.control;

import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.RectVector;
import org.bytedeco.opencv.opencv_face.FaceRecognizer;
import org.bytedeco.opencv.opencv_objdetect.CascadeClassifier;
import org.bytedeco.opencv.opencv_videoio.VideoCapture;

import facerecognizers.FaceRecog;
import javafx.concurrent.Task;
import javafx.scene.image.ImageView;

public class WebcamThreadDetect extends Task<Void>{

	private ImageView view;
	private VideoCapture cap;
	private CascadeClassifier cas;
	private FaceRecognizer recognizerModel;
	private FaceRecog recog;
	
	public WebcamThreadDetect(ImageView view, VideoCapture cap, CascadeClassifier cas, FaceRecognizer recognizerModel,
			FaceRecog recog) {
		this.view = view;
		this.cap = cap;
		this.cas = cas;
		this.recognizerModel = recognizerModel;
		this.recog = recog;
	}

	@Override
	protected Void call() throws Exception {
		Mat frame = new Mat();
		while(cap != null && cap.isOpened() && view.isVisible()) {
			cap.read(frame);
			
			RectVector faces = Utilitarios.detectFaces(cas, frame);
			Mat imgProc = recog.processImage(frame, faces);
			double predict = recog.identificarRosto(recognizerModel, imgProc);
			System.out.println(predict);
			Thread.sleep(200);
			System.out.println(cap.isOpened());
		}
		return null;
	}

	


}
