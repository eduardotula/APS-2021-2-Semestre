package com.source.control;

import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.global.opencv_features2d;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.DMatch;
import org.bytedeco.opencv.opencv_core.DMatchVectorVector;
import org.bytedeco.opencv.opencv_core.KeyPointVector;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.MatVector;
import org.bytedeco.opencv.opencv_core.Rect;
import org.bytedeco.opencv.opencv_core.Scalar;
import org.bytedeco.opencv.opencv_core.Size;
import org.bytedeco.opencv.opencv_features2d.BFMatcher;
import org.bytedeco.opencv.opencv_features2d.SIFT;


//Preprocessamento

//Histogram equalization

//GFTT corner detection
//SIFT Descriptor
//BruteForceMatcher
/**
 * Esta classe contem os metodos necessarios para processar e comparar impressões digitais*/
public class Biometria {
	
	private float ratio =  0.7f;
	
	//private GFTTDetector gf = GFTTDetector.create();
	private SIFT sft = SIFT.create();
	private BFMatcher bf = BFMatcher.create();
	
	
	/**
	 * Realiza a comparação entre duas imagens utilizando os seguintes algoritmos: Good features to track para obter pontos de interesse(keypoints),
	 * SIFT para detecção de descritores (descriptors) dos pontos de interesse e utiliza BruteForce como combinador para comparar os descritores das duas imagens, utilizar um metodo BruteForce 
	 * não produz os resultados mais rapidos mas de acordo com testes realizadosfoi obtido uma maior precisão nos testes.
	 * @param img imagem para ser comparada
	 * @param img2 imagem para ser comparada
	 * @param preciEspera numero de combinações esperadas entre as duas imagens
	 * @return imagem mostrando visualmente a comparação ou null */
	public Mat compare(Mat img, Mat img2, double preciEspera) {

		KeyPointVector keyPointImg1 = new KeyPointVector();
		KeyPointVector keyPointImg2 = new KeyPointVector();
		Mat objectDescImg1 = new Mat();
		Mat objectDescImg2 = new Mat();
		
		//Detecta os pontos de interesse 
		sft.detect(img, keyPointImg1);
		sft.detect(img2, keyPointImg2);
		//Calcula os descritores de pontos de interesse
		sft.compute(img, keyPointImg1, objectDescImg1);
		sft.compute(img2, keyPointImg2, objectDescImg2);
		
		DMatchVectorVector knnMatches = new DMatchVectorVector();
		
		//Analiza as melhores combinações de descritores das duas imagens
		bf.knnMatch(objectDescImg1, objectDescImg2, knnMatches, 2);
		
		//Realiza um utilmo teste utilizando um valor ratio para filtrar melhores resultados
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
		
		if(listOfGoodMatches.size() >= preciEspera) {
			Mat imagemCompara  = new Mat();
			opencv_features2d.drawMatchesKnn(img, keyPointImg1, img2, keyPointImg2, listOfGoodMatches,imagemCompara,Scalar.all(-1),Scalar.all(-1),null, opencv_features2d.NOT_DRAW_SINGLE_POINTS);
			listOfGoodMatches.close();
			return imagemCompara;
		}
		listOfGoodMatches.close();
		return null;
	}
	
	
	/**
	 * Processa a imagem para o padrão utilizado na aplicação
	 * @param img imagem para ser processada
	 * @return imagem processada com tamanho de (250,300)*/
	public static Mat processImagem(Mat img) {
		Mat imagem = img.clone();
		if(imagem.type() > 0) {
			opencv_imgproc.cvtColor(imagem, imagem, opencv_imgproc.COLOR_BGR2GRAY);
		}
		//Obtem a as dimensões da impressão digital
		Rect rec = getBigContour(imagem);
		
		Mat imt = new Mat(imagem.clone(),rec);
		Mat temp2 = new Mat();
		Mat temp3 = new Mat();
		//Equalisa o histograma da imagem
		opencv_imgproc.equalizeHist(imt, imt);
		
		//Embaça a imagem aplicando um filtro Gaussian, e mistura a imagem orignal,embaçada e alguns pesos para obter uma imagem menos embaçada 
		opencv_imgproc.GaussianBlur(imt, temp2, new Size(0,0), 20);
		opencv_core.addWeighted(temp2,  2, imt, -1.1, 1, temp3);
		
		
		//Ajusta o tamanho da imagem para um tamanho fixo
		opencv_imgproc.resize(temp3,temp3, new Size(250,300));
		//Equalisa o histograma da imagem
		opencv_imgproc.equalizeHist(temp3.clone(), temp3);
		temp2.close();
		imt.close();
		imagem.close();
		return temp3;
	}
	

	
	/*
	 * private static Mat cropImg(Mat img) { if(img.type() > 0) {
	 * opencv_imgproc.cvtColor(img, img, opencv_imgproc.COLOR_BGR2GRAY); } Mat
	 * kernel = opencv_imgproc.getStructuringElement(opencv_imgproc.MORPH_ELLIPSE,
	 * new Size(3, 3));
	 * 
	 * Mat temp = new Mat(); opencv_core.bitwise_not(img, temp); //Inverter imagem
	 * opencv_imgproc.erode(temp, temp, kernel); Rect rec =
	 * opencv_imgproc.boundingRect(temp);
	 * 
	 * System.out.println(rec.x() + " " + rec.y() + " " + rec.width() + " " +
	 * rec.height());
	 * 
	 * Mat i = new Mat(temp.clone(), rec); opencv_imgproc.resize(i, i, new
	 * Size(250,300)); opencv_core.bitwise_not(i, i); //Inverter imagem
	 * temp.close(); return i;
	 * 
	 * }
	 */
	
