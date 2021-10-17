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

import com.source.control.Utilitarios;
import com.source.model.Imag;

public class FisherRecog extends FaceRecog {


	private CascadeClassifier cas;

	public FisherRecog(CascadeClassifier cas) {
		this.cas = cas;
	}
	/**
	 * Treina um modelo LBPH face dado um vetore de imagens Mat sem procesasmento
	 * Este metodo irá utilizar somente o rosto com a maior resolução
	 * 
	 * @param src        Lista de imagens Mat para ser usado na criação do modelo
	 * @param recognizer
	 * @return Retorna FaceRecognizer treinado
	 * @throws Exception
	 */
	@Override
	public FaceRecognizer trainRaw(FaceRecognizer recognizer, List<Imag> imagens) throws Exception {
		System.out.println(imagens.size() + "  lengh1");
		if (imagens.size() <= 0) {
			throw new Exception("Vetores de imagem não pode estar vazio");
		}
<<<<<<< master
		

		
=======
		MatVector vectorImagens = new MatVector();
>>>>>>> df93408 Commit
		List<Imag> imagensProc = new ArrayList<Imag>();
		System.out.println("Metodo trainRaw");
		System.out.println("Input image rows " + imagens.get(0).getImagem().rows());
		System.out.println("input channels " + imagens.get(0).getImagem().channels());
		for (int i = 0; i < imagens.size(); i++) {
			Imag imagem = imagens.get(i);

			if (imagem.getRostos().size() <= 0) {
				// Detecta os rostos de uma imagem
				imagem.setRostos(Utilitarios.detectFaces(cas, imagem.getImagem()));
				if (imagem.getRostos().size() > 0) {
					// Detecta o rosto principal
					imagem.setRostoPrinc(detectRostoPrincipal(imagem.getRostos()));
					System.out.println(imagem.getRostoPrinc().isNull());
					if (!imagem.getRostoPrinc().isNull()) {
						temp.setImagem(processImage(imagem.getImagem(), imagem.getRostoPrinc()));
						temp.setIdLabel(imagem.getIdLabel());
						imagensProc.add(temp);
						Mat a = new Mat();
						opencv_imgproc.cvtColor(temp.getImagem(), a, opencv_imgproc.COLOR_GRAY2RGB);
						Utilitarios.showImage(a);
						a.close();
					}
				}

			} else {
				System.out.println(imagem.getRostoPrinc().isNull());
				temp.setImagem(processImage(imagem.getImagem(), imagem.getRostoPrinc()));
				temp.setIdLabel(imagem.getIdLabel());
				imagensProc.add(temp);
				Mat a = new Mat();
				opencv_imgproc.cvtColor(temp.getImagem(), a, opencv_imgproc.COLOR_GRAY2RGB);
				Utilitarios.showImage(a);
				a.close();

			}
		}
<<<<<<< master
		MatVector vectorImagens = new MatVector(imagensProc.size()+1);
		vectorImagens.put(0,new Mat(resizeRows,resizeColumn,opencv_imgproc.COLOR_BGR2GRAY));
		Mat labels = new Mat(imagensProc.size()+1, 1, opencv_core.CV_32SC1);
=======

		Mat labels = new Mat(imagensProc.size(), 1, opencv_core.CV_32SC1);
>>>>>>> df93408 Commit
		IntBuffer labelsBuf = labels.createBuffer();
<<<<<<< master
		labelsBuf.put(0,0);
		
		long counter = 1;
=======
		int counter = 0;
>>>>>>> df93408 Commit
		for (Imag imagem : imagensProc) {

			labelsBuf.put(counter, imagem.getIdLabel());
			vectorImagens.push_back(imagem.getImagem());
			counter++;
		}
		System.out.println(vectorImagens.size() + " " +labels.size());
		recognizer.train(vectorImagens, labels);
		releaseResources(labels);
		releaseResources(temp);
		return recognizer;
	}

	@Override
	public FaceRecognizer train(FaceRecognizer recognizer, List<Imag> imagens) throws Exception {
		if (imagens.get(0).isProces()) {
			// TODO criar metodo
			// recognizer.train(rostosProcessadosList,labels);
		} else {
			throw new Exception("Metodo não aceita imagens não processadas");
		}
		return null;

	}

