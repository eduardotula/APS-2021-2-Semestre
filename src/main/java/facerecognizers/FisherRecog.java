package facerecognizers;

import java.util.ArrayList;
import java.util.List;

import org.bytedeco.javacpp.Pointer;
import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.MatVector;
import org.bytedeco.opencv.opencv_core.Rect;
import org.bytedeco.opencv.opencv_core.RectVector;
import org.bytedeco.opencv.opencv_face.FaceRecognizer;
import org.bytedeco.opencv.opencv_face.FisherFaceRecognizer;
import org.bytedeco.opencv.opencv_face.LBPHFaceRecognizer;
import org.bytedeco.opencv.opencv_objdetect.CascadeClassifier;

import com.source.control.Utilitarios;
import com.source.model.Imag;


public class FisherRecog extends FaceRecog{
	
	private CascadeClassifier cas;

	public FisherRecog(CascadeClassifier cas) {
		this.cas = cas;
	}


	
	/**Treina um modelo Fisher face dado um vetore de imagens Mat sem procesasmento
	 * Este metodo irá utilizar somente o rosto com a maior resolução
	 * 
	 * @param src Lista de imagens Mat para ser usado na criação do modelo
	 * @param recognizer
	 * @return Retorna FaceRecognizer treinado
	 * @throws Exception 
	 * */
	@SuppressWarnings("resource")
	@Override
	public FaceRecognizer trainRaw(FaceRecognizer recognizer,List<Imag> imagens) throws Exception {
		System.out.println(imagens.size() + "  lengh1");
		if(imagens.size() <= 0) { throw new Exception("Vetores de imagem não pode estar vazio");}
		MatVector rostosProcessadosList = new MatVector();
		System.out.println("quantidade de imagens para treinamento " + imagens.size());
		Mat labels = new Mat(0, 1, opencv_core.CV_32SC1);
		Mat label = new Mat(1,1,opencv_core.CV_32SC1);
		
		
		for(Imag image : imagens) {
			
			//Detecta os rostos de uma imagem
			image.setRostos(Utilitarios.detectFaces(cas, image.getImagem()));
			//Detecta o rosto principal
			image.setRostoPrinc(detectRostoPrincipal(image.getRostos()));
			//Processa a imagem
			image.setImagem(processImage(new Mat(image.getImagem()), image.getRostoPrinc()));

			image.setProces(true);
			label.data().put(0, Integer.valueOf(image.getIdLabel()).byteValue());
			labels.push_back(label);
			rostosProcessadosList.push_back(image.getImagem());
		}
		if(rostosProcessadosList.size() <= 0) {throw new Exception("Não foi encontrado nenhum rosto no vetor de imagens");}
		
		
		
		//if(rostosProcessadosList.size() % 2 != 0) { rostosProcessadosList.pop_back();}
		recognizer.train(rostosProcessadosList,labels);
		return recognizer;
	}
	/**Cria um novo modelo de Fisher e realiza o treino para um conjunto de imagens src
	 * @param src Vetor de imagens Mat para ser usado na criação do modelo
	 * @return FaceRecognizer treinado
	 */
	@Override
	public FaceRecognizer trainRaw(List<Imag> imagens) throws Exception {
		FaceRecognizer recognizer = FisherFaceRecognizer.create();
		return trainRaw(recognizer, imagens);
	}
	/**Carrega um modelo de Fisher e realiza o treino para um conjunto de imagens src
	 * @param src Vetor de imagens Mat para ser usado na criação do modelo
	 * @param modelPath diretorio do modelo FisherFace
	 * @return FaceRecognizer treinado
	 */
	@Override
	public FaceRecognizer trainRaw(String modelPath, List<Imag> imagens) throws Exception{
		FaceRecognizer recognizer = FisherFaceRecognizer.create();
		recognizer.read(modelPath);
		return trainRaw(recognizer, imagens);
	}
	
	/**
	 * Processa imagem de acordo com o padrão Fisher e retorna o valor de precisao da imagem
	 * com o modelo FaceRecognizer
	 * @param recog modelo treinado FisherFace
	 * @param imagem para ser processada e testada
	 * @param facePrinc posição da face principal
	 * @return valor de precisão com a imagem*/
	@Override
	public double identificarRosto(FaceRecognizer recog, Imag imagem)throws Exception{
		int[] label = new int[] {1};
		double[] predic = new double[] {1.1};
		

		recog.predict(processImage(imagem.getImagem(), imagem.getRostoPrinc()),
				label, predic);
		System.out.println(label[0] + "  prediction");
		System.out.println(predic[0]+ " confianca");
		return (int)label[0];
	}
	
	
	
	private void releaseResources(Pointer... args) {
			for(Pointer arg : args) {
				try {
					arg.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
	}
	private void releaseResources(Imag... args) {
		for(Imag arg : args) {
			try {
				arg.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
}
}
