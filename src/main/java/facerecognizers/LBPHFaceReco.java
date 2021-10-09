package facerecognizers;

import java.util.ArrayList;
import java.util.List;

import org.bytedeco.javacpp.Pointer;
import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.MatVector;
import org.bytedeco.opencv.opencv_core.Rect;
import org.bytedeco.opencv.opencv_core.RectVector;
import org.bytedeco.opencv.opencv_core.Size;
import org.bytedeco.opencv.opencv_face.FaceRecognizer;
import org.bytedeco.opencv.opencv_face.FisherFaceRecognizer;
import org.bytedeco.opencv.opencv_face.LBPHFaceRecognizer;
import org.bytedeco.opencv.opencv_objdetect.CascadeClassifier;

import com.source.control.Utilitarios;

public class LBPHFaceReco extends FaceRecog{

	private CascadeClassifier cas;

	public LBPHFaceReco(CascadeClassifier cas) {
		this.cas = cas;
	}


	
	/**Treina um modelo LBPH face dado um vetore de imagens Mat
	 * Este metodo irá utilizar somente o rosto com a maior resolução
	 * 
	 * @param src Vetor de imagens Mat para ser usado na criação do modelo
	 * @return Retorna FaceRecognizer treinado
	 * @throws Exception 
	 * */
	@SuppressWarnings("resource")
	@Override
	public FaceRecognizer train(MatVector src, FaceRecognizer recognizer) throws Exception {
		System.out.println(src.get().length + "  lengh1");
		if(src.get().length <= 0) { throw new Exception("Vetores de imagem não pode estar vazio");}
		Mat labels = new Mat();
		RectVector facesDetectadas = new RectVector();
		List<Mat> rostosProcessadosList = new ArrayList<Mat>();
		MatVector rostosProcessados;
		Rect rostoPrincipal = new Rect();
		System.out.println("quantidade de imagens para treinamento " + src.size());
		
		for(Mat image : src.get()) {
			//Detecta os rostos de uma imagem
			facesDetectadas = Utilitarios.detectFaces(cas, image);
			rostoPrincipal = detectRostoPrincipal(facesDetectadas);
			image = processImage(image, rostoPrincipal);

			rostosProcessadosList.add(image);
			Thread.sleep(5);
		}
		if(rostosProcessadosList.size() % 2 != 0) { rostosProcessadosList.remove(rostosProcessadosList.size()-1);}
		rostosProcessados = new MatVector(rostosProcessadosList.size());
		labels = new Mat(rostosProcessadosList.size(), 1, opencv_core.CV_32SC1);
        //IntBuffer labelsBuf = labels.createBuffer();
		for(int i = 0;i<rostosProcessadosList.size();i++) {
			rostosProcessados.put(i,rostosProcessadosList.get(i));

			//labelsBuf.put(i, label);
			labels.data().put(i, Integer.valueOf(1).byteValue());
			System.out.println(i + "  loop");
		}
		
		if(rostosProcessados.get().length <= 0) {		releaseResources(rostoPrincipal,labels,facesDetectadas,rostosProcessados);
		throw new Exception("Não foi encontrado nenhum rosto no vetor de imagens");}

		recognizer.train(rostosProcessados,labels);
		releaseResources(rostoPrincipal,labels,facesDetectadas);
		return recognizer;
	}
	/**Cria um novo modelo de LBPH e realiza o treino para um conjunto de imagens src
	 * @param src Vetor de imagens Mat para ser usado na criação do modelo
	 * @return FaceRecognizer treinado
	 */
	@Override
	public FaceRecognizer train(MatVector src) throws Exception {
		FaceRecognizer recognizer = FisherFaceRecognizer.create();
		return train(src, recognizer);
	}
	/**Carrega um modelo de LBPH e realiza o treino para um conjunto de imagens src
	 * @param src Vetor de imagens Mat para ser usado na criação do modelo
	 * @param modelPath diretorio do modelo FisherFace
	 * @return FaceRecognizer treinado
	 */
	@Override
	public FaceRecognizer train(MatVector src, String modelPath) throws Exception{
		FaceRecognizer recognizer = FisherFaceRecognizer.create();
		recognizer.read(modelPath);
		return train(src, recognizer);
	}
	
	/**
	 * Processa imagem de acordo com o padrão LBPH e retorna o valor de precisao da imagem
	 * com o modelo FaceRecognizer
	 * @param recog modelo treinado FisherFace
	 * @param imagem para ser processada e testada
	 * @param facePrinc posição da face principal
	 * @return valor de precisão com a imagem*/
	@Override
	public double identificarRosto(FaceRecognizer recog, Mat imagem, Rect facePrinc)throws Exception{
		int[] label = new int[] {1};
		double[] predic = new double[] {1.1};
		
		Mat imgProc = processImage(imagem, facePrinc);
		recog.predict(imgProc, label, predic);
		System.out.println(label[0] + "  prediction");
		System.out.println(predic[0]+ " confianca");
		releaseResources(imgProc);
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
}