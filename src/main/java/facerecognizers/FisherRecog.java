package facerecognizers;

import java.io.File;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.global.opencv_imgcodecs;
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
import com.source.model.Imag;

import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;



/**
 * @author Eduardo
 * Esta classe contem todos os recursos necessarios para processamento de imagem, treinamento de modelo, e identificação facial
 * Os metodo contidos nesta classe não foram projetados com desempenho em mente mas sim de facil utilização, para uma implementação que oferece maior
 * desempenho é necessario utilizar processamento com GPU e metodos customizados para aplicações especificas.
 */
public class FisherRecog {

	
	
	private List<Imag> imagensTrain = new ArrayList<Imag>();
	private FaceRecognizer recognizer;
	private CascadeClassifier cas = new CascadeClassifier();

	public static int resizeRows = 150;
	public static int resizeColumn = 150;

	public FisherRecog() {
		loadClassifiers("com/classifiers/haar", cas).start();
		recognizer = FisherFaceRecognizer.create();

	}

	public FisherRecog(String modelPath) throws Exception{
		loadClassifiers("com/classifiers/haar", cas).start();
		recognizer = FisherFaceRecognizer.create();
		recognizer.read(modelPath);
	}
	
	public CascadeClassifier getCascadeClassifier() {
		return cas;
	}
	
