package facerecognizers;

import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Rect;
import org.bytedeco.opencv.opencv_core.RectVector;


public interface FaceRecog {
	
	@SuppressWarnings("resource")
	public default Rect detectRostoPrincipal(RectVector facesDetectadas) {
		
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
	public default Mat recortarRosto(Rect posicaoRosto, Mat imagem) {
		
		Rect imgCrop = new Rect(posicaoRosto.x(), posicaoRosto.y(), posicaoRosto.width(), posicaoRosto.height());
		imagem = new Mat(imagem, imgCrop);
		return imagem;
	}

	
}
