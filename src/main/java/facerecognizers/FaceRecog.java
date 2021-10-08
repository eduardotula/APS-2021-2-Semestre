package facerecognizers;

import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.MatVector;
import org.bytedeco.opencv.opencv_core.Rect;
import org.bytedeco.opencv.opencv_core.RectVector;
import org.bytedeco.opencv.opencv_face.FaceRecognizer;


public abstract class FaceRecog {
	
	/**
	 * Detecta o rosto com maior resolução
	 * @param facesDetectadas vetor contendo faces identificadas
	 * @return rosto com maior resolução*/
	@SuppressWarnings("resource")
	public Rect detectRostoPrincipal(RectVector facesDetectadas) {
		
		Rect rostoPrimario = new Rect();
		
		//Obtem o rosto detectado com maior resolução do frame
		for(Rect f : facesDetectadas.get()) {
			if(f.width() > rostoPrimario.width() && f.height() > rostoPrimario.height()) {
				rostoPrimario = f;
			}
		}
		return rostoPrimario;
	}
	
	/**Recorta o rosto detectado*/
	public Mat recortarRosto(Rect posicaoRosto, Mat imagem) {
		
		Rect imgCrop = new Rect(posicaoRosto.x(), posicaoRosto.y(), posicaoRosto.width(), posicaoRosto.height());
		imagem = new Mat(imagem, imgCrop);
		return imagem;
	}

	public FaceRecognizer train(MatVector src, FaceRecognizer recognizer) throws Exception{return null;}	
	public Mat processImage(Mat imagem, RectVector facesDetectadas)throws Exception {return null;}
	public FaceRecognizer train(MatVector src) throws Exception {return null;}
	public FaceRecognizer train(MatVector src, String modelPath)throws Exception {return null;}

	public double identificarRosto(FaceRecognizer recog, Mat imagem)throws Exception{return 0.0;}
}
