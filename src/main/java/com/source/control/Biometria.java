package com.source.control;

import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.global.opencv_features2d;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.DMatch;
import org.bytedeco.opencv.opencv_core.DMatchVectorVector;
import org.bytedeco.opencv.opencv_core.KeyPointVector;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Rect;
import org.bytedeco.opencv.opencv_core.Scalar;
import org.bytedeco.opencv.opencv_core.Size;
import org.bytedeco.opencv.opencv_features2d.BFMatcher;
import org.bytedeco.opencv.opencv_features2d.DescriptorMatcher;
import org.bytedeco.opencv.opencv_features2d.FlannBasedMatcher;
import org.bytedeco.opencv.opencv_features2d.GFTTDetector;
import org.bytedeco.opencv.opencv_features2d.SIFT;
import org.bytedeco.opencv.opencv_xfeatures2d.SURF;


//Preprocessamento

//Histogram equalization

//GFTT corner detection
//SIFT Descriptor
//BruteForceMatcher
/**
 * Esta classe contem os metodos necessarios para processar e comparar impressões digitais*/
public class Biometria {
	
	private float ratio =  0.7f;
	
	private SURF surf = SURF.create();
	private GFTTDetector gf = GFTTDetector.create();
	private SIFT sft = SIFT.create();
	private BFMatcher bf = BFMatcher.create();
	
	public Mat compare(Mat img, Mat img2, double preciEspera) {
		
		KeyPointVector keyPointImg1 = new KeyPointVector();
		KeyPointVector keyPointImg2 = new KeyPointVector();
		Mat objectDescImg1 = new Mat();
		Mat objectDescImg2 = new Mat();
		surf.detectAndCompute(img, new Mat(), keyPointImg1, objectDescImg1, false);
		surf.detectAndCompute(img2, new Mat(), keyPointImg2, objectDescImg2, false);
		DescriptorMatcher matcher = FlannBasedMatcher.create(FlannBasedMatcher.FLANNBASED);
		DMatchVectorVector knnMatches = new DMatchVectorVector();
		matcher.knnMatch(objectDescImg1, objectDescImg2, knnMatches, 2);

		
		
		
		DMatchVectorVector listOfGoodMatches = new DMatchVectorVector();
		for (int i = 0; i < knnMatches.size(); i++) {
			if (knnMatches.get(i).size() > 1) {
				DMatch[] matches = knnMatches.get(i).get();
				if (matches[0].distance() < ratio * matches[1].distance()) {
					listOfGoodMatches.push_back(knnMatches.get(i));
				}
			}
		}
		System.out.println(listOfGoodMatches.size());
		System.out.println(knnMatches.size());
		
		Double preci = (double) ((listOfGoodMatches.size()*100) / knnMatches.size());
		System.out.println(preci);
		
		if(listOfGoodMatches.size() >= preciEspera) {
			Mat imagemCompara  = new Mat();
			opencv_features2d.drawMatchesKnn(img, keyPointImg1, img2, keyPointImg2, listOfGoodMatches,imagemCompara,Scalar.all(-1),Scalar.all(-1),null, opencv_features2d.NOT_DRAW_SINGLE_POINTS);
			listOfGoodMatches.close();
			return imagemCompara;
		}
		listOfGoodMatches.close();
		return null;
		
		
	}
	
