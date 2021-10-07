package facerecognizers;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.MatVector;
import org.bytedeco.opencv.opencv_core.Rect;
import org.bytedeco.opencv.opencv_core.RectVector;
import org.bytedeco.opencv.opencv_core.Size;
import org.bytedeco.opencv.opencv_face.FaceRecognizer;
import org.bytedeco.opencv.opencv_face.FisherFaceRecognizer;
import org.bytedeco.opencv.opencv_objdetect.CascadeClassifier;

import com.source.control.Utilitarios;

public class FisherRecog implements FaceRecog{
	
	private CascadeClassifier cas;
	private RectVector facesDetectadas;
	private Mat imagemResized;
	private MatVector rostosProcessados;
	private Mat labels;
	private Rect rostoPrimario;

	public FisherRecog(CascadeClassifier cas) {
		this.cas = cas;
	}


	
	/**Treina um modelo fisher face dado um vetore de imagens Mat
	 * Este metodo irá utilizar somente o rosto com a maior resolução
	 * 
	 * @param src Vetor de imagens Mat para ser usado na criação do modelo
	 * @return Retorna FaceRecognizer treinado
	 * @throws Exception 
	 * */
	public FaceRecognizer train(MatVector src, FaceRecognizer recognizer) throws Exception {
		int labelCounter = 0;
		
		if(src.get().length <= 0) { throw new Exception("Vetores de imagem não pode estar vazio");}
		labels = new Mat();
		facesDetectadas = new RectVector();
		
		List<Mat> rostosProcessadosList = new ArrayList<Mat>();
		
		
		for(Mat image : src.get()) {
			//Detecta os rostos de uma imagem
			facesDetectadas = Utilitarios.detectFaces(cas, image);
			//Detecta o rosto com maior resolução
			rostoPrimario = detectRostoPrincipal(facesDetectadas);
			
			//Caso a imagem contenha um rosto realiza o processamento
			if(rostoPrimario.width() > 0 && rostoPrimario.height() > 0) {
				//Recorta o rosto
				image = recortarRosto(rostoPrimario, image);
				imagemResized = new Mat();
				//Ajusta o tamanho 
				opencv_imgproc.resize(image, imagemResized, new Size(100, 100),
				      1.0, 1.0, opencv_imgproc.INTER_CUBIC);
				rostosProcessadosList.add(imagemResized);
			}

		}
		rostosProcessados = new MatVector(rostosProcessadosList.size());
        labels = new Mat(rostosProcessados.get().length, 1, opencv_core.CV_32SC1);
        IntBuffer labelsBuf = labels.createBuffer();
        int label = 1;
		for(int i = 0;i<rostosProcessados.get().length;i++) {
			rostosProcessados.put(i, rostosProcessadosList.get(i));
			labelsBuf.put(i, label);
			System.out.println(i);
			label++;
		}
		
		if(rostosProcessados.get().length <= 0) {labels.close(); rostosProcessados.close();
		throw new Exception("Não foi encontrado nenhum rosto no vetor de imagens");}
		System.out.println(labels.arrayDepth());
		System.out.println(labels.arrayHeight());
		System.out.println(labels.arraySize());
		recognizer.train(rostosProcessados,labels);
		releaseResources();
		return recognizer;
	}
	
	
	/**Cria um novo modelo de FisherFaceRecognizer e realiza o treino para um conjunto de imagens src
	 * @param src Vetor de imagens Mat para ser usado na criação do modelo
	 * @return FaceRecognizer treinado
	 */
	public FaceRecognizer train(MatVector src) throws Exception {
		FaceRecognizer recognizer = FisherFaceRecognizer.create();
		return train(src, recognizer);
	}
	/**Carrega um modelo de FisherFaceRecognizer e realiza o treino para um conjunto de imagens src
	 * @param src Vetor de imagens Mat para ser usado na criação do modelo
	 * @param modelPath diretorio do modelo FisherFace
	 * @return FaceRecognizer treinado
	 */
	public FaceRecognizer train(MatVector src, String modelPath) throws Exception{
		FaceRecognizer recognizer = FisherFaceRecognizer.create();
		recognizer.read(modelPath);
		return train(src, recognizer);
	}
	
	private void releaseResources() {
		try {
			cas.close();
			facesDetectadas.close();
			imagemResized.close();
			rostosProcessados.close();
			labels.close();
			rostoPrimario.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
