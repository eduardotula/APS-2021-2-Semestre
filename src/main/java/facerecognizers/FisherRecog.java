package facerecognizers;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.bytedeco.javacpp.Pointer;
import org.bytedeco.opencv.opencv_annotation;
import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.MatVector;
import org.bytedeco.opencv.opencv_core.Rect;
import org.bytedeco.opencv.opencv_core.RectVector;
import org.bytedeco.opencv.opencv_core.Scalar;
import org.bytedeco.opencv.opencv_core.Size;
import org.bytedeco.opencv.opencv_face.FaceRecognizer;
import org.bytedeco.opencv.opencv_face.FisherFaceRecognizer;
import org.bytedeco.opencv.opencv_objdetect.CascadeClassifier;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.face.EigenFaceRecognizer;

import com.source.control.Utilitarios;

import ch.qos.logback.core.CoreConstants;

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
		System.out.println(src.get().length + "  lengh1");
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
		labels = new Mat(2, 1, opencv_core.CV_32SC1);
		System.out.println(rostosProcessadosList.size()  + "   lenth2");
        //IntBuffer labelsBuf = labels.createBuffer();
        int label = 1;
		for(int i = 0;i<rostosProcessadosList.size();i++) {
			rostosProcessados.put(i,rostosProcessadosList.get(i));
			//labelsBuf.put(i, label);
			labels.data().put(i, Integer.valueOf(1).byteValue());
			System.out.println(i + "  loop");
			label++;
		}
		
		if(rostosProcessados.get().length <= 0) {labels.close(); rostosProcessados.close();
		throw new Exception("Não foi encontrado nenhum rosto no vetor de imagens");}
		System.out.println(labels.cols());
		System.out.println(labels.rows());
		System.out.println(labels.depth());
		System.out.println(labels.data().getInt(0));
		recognizer.train(rostosProcessados,labels);
		releaseResources();
		return recognizer;
	}
	
	public Mat processImage(Mat imagem, RectVector facesDetectadas) {
		Rect rostoPrincipal = detectRostoPrincipal(facesDetectadas);
		imagem = recortarRosto(rostoPrincipal, imagem);
		return recortarRosto(rostoPrincipal, imagem);
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