	/**
	 * Atualiza um modelo ja treinado para reconhecer um rosto novo
	 * 
	 * @param recognizer modelo de FaceRecognizer
	 * @param imagens    Lista de imagens não processadas
	 * @throws Exception
	 */
	@Override
	public FaceRecognizer update(FaceRecognizer recog, List<Imag> imagens) throws Exception {
		if (imagens.get(0).isProces()) {

		} else {
			throw new Exception("Metodo não aceita imagens não processadas");
		}
		return null;
	}

	/**
	 * Atualiza um modelo ja treinado para reconhecer um rosto novo
	 * 
	 * @param recognizer modelo de FaceRecognizer
	 * @param imagens    Lista de imagens processadas
	 * @return
	 * @throws Exception
	 */
	@Override
	public FaceRecognizer updateRaw(FaceRecognizer recognizer, List<Imag> imagens) throws Exception {
		MatVector vectorImagens = new MatVector();
		List<Imag> imagensProc = new ArrayList<Imag>();
		Imag temp = new Imag();
		System.out.println("Metodo updateRaw");
		System.out.println("Input image rows " + imagens.get(0).getImagem().rows());
		System.out.println("input channels " + imagens.get(0).getImagem().channels());
		for (int i = 0; i < imagens.size(); i++) {
			Imag imagem = imagens.get(i);

			if (imagem.getRostos().size() <= 0) {
				// Detecta os rostos de uma imagem
				imagem.setRostos(Utilitarios.detectFaces(cas, imagem.getImagem()));
				if (imagem.getRostos().size() > 0) {
					// Detecta o rosto principal
					imagem.setRostoPrinc(detectRostoPrincipal(imagem.getRostos()));
					if (!imagem.getRostoPrinc().isNull()) {
						temp.setImagem(processImage(imagem.getImagem(), imagem.getRostoPrinc()));
						temp.setIdLabel(imagem.getIdLabel());
						imagensProc.add(temp);
					}
				}

			} else {
				temp.setImagem(processImage(imagem.getImagem(), imagem.getRostoPrinc()));
				temp.setIdLabel(imagem.getIdLabel());
				imagensProc.add(temp);
			}
		}
		Mat labels = new Mat(imagensProc.size(), 1, opencv_core.CV_32SC1);
		IntBuffer labelsBuf = labels.createBuffer();
		int counter = 0;
		for (Imag imagem : imagensProc) {

			labelsBuf.put(counter, imagem.getIdLabel());
			vectorImagens.push_back(imagem.getImagem());

			counter++;
		}
		recognizer.update(vectorImagens, labels);
		releaseResources(labels);
		releaseResources(temp);
		return recognizer;

	}

	/**
	 * Cria um novo modelo de LBPH e realiza o treino para um conjunto de imagens
	 * src
	 * 
	 * @param src Vetor de imagens Mat para ser usado na criação do modelo
	 * @return FaceRecognizer treinado
	 */
	@Override
	public FaceRecognizer trainRaw(List<Imag> imagens) throws Exception {
		FaceRecognizer recognizer = FisherFaceRecognizer.create();
		return trainRaw(recognizer, imagens);
	}

	@Override
	public FaceRecognizer trainRawFiles(List<File> imagens, int label, String descri) throws Exception {
		FaceRecognizer recognizer = FisherFaceRecognizer.create();
		return trainRaw(recognizer, convertFilesToImag(imagens, label, descri));

	}

	/**
	 * Carrega um modelo de LBPH e realiza o treino para um conjunto de imagens src
	 * 
	 * @param src       Vetor de imagens Mat para ser usado na criação do modelo
	 * @param modelPath diretorio do modelo FisherFace
	 * @return FaceRecognizer treinado
	 */
	@Override
	public FaceRecognizer trainRaw(String modelPath, List<Imag> imagens) throws Exception {
		FaceRecognizer recognizer = FisherFaceRecognizer.create();
		recognizer.read(modelPath);
		return trainRaw(recognizer, imagens);
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