	public Mat compareTeste(Mat img, Mat img2, double preciEspera) {

		KeyPointVector keyPointImg1 = new KeyPointVector();
		KeyPointVector keyPointImg2 = new KeyPointVector();
		Mat objectDescImg1 = new Mat();
		Mat objectDescImg2 = new Mat();
		gf.detect(img, keyPointImg1);
		gf.detect(img2, keyPointImg2);
		
		sft.compute(img, keyPointImg1, objectDescImg1);
		sft.compute(img2, keyPointImg2, objectDescImg2);
		
		DMatchVectorVector knnMatches = new DMatchVectorVector();
		
		bf.knnMatch(objectDescImg1, objectDescImg2, knnMatches, 2);
		
		
		//matcher.knnMatch(objectDescImg1, objectDescImg2, knnMatches, 2);

		
		
		
		DMatchVectorVector listOfGoodMatches = new DMatchVectorVector();
		for (int i = 0; i < knnMatches.size(); i++) {
			if (knnMatches.get(i).size() > 1) {
				DMatch[] matches = knnMatches.get(i).get();
				if (matches[0].distance() < ratio * matches[1].distance()) {
					listOfGoodMatches.push_back(knnMatches.get(i));
				}
			}
		}
		System.out.println(listOfGoodMatches.size());
		System.out.println(knnMatches.size());
		
		Double preci = (double) ((listOfGoodMatches.size()*100) / knnMatches.size());
		System.out.println(preci);
		
		if(listOfGoodMatches.size() >= preciEspera) {
			Mat imagemCompara  = new Mat();
			opencv_features2d.drawMatchesKnn(img, keyPointImg1, img2, keyPointImg2, listOfGoodMatches,imagemCompara,Scalar.all(-1),Scalar.all(-1),null, opencv_features2d.NOT_DRAW_SINGLE_POINTS);
			listOfGoodMatches.close();
			return imagemCompara;
		}
		listOfGoodMatches.close();
		return null;
	}
	
	public static Mat processImg(Mat imagem) {
		Mat img = cropImg(imagem);
		opencv_imgproc.equalizeHist(img, img);
		opencv_imgproc.GaussianBlur(img, img, new Size(3,3), 0);
		
		Mat dst = cropImg(img);
	//	Mat dst2 = thin(dst);
		opencv_imgproc.threshold(img, img, 0, 255, opencv_imgproc.THRESH_OTSU);
		opencv_imgproc.GaussianBlur(img, img, new Size(3,3),0);
		return img;
	}
	
	public static Mat processTeste(Mat imagem) {
		Mat imt = imagem.clone();
		Mat temp2 = new Mat();
		Mat temp3 = new Mat();
		opencv_imgproc.equalizeHist(imt, imt);
		opencv_imgproc.GaussianBlur(imt, temp2, new Size(0,0), 10);
		opencv_core.addWeighted(temp2,  1.5, imt, -0.5, 1, temp3);
		Mat temp4 = cropImg(temp3);
		return temp4;
	}
	
	public static Mat cropImg(Mat img) {
		if(img.type() > 0) {
			opencv_imgproc.cvtColor(img, img, opencv_imgproc.COLOR_BGR2GRAY);
		}
		Mat kernel = opencv_imgproc.getStructuringElement(opencv_imgproc.MORPH_ELLIPSE, new Size(3, 3));
		
		Mat temp = new Mat();
		opencv_core.bitwise_not(img, temp); //Inverter imagem
		opencv_imgproc.erode(temp, temp, kernel);
		Rect rec = opencv_imgproc.boundingRect(temp);
		
		System.out.println(rec.x() + " " + rec.y() + " " + rec.width() + " " + rec.height());

		Mat i = new Mat(temp.clone(), rec);
		opencv_imgproc.resize(i, i, new Size(250,300));
		opencv_core.bitwise_not(i, i); //Inverter imagem
		temp.close();
		return i;
		
	}
	
	private static Mat thin(Mat img1) {
		Mat tresh = img1.clone();

		opencv_imgproc.threshold(tresh, tresh, 0, 255, opencv_imgproc.THRESH_OTSU);
		
		Mat element = opencv_imgproc.getStructuringElement(opencv_imgproc.MORPH_ELLIPSE, new Size(3, 3));
		Mat eroded = new Mat();
		Mat temp = new Mat();
		Mat skel = new Mat(tresh.rows(), tresh.cols(), opencv_core.CV_8UC1, new Scalar(0));

		int size = img1.cols() * img1.rows();
		int zeros = 0;
		boolean a = true;

		while (a) {
			opencv_imgproc.erode(tresh, eroded, element);
			opencv_imgproc.dilate(eroded, temp, element);
			opencv_core.subtract(tresh, temp, temp);
			opencv_core.bitwise_or(skel, temp, skel);
			eroded.copyTo(tresh);

			zeros = size - opencv_core.countNonZero(tresh);
			if (zeros == size)
				a = false;
		}
		temp.close();
		eroded.close();
		return skel;
		
	}

}
