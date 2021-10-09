package com.source.control;

import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.RectVector;
import org.bytedeco.opencv.opencv_face.FaceRecognizer;
import org.bytedeco.opencv.opencv_objdetect.CascadeClassifier;
import org.bytedeco.opencv.opencv_videoio.VideoCapture;

import facerecognizers.FaceRecog;
import javafx.concurrent.Task;
import javafx.scene.image.Image;
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
		//recognizerModel.setThreshold(0);
		while(cap != null && cap.isOpened() && view.isVisible()) {
			System.out.println(cap.read(frame));
			
			RectVector faces = Utilitarios.detectFaces(cas, frame);
			if(faces.size() > 0) {
				System.out.println(frame.rows());
				double predict = recog.identificarRosto(recognizerModel, frame);
				System.out.println("     ++++++   "+predict);
			}
			
			Image imgView = Utilitarios.detectFaceRect(cas, frame);
			view.setImage(imgView);
			Thread.sleep(300);
			System.out.println(cap.isOpened());
		}
		return null;
	}

	


}
