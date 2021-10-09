package facerecognizers;

import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.MatVector;
import org.bytedeco.opencv.opencv_core.Rect;
import org.bytedeco.opencv.opencv_core.RectVector;
import org.bytedeco.opencv.opencv_core.Size;
import org.bytedeco.opencv.opencv_face.FaceRecognizer;


public abstract class FaceRecog {
	
	private int resizeRows = 150;
	private int resizeColumn = 150;
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
		Mat imagemRec = new Mat(imagem, imgCrop);
		return imagemRec;
	}
	
	/**
	 * Processa a imagem de acordo com os padroes LBPH
	 * detectRostoPrincipal, recortarRosto, resize para 1000 pixels e converte para BGR2GRAY
	 * @param imagem para ser processada
	 * @param facesDetectadas lista de faces identificadas no frame*/
	public Mat processImage(Mat imagem, RectVector facesDetectadas)throws Exception {
		System.out.println("Metodo processImage");
		System.out.println("facesDetectadas " + facesDetectadas.size());
		System.out.println("Input image rows " + imagem.rows());
		System.out.println("input channels " + imagem.channels());
		System.out.println("input type " + imagem.type());
		Mat imgProc = new Mat(imagem);
		Rect rostoPrimario = new Rect();
		
		//Detecta o rosto com maior resolução
		rostoPrimario = detectRostoPrincipal(facesDetectadas);

		//Caso a imagem contenha um rosto realiza o processamento
		if(rostoPrimario.width() > 0 && rostoPrimario.height() > 0) {
			//Recorta o rosto
			
			
			if(imagem.rows() > resizeRows && imagem.cols() > resizeColumn) {
				//Ajusta o tamanho 
				imgProc = recortarRosto(rostoPrimario, imagem);
				opencv_imgproc.resize(imgProc, imgProc, new Size(150, 150),
				      1.0, 1.0, opencv_imgproc.INTER_CUBIC);
			}

			if(imgProc.type() > 0) {
				opencv_imgproc.cvtColor(imgProc, imgProc, opencv_imgproc.COLOR_BGR2GRAY);
			}
		}
		System.out.println("facesDetectadas " + facesDetectadas.size());
		System.out.println("output image rows " + imgProc.rows());
		System.out.println("output channels " + imgProc.channels());
		return imgProc;
	}

	public FaceRecognizer train(MatVector src, FaceRecognizer recognizer) throws Exception{return null;}	
	public FaceRecognizer train(MatVector src) throws Exception {return null;}
	public FaceRecognizer train(MatVector src, String modelPath)throws Exception {return null;}

	public double identificarRosto(FaceRecognizer recog, Mat imagem)throws Exception{return 0.0;}
}
