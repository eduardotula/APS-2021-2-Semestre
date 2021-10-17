package com.source.control;

import java.util.ArrayList;
import java.util.List;

import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Point;
import org.bytedeco.opencv.opencv_core.Rect;
import org.bytedeco.opencv.opencv_core.RectVector;
import org.bytedeco.opencv.opencv_core.Scalar;
import org.bytedeco.opencv.opencv_face.FaceRecognizer;
import org.bytedeco.opencv.opencv_face.Facemark;
import org.bytedeco.opencv.opencv_objdetect.CascadeClassifier;
import org.bytedeco.opencv.opencv_videoio.VideoCapture;


import facerecognizers.FaceRecog;
import javafx.concurrent.Task;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class WebcamThreadDetect extends Task<Void> {

	private ImageView view;
	private VideoCapture cap;
	private CascadeClassifier cas;
	private FaceRecognizer model;
	private FaceRecog recog;
	private double thres = 85;

	public WebcamThreadDetect(ImageView view, VideoCapture cap, CascadeClassifier cas, FaceRecognizer model,
			FaceRecog recog) {
		this.view = view;
		this.cap = cap;
		this.cas = cas;
		this.model = model;
		this.recog = recog;
	}

	@Override
	protected Void call() {
		try {
			model.setThreshold(thres);
			Rect facePrinc = new Rect();
			RectVector faces = new RectVector();
			Mat frame = new Mat();
			
			while (!cap.isNull() && cap.isOpened() && view.isVisible()) {
				System.out.println(cap.read(frame));

				faces = Utilitarios.detectFaces(cas, frame);
				
				if (faces.size() > 0) {
					
					facePrinc = Utilitarios.detectFacePrincipal(faces);
					
					if (facePrinc.width() >= 150 && facePrinc.height() >= 150) {
						
						int[] labelPrece = recog.identificarRosto(model, frame);
						frame = drawBlueRec(frame, facePrinc);
						
						if (labelPrece[0] > -1) {

							opencv_imgproc.putText(frame, model.getLabelInfo(labelPrece[0]),
									new Point(facePrinc.x(), facePrinc.y()),
									opencv_imgproc.FONT_HERSHEY_PLAIN, 3, new Scalar(0, 255, 0, 2.0), 3,
									opencv_imgproc.LINE_AA, false);
							
							opencv_imgproc.putText(frame, "% " + calcularPorcentagem(labelPrece[1]),
									new Point(facePrinc.x(),
											facePrinc.y() + facePrinc.height() + 10),
									opencv_imgproc.FONT_HERSHEY_PLAIN, 3, new Scalar(0, 255, 0, 2.0), 3,
									opencv_imgproc.LINE_AA, false);
						}
					} else {
						frame = drawRedRec(frame, facePrinc);

					}

					view.setImage(Utilitarios.convertMatToImage(frame));

				} else {
					System.out.println(frame.rows());
					view.setImage(Utilitarios.convertMatToImage(frame));
				}
				Thread.sleep(1);
			}

			frame.close();
			model.close();
			cap.close();
			view.setImage(null);
			return null;
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private Mat drawRedRec(Mat frame, Rect rect) throws Exception {
		Mat img = frame.clone();
		opencv_imgproc.rectangle(img, new Point(rect.x(), rect.y()),
				new Point(rect.x() + rect.width(), rect.y() + rect.height()), Scalar.RED, 1, opencv_imgproc.LINE_AA, 0);
		return img;
	}

	private Mat drawBlueRec(Mat frame, Rect rect) throws Exception {
		Mat img = frame.clone();
		opencv_imgproc.rectangle(img, new Point(rect.x(), rect.y()),
				new Point(rect.x() + rect.width(), rect.y() + rect.height()), Scalar.BLUE, 1, opencv_imgproc.LINE_AA,
				0);
		return img;
	}

	private int calcularPorcentagem(int result) {
		result = (int) (((result * 100)/thres) - 100) * -1 ;
		return result;
	}
}
