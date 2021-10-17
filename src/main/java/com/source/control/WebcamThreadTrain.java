package com.source.control;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.MatVector;
import org.bytedeco.opencv.opencv_core.Point;
import org.bytedeco.opencv.opencv_core.Rect;
import org.bytedeco.opencv.opencv_core.RectVector;
import org.bytedeco.opencv.opencv_core.Scalar;
import org.bytedeco.opencv.opencv_face.EigenFaceRecognizer;
import org.bytedeco.opencv.opencv_face.FaceRecognizer;
import org.bytedeco.opencv.opencv_face.FisherFaceRecognizer;
import org.bytedeco.opencv.opencv_face.LBPHFaceRecognizer;
import org.bytedeco.opencv.opencv_objdetect.CascadeClassifier;
import org.bytedeco.opencv.opencv_videoio.VideoCapture;

import com.view.controllers.CMainFrame;

import facerecognizers.FaceRecog;
import facerecognizers.LBPHFaceReco;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

public class WebcamThreadTrain extends Task<Void> {

	private ImageView view;
	private VideoCapture cap;
	private CascadeClassifier cas;
	private FaceRecog recog;
	private Integer label;
	private List<Mat> frames = new ArrayList<Mat>();
	private List<RectVector> listaFaces = new ArrayList<RectVector>();
	private List<Rect> listaFacePrinc = new ArrayList<Rect>();
	private Mat frame = new Mat();
	private Rect facePrinc = new Rect();
	private RectVector faces = new RectVector();
	private String nome;
	private Integer reduceArray = 4;

	public WebcamThreadTrain(ImageView view, VideoCapture cap, CascadeClassifier cas, FaceRecog recog, Integer label,
			String nome) {
		this.view = view;
		this.cap = cap;
		this.cas = cas;
		this.recog = recog;
		this.label = label;
		this.nome = nome;
	}

	@Override
	protected Void call() {
		try {

			System.out.println(label);
			while (!cap.isNull() && cap.isOpened() && view.isVisible()) {
				System.out.println(cap.read(frame));

				faces = Utilitarios.detectFaces(cas, frame);
				if (faces.size() > 0) {

					facePrinc = Utilitarios.detectFacePrincipal(faces);
					if (facePrinc.width() >= 150 && facePrinc.height() >= 150) {
						System.out.println("Input image rows " + frame.rows());
						System.out.println("input channels " + frame.channels());
						frames.add(frame.clone());
						listaFaces.add(Utilitarios.cloneRectVector(faces));
						listaFacePrinc.add(facePrinc);
						frame = (drawBlueRec(frame, facePrinc));
					} else {
						frame = drawRedRec(frame, facePrinc);

					}
					System.out.println(frame.rows() + " teeeee");
					System.out.println(frame.channels() + " teeeee ch");

					/*
					 * opencv_imgproc.putText(imgFace.getImagem(), CMainFrame.txt, new
					 * Point(Math.max(imgFace.getRostoPrinc().tl().x() - 10, 0),
					 * Math.max(imgFace.getRostoPrinc().tl().y() - 10, 0)),
					 * opencv_imgproc.FONT_HERSHEY_PLAIN, 5.0, new Scalar(255, 0, 255, 2.0),3,
					 * opencv_imgproc.LINE_4,false);
					 */
					view.setImage(Utilitarios.convertMatToImage(frame));

				} else {
					System.out.println(frame.rows());
					view.setImage(Utilitarios.convertMatToImage(frame));
				}
				Thread.sleep(1);
			}
			reduceListSize(frames);

			Platform.runLater(() -> {

				try {
					FaceRecognizer model = FisherFaceRecognizer.create();
					File modelPath = new FileChooser().showOpenDialog(null);

					if (modelPath != null) {
						model.read(modelPath.getAbsolutePath());
						model.setLabelInfo(label, nome);
						recog.updateRaw(model, frames).write(modelPath.getAbsolutePath());
					} else {
						model.setLabelInfo(label, nome);
						for (int i = 0; i < frames.size(); i++) {
							System.out.println(listaFacePrinc.get(i).width());
							System.out.println(listaFacePrinc.get(i).x());
						}
						recog.trainRaw(model, frames, listaFacePrinc, listaFaces)
								.write(new FileChooser().showSaveDialog(null).getAbsolutePath());
					}

					frame.close();
					cap.close();
				} catch (Exception e) {
					e.printStackTrace();
				}

			});

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

	private void reduceListSize(List<Mat> faceFrames) {
		int counter = 0;
		for (int i = 0; i < faceFrames.size(); i++) {
			if (reduceArray == counter) {
				faceFrames.remove(i);
				counter = 0;
			} else {

				counter++;

			}
		}

	}
}
