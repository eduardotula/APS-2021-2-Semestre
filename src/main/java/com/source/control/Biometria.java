package com.source.control;

import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.global.opencv_features2d;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.DMatch;
import org.bytedeco.opencv.opencv_core.DMatchVectorVector;
import org.bytedeco.opencv.opencv_core.KeyPointVector;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Scalar;
import org.bytedeco.opencv.opencv_core.Size;
import org.bytedeco.opencv.opencv_features2d.DescriptorMatcher;
import org.bytedeco.opencv.opencv_features2d.FlannBasedMatcher;
import org.bytedeco.opencv.opencv_xfeatures2d.SURF;

public class Biometria {
	
	private float ratio =  0.7f;
	
	
	public Double compare(Mat img, Mat img2) {
		opencv_imgproc.equalizeHist(img, img);
		opencv_imgproc.GaussianBlur(img, img, new Size(3,3), 0);
		
		opencv_imgproc.equalizeHist(img2, img2);
		opencv_imgproc.GaussianBlur(img2, img2, new Size(3,3), 0);
		img2 = thin(img2);
		img = thin(img);
		opencv_imgproc.GaussianBlur(img, img, new Size(3,3),0);
		opencv_imgproc.GaussianBlur(img2, img2, new Size(3,3),0);
		
		
		SURF a = SURF.create();
		KeyPointVector keyPointImg1 = new KeyPointVector();
		KeyPointVector keyPointImg2 = new KeyPointVector();
		Mat objectDescImg1 = new Mat();
		Mat objectDescImg2 = new Mat();
		a.detectAndCompute(img, new Mat(), keyPointImg1, objectDescImg1, false);
		a.detectAndCompute(img2, new Mat(), keyPointImg2, objectDescImg2, false);
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
		// -- Draw matches
		Mat imgMatches = new Mat();
		opencv_features2d.drawMatchesKnn(img, keyPointImg1, img2, keyPointImg2, listOfGoodMatches,imgMatches,Scalar.all(-1),Scalar.all(-1),null, opencv_features2d.DRAW_RICH_KEYPOINTS);

		
		Utilitarios.showImage(imgMatches);
		return preci;
		
	}
	
	public Mat thin(Mat img1) {
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
		return skel;
	}

}
