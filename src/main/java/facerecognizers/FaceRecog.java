package facerecognizers;

import java.io.File;
import java.util.List;

import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.MatVector;
import org.bytedeco.opencv.opencv_core.Rect;
import org.bytedeco.opencv.opencv_core.RectVector;
import org.bytedeco.opencv.opencv_core.Size;
import org.bytedeco.opencv.opencv_face.FaceRecognizer;

import com.source.control.Utilitarios;
import com.source.model.Imag;


public abstract class FaceRecog {
	
	public static int resizeRows = 150;
	public static int resizeColumn = 150;
	/**
	 * Detecta o rosto com maior resolução
	 * @param facesDetectadas vetor contendo faces identificadas
	 * @return rosto com maior resolução*/
	@SuppressWarnings("resource")
	public Rect detectRostoPrincipal(RectVector facesDetectadas) {
		//System.out.println("DetectRostoPrincipal()  " +  facesDetectadas.size());
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
		Mat imagemRec = new Mat(imagem.clone(), imgCrop);
		return imagemRec;
	}
	

	
	/**
	 * Processa a imagem de acordo com os padroes LBPH
	 * detectRostoPrincipal, recortarRosto, resize para 1000 pixels e converte para BGR2GRAY
	 * @param imagem para ser processada
	 * @param facePrincipal Rec da face principal
	 * @throws Exception*/
	@SuppressWarnings("resource")
	public Mat processImage(Mat imagem, Rect facePrincipal)throws Exception {
		System.out.println("Metodo processImage");
		System.out.println("Input image rows " + imagem.rows());
		System.out.println("input channels " + imagem.channels());
		System.out.println("input type " + imagem.type());
		Mat imgProc = imagem.clone();
		
		

		//Caso a imagem contenha um rosto realiza o processamento
		if(facePrincipal.width() > 0 && facePrincipal.height() > 0) {
			//Recorta o rosto
			
			
			if(imgProc.rows() > resizeRows && imgProc.cols() > resizeColumn) {
				//Ajusta o tamanho 
				imgProc = recortarRosto(facePrincipal, imgProc);
				opencv_imgproc.resize(imgProc, imgProc, new Size(150, 150),
				      1.0, 1.0, opencv_imgproc.INTER_CUBIC);
			}

			if(imgProc.type() > 0) {
				opencv_imgproc.cvtColor(imgProc, imgProc, opencv_imgproc.COLOR_BGR2GRAY);
			}
		}else {
			imgProc.close(); facePrincipal.close(); throw new Exception("Imagem não contem faces");
		}
		System.out.println("facesDetectadas " + facePrincipal.width());
		System.out.println("output image rows " + imgProc.rows());
		System.out.println("output channels " + imgProc.channels());
		return imgProc;
	}

	public Imag processImage(Imag imagem) throws Exception{
		imagem.setImagem(processImage(imagem.getImagem(), imagem.getRostoPrinc()));
		imagem.setProces(true);
		return imagem;
	}
}

}