	public void setCascadeClassifier(CascadeClassifier cas) {
		this.cas = cas;
	}
	/**
	 * Atualiza o modelo Fisherface com um modelo ja existente
	 * @param modelPath diretorio do modelo
	 * @throws Exception*/
	public void readModel(String modelPath)throws Exception{
		recognizer.read(modelPath);
	}
	/**
	 * Seleciona um novo valor do threshold do modelo
	 * @param double threshold
	 * */
	public void setThreshold(double threshold) {
		recognizer.setThreshold(threshold);
	}
	/**
	 * Obtem o threshold do modelo
	 * @return double valor do threshold*/
	public double getThreshold() {
		return recognizer.getThreshold();
	}
	
	
	/**
	 * Treina um modelo Fisherface dado um vetore de imagens Mat sem procesasmento
	 * Este metodo irá utilizar somente o rosto com a maior resolução
	 * 
	 * @param recognizer
	 * @return Retorna FaceRecognizer treinado
	 * @throws Exception
	 */
	public FaceRecognizer startTrain() throws Exception {
		System.out.println(imagensTrain.size() + "  lengh1");
		if (imagensTrain.size() <= 0) {
			throw new Exception("Vetores de imagem não pode estar vazio");
		}

		List<Imag> imagensProc = new ArrayList<Imag>();
		System.out.println("Metodo trainRaw");
		System.out.println("Input image rows " + imagensTrain.get(0).getImagem().rows());
		System.out.println("input channels " + imagensTrain.get(0).getImagem().channels());
		for (Imag imagem : imagensTrain) {

				// Caso a imagem já tenha sido processada
			if (imagem.isProces()){
				imagensProc.add(imagem);
			}
			
		}
		
		if(imagensProc.size() == 0) {
			throw new Exception("Nenhum imagem fornecida contem rostos");
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

		for(Mat imagem : vectorImagens.get()) {
			Mat a = new Mat();
			opencv_imgproc.cvtColor(imagem, a, opencv_imgproc.COLOR_GRAY2BGR);
			Utilitarios.showImage(a);
		}
		
		System.out.println(vectorImagens.size() + " " + labels.rows());
		recognizer.train(vectorImagens, labels);
		releaseResources(labels);
		releaseResources(vectorImagens);
		return recognizer;
	}

	/**
	 * Adiciona imagens para lista treinamento caso imagens não foram processadas
	 * serão processadas neste metodo
	 * 
	 * @param imagens Lista de imagens processadas
	 * @return
	 * @throws Exception
	 */
	public void addImagensTrain(List<Imag> imagens) throws Exception {
		for (Imag img : imagens) {
			if(img.isProces()) {
				imagensTrain.add(img);
			}else {
				detectFaceAndMainFaceProcess(img);
				imagensTrain.add(img);
			}
			
		}
	}

	public void setLabel(Integer id, String label) {
		recognizer.setLabelInfo(id, label);
	}

	public BytePointer getLabelInfo(int label) {
		return recognizer.getLabelInfo(label);
		
	}
	/**
	 * Processa imagem de teste com o padrão FisherFace e retorna a label
	 * correspondente e seu nivel de precisão.
	 * Atenção este metodo ira processar o objeto Mat contendo a imagem.
	 * 
	 * @param imagem para ser processada e testada
	 * @return int[]{label,precisao}
	 */
	public int[] identificarRosto(Imag imagem) throws Exception {
		int[] label = new int[] { 1 };
		double[] predic = new double[] { 1.1 };

		if(imagem.isProces()) {
			recognizer.predict(imagem.getImagem(), label, predic);
			System.out.println(label[0] + "  prediction");
			System.out.println(predic[0] + " confianca");
			return new int[] { label[0], (int) predic[0] };
		}else {
			detectFaceAndMainFaceProcess(imagem);
			if(imagem.isProces()) {
				recognizer.predict(imagem.getImagem(), label, predic);
				System.out.println(label[0] + "  prediction");
				System.out.println(predic[0] + " confianca");
				return new int[] { label[0], (int) predic[0] };
			}else { throw new Exception("Imagem de input não contem faces");}
		}

	}

	/**
	 * Recorta o rosto detectado
	 * @param posicaoRosto Rect contendo as cordenadas do rosto á ser cortado
	 * @param imagem imagem para ser recortada
	 * @return Mat imagem já recortada
	 * @Override do
	 */
	public Mat recortarRosto(Rect posicaoRosto, Mat imagem) {

		Rect imgCrop = new Rect(posicaoRosto.x(), posicaoRosto.y(), posicaoRosto.width(), posicaoRosto.height());
		Mat imagemRec = new Mat(imagem, imgCrop);
		return imagemRec;
	}

	/**
	 * Processa a imagem de acordo com os padroes LBPH detectRostoPrincipal,
	 * recortarRosto, resize para 1000 pixels e converte para BGR2GRAY
	 * 
	 * @param imagem        para ser processada
	 * @param facePrincipal Rec da face principal
	 * @throws Exception
	 */
	@SuppressWarnings("resource")
	public Mat processImage(Mat imagem, Rect facePrincipal) throws Exception {
		System.out.println("Metodo processImage");
		System.out.println("Input image rows " + imagem.rows());
		System.out.println("input channels " + imagem.channels());
		System.out.println("input type " + imagem.type());
		Mat imgProc = imagem.clone();

		// Caso a imagem contenha um rosto realiza o processamento
		if (facePrincipal.width() > 0 && facePrincipal.height() > 0) {
			// Recorta o rosto

			if (imgProc.rows() > resizeRows && imgProc.cols() > resizeColumn) {
				// Ajusta o tamanho
				imgProc = recortarRosto(facePrincipal, imgProc);
				opencv_imgproc.resize(imgProc, imgProc, new Size(resizeRows, resizeColumn), 1.0, 1.0, opencv_imgproc.INTER_CUBIC);
			}

			if (imgProc.type() > 0) {
				opencv_imgproc.cvtColor(imgProc, imgProc, opencv_imgproc.COLOR_BGR2GRAY);
			}
			
		} else {
			imgProc.close();
			facePrincipal.close();
			throw new Exception("Imagem não contem faces");
		}
		System.out.println("facesDetectadas " + facePrincipal.width());
		System.out.println("output image rows " + imagem.rows());
		System.out.println("output channels " + imagem.channels());
		return imgProc;
	}


	/**
	 * Este metodo realiza todas as detecções necessarias e processamentos para que
	 * a imagem esteja no padrão para treinamento. Caso a imagem estiver dentro dos
	 * padrões de treinamento a flag Imag.proces será true.
	 * 
	 * Atenção este metodo não retorna um novo objeto Imag mas sim utiliza o objeto
	 * passado como parametro, Objeto Mat contendo a imagem sera processado e perdera suas caracteristicas originais
	 * 
	 * @param imagem Objeto imagem fora dos padrões
	 * @throws Exception
	 */
	public void detectFaceAndMainFaceProcess(Imag imagem) throws Exception {

		// Detecta os rostos de uma imagem
		imagem.setRostos(Utilitarios.detectFaces(cas, imagem.getImagem()));

		if (imagem.getRostos().size() > 0) { // Caso quantidade de rostos detectado seja maior que 0

			// Detecta o rosto principal
			imagem.setRostoPrinc(Utilitarios.detectFacePrincipal(imagem.getRostos()));
			System.out.println(imagem.getRostoPrinc().isNull());

			// Caso rosto principal possuir um tamanho ideia para processamento
			if (imagem.getRostoPrinc().height() >= resizeRows && imagem.getRostoPrinc().width() >= resizeColumn) {

				// Processa a imagem
				imagem.setImagem(processImage(imagem.getImagem(), imagem.getRostoPrinc()));
				imagem.setProces(true);

			} else
				imagem.setProces(false);
		} else
			imagem.setProces(false);
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

	/**
	 *Fecha o modelo, no momento que este metodo for chamado não será possivel utilizar outros metodos da classe,
	 *para a reutilização desta classe será necessario carregar o modelo novamente FisherRecog.readModel(modelPath) */
	public void closeModel() {
		recognizer.close();
	}
	
	private List<Imag> convertFilesToImag(List<File> files, Integer label, String descri) {
		List<Imag> imagens = new ArrayList<Imag>();
		for (File file : files) {
			imagens.add(new Imag(label, descri, null, opencv_imgcodecs.imread(file.getAbsolutePath()), false,
					new RectVector(), new Rect()));
		}
		return imagens;
	}

	private synchronized Thread loadClassifiers(String folderPath, CascadeClassifier cascas) {

		Task<Integer> loadClassifiers = new Task<Integer>() {

			@Override
			protected Integer call() throws Exception {
				File file;
				try {
					file = new File(getClass().getClassLoader().getResource(folderPath).toURI());
					File[] files = file.listFiles();
					for (File fier : files) {
						System.out.println(fier.getPath());
						cascas.load(fier.getPath().toString());
					}
				} catch (Exception e) {

					new Alert(AlertType.ERROR, "Falha ao carregar Classificadores").showAndWait();
					e.printStackTrace();
					System.exit(0);
				}
				return null;
			}
		};
		return new Thread(loadClassifiers);
	}
}
