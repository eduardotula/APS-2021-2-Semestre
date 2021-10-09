package com.source.control;

import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Point;
import org.bytedeco.opencv.opencv_core.Rect;
import org.bytedeco.opencv.opencv_core.RectVector;
import org.bytedeco.opencv.opencv_core.Scalar;
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
	protected Void call()  {
		try {
			Mat frame = new Mat();
			RectVector faces = new RectVector();
			Mat imgFace = new Mat();
			Rect facePrinc = new Rect();
			recognizerModel.setThreshold(123.0);
			//recognizerModel.setThreshold(0);
			while(cap != null && cap.isOpened() && view.isVisible()) {
				System.out.println(cap.read(frame));
				
				faces = Utilitarios.detectFaces(cas, frame);
				if (faces.size() > 0) {
					imgFace = new Mat(frame);
					facePrinc = Utilitarios.detectFacePrincipal(faces);
					if(facePrinc.width() >= 150 && facePrinc.height() >= 150 ) {
						double pred = recog.identificarRosto(recognizerModel, frame, facePrinc);
						
						imgFace = drawBlueRec(frame, facePrinc);
					}else {
						imgFace = drawRedRec(frame, facePrinc);
						
					}
					System.out.println(imgFace.rows() + " teeeee");
					System.out.println(imgFace.channels() + " teeeee ch");

					view.setImage(Utilitarios.convertMatToImage(imgFace));
					
					
				}else {
					System.out.println(frame.rows());
					view.setImage(Utilitarios.convertMatToImage(frame));
				}
				Thread.sleep(300);
			}
			imgFace.close();facePrinc.close();frame.close();faces.close();
			recognizerModel.close();
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

	
	private Mat drawRedRec(Mat frame, Rect rect) throws Exception{
		Mat img = new Mat(frame);
		opencv_imgproc.rectangle(img, new Point(rect.x(), rect.y()),
				new Point(rect.x() + rect.width(), rect.y() + rect.height()), Scalar.RED, 1, opencv_imgproc.LINE_AA, 0);
		return img;
	}

	private Mat drawBlueRec(Mat frame, Rect rect) throws Exception{
		Mat img = new Mat(frame);
		opencv_imgproc.rectangle(img, new Point(rect.x(), rect.y()),
				new Point(rect.x() + rect.width(), rect.y() + rect.height()), Scalar.BLUE, 1, opencv_imgproc.LINE_AA, 0);
		return img;
	}
	
}
