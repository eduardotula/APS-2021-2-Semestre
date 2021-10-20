package facerecognizers;

import java.io.File;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.bytedeco.javacpp.Pointer;
import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.MatVector;
import org.bytedeco.opencv.opencv_core.Rect;
import org.bytedeco.opencv.opencv_core.RectVector;
import org.bytedeco.opencv.opencv_face.FaceRecognizer;
import org.bytedeco.opencv.opencv_face.FisherFaceRecognizer;
import org.bytedeco.opencv.opencv_face.LBPHFaceRecognizer;
import org.bytedeco.opencv.opencv_objdetect.CascadeClassifier;
import org.bytedeco.opencv.opencv_xfeatures2d.SURF;

import com.source.control.Utilitarios;
import com.source.model.Imag;

public class FisherRecog extends FaceRecog {

	private CascadeClassifier cas;
	private List<Imag> imagensTrain = new ArrayList<Imag>();
	private FaceRecognizer recognizer;
	
	public FisherRecog(CascadeClassifier cas) {
		this.cas = cas;
		recognizer = FisherFaceRecognizer.create();
	}

	/**
	 * Treina um modelo Fisherface dado um vetore de imagens Mat sem procesasmento
	 * Este metodo irá utilizar somente o rosto com a maior resolução
	 * 
	 * @param recognizer
	 * @return Retorna FaceRecognizer treinado
	 * @throws Exception
	 */
	public FaceRecognizer startTrain(FaceRecognizer recognizer) throws Exception {
		System.out.println(imagensTrain.size() + "  lengh1");
		if (imagensTrain.size() <= 0) {
			throw new Exception("Vetores de imagem não pode estar vazio");
		}


		List<Imag> imagensProc = new ArrayList<Imag>();
		System.out.println("Metodo trainRaw");
		System.out.println("Input image rows " + imagensTrain.get(0).getImagem().rows());
		System.out.println("input channels " + imagensTrain.get(0).getImagem().channels());
		for (Imag imagem : imagensTrain) {

			if (!imagem.isProces() | imagem.getRostos().size() <= 0  ) {
				// Detecta os rostos de uma imagem
				imagem.setRostos(Utilitarios.detectFaces(cas, imagem.getImagem()));
				if (imagem.getRostos().size() > 0) {
					// Detecta o rosto principal
					imagem.setRostoPrinc(detectRostoPrincipal(imagem.getRostos()));
					System.out.println(imagem.getRostoPrinc().isNull());
					if (!imagem.getRostoPrinc().isNull()) {
						imagem.setImagem(processImage(imagem.getImagem(), imagem.getRostoPrinc()));
						imagensProc.add(imagem);

					}
				}

			} else {
				System.out.println(imagem.getRostoPrinc().isNull());
				imagem.setImagem(processImage(imagem.getImagem(), imagem.getRostoPrinc()));
				imagensProc.add(imagem);

			}
		}
		MatVector vectorImagens = new MatVector(imagensProc.size());
		Mat labels = new Mat(imagensProc.size(), 1, opencv_core.CV_32SC1);
		IntBuffer labelsBuf = labels.createBuffer();

		long counter = 0;
		for (Imag imagem : imagensProc) {

			labelsBuf.put((int) counter, imagem.getIdLabel());
			vectorImagens.put(counter, imagem.getImagem());

			counter++;
		}




		System.out.println(vectorImagens.size() + " " + labels.rows());
		recognizer.train(vectorImagens, labels);
		releaseResources(labels);
		releaseResources(vectorImagens);
		return recognizer;
	}



	/**
	 * Adiciona imagens para treinamento
	 * 
	 * @param imagens    Lista de imagens processadas
	 * @return
	 * @throws Exception
	 */
	public void addImagensTrain(List<Imag> imagens) throws Exception {
		for(Imag img : imagens) {
			imagensTrain.add(img);
		}
	}

	/**
	 * Cria um novo modelo de LBPH e realiza o treino para um conjunto de imagens
	 * src
	 * 
	 * @param src Vetor de imagens Mat para ser usado na criação do modelo
	 * @return FaceRecognizer treinado
	 */
	@Override
	public FaceRecognizer trainRaw() throws Exception {
		return startTrain(recognizer);
		
	}


	public void setLabel(Integer id,String label) {
		recognizer.setLabelInfo(id, label);
	}


	/**
	 * Processa imagem de acordo com o padrão LBPH e retorna o valor de precisao da
	 * imagem com o modelo FaceRecognizer
	 * 
	 * @param recog     modelo treinado FisherFace
	 * @param imagem    para ser processada e testada
	 * @param facePrinc posição da face principal
	 * @return valor de precisão com a imagem
	 */
	@Override
	public int[] identificarRosto(FaceRecognizer recog, Imag imagem) throws Exception {
		int[] label = new int[] { 1 };
		double[] predic = new double[] { 1.1 };

		recog.predict(processImage(imagem.getImagem(), imagem.getRostoPrinc()), label, predic);
		System.out.println(label[0] + "  prediction");
		System.out.println(predic[0] + " confianca");
		return new int[] { label[0], (int) predic[0] };
	}

	private void releaseResources(Imag... args) {
		for (Imag arg : args) {
			try {
				arg.close();
			} catch (Exception e) {
			}
		}
	}

	private void releaseResources(Pointer... args) {
		for (Pointer arg : args) {
			try {
				arg.close();
			} catch (Exception e) {
			}
		}
	}

	private List<Imag> convertFilesToImag(List<File> files, Integer label, String descri) {
		List<Imag> imagens = new ArrayList<Imag>();
		for (File file : files) {
			imagens.add(new Imag(label, descri, null, opencv_imgcodecs.imread(file.getAbsolutePath()), false,
					new RectVector(), new Rect()));
		}
		return imagens;
	}
}