	/**
	 * Obtem o maior contorno contido na imagem
	 * @param imagem
	 * @return Rect dimesões do maior contorno da imagem*/
	public static Rect getBigContour(Mat imagem) {
		Mat temp = imagem.clone();
		Double contourAre = 0.0;
		MatVector vec = new MatVector();
		opencv_core.bitwise_not(temp, temp); //Inverter imagem
		opencv_imgproc.threshold(temp, temp, 0, 255, opencv_imgproc.THRESH_OTSU);
		opencv_imgproc.GaussianBlur(temp, temp, new Size(15,15),20); //Aplica um blurr
		
		//Busca por cortornos na imagem
		opencv_imgproc.findContours(temp, vec, opencv_imgproc.RETR_EXTERNAL, opencv_imgproc.CHAIN_APPROX_NONE);
		@SuppressWarnings("resource")
		Mat biggeCont = new Mat();
		//Busca pelo maior contorno
		for(int i = 0;i<vec.size();i++) {
			Mat cont = vec.get(i);
			double c = opencv_imgproc.contourArea(cont);
			if(c>=contourAre) {
				biggeCont = cont.clone();
				contourAre = c;
			}
		}
		//Obtem as dimensões do maior contorno
		Rect rec = opencv_imgproc.boundingRect(biggeCont);
		vec.close();
		temp.close();
		biggeCont.close();
		return rec;
	}
	/*
	 * private static Mat thin(Mat img1) { Mat tresh = img1.clone();
	 * 
	 * opencv_imgproc.threshold(tresh, tresh, 0, 255, opencv_imgproc.THRESH_OTSU);
	 * 
	 * Mat element =
	 * opencv_imgproc.getStructuringElement(opencv_imgproc.MORPH_ELLIPSE, new
	 * Size(3, 3)); Mat eroded = new Mat(); Mat temp = new Mat(); Mat skel = new
	 * Mat(tresh.rows(), tresh.cols(), opencv_core.CV_8UC1, new Scalar(0));
	 * 
	 * int size = img1.cols() * img1.rows(); int zeros = 0; boolean a = true;
	 * 
	 * while (a) { opencv_imgproc.erode(tresh, eroded, element);
	 * opencv_imgproc.dilate(eroded, temp, element); opencv_core.subtract(tresh,
	 * temp, temp); opencv_core.bitwise_or(skel, temp, skel); eroded.copyTo(tresh);
	 * 
	 * zeros = size - opencv_core.countNonZero(tresh); if (zeros == size) a = false;
	 * } temp.close(); eroded.close(); return skel;
	 * 
	 * }
	 */

}
