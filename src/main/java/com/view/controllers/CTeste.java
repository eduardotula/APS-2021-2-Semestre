package com.view.controllers;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

import javax.persistence.PersistenceException;

import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.MatVector;
import org.bytedeco.opencv.opencv_core.Rect;
import org.bytedeco.opencv.opencv_core.Size;

import com.source.Aplicacao;
import com.source.control.Biometria;
import com.source.control.ControllerBd;
import com.source.control.Utilitarios;
import com.source.model.Acesso;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

public class CTeste {

	@FXML
	public ImageView img;

	@FXML
	public Button btn;
	@FXML
	public TextField campo1;
	@FXML
	public TextField campo2;
	@FXML
	public TextField campo3;
	@FXML
	public TextField campo4;

	Mat imt;
	Mat imt2;

	/**
	 * 
	 */
	@FXML
	public void actBtn() {

		/*
		 * imt = opencv_imgcodecs.
		 * imread("C:\\Users\\eduar\\Desktop\\Train img\\Treinar pack/017_3_2.tif",
		 * opencv_imgcodecs.IMREAD_GRAYSCALE);
		 * 
		 * Mat temp2 = new Mat(); Mat temp3 = new Mat(); Mat temp4 = new Mat();
		 */
		// opencv_imgproc.equalizeHist(imt, imt);
		/*
		 * opencv_imgproc.threshold(imt, imt, 100, 255, opencv_imgproc.THRESH_OTSU);
		 * opencv_imgproc.GaussianBlur(imt, temp2, new Size(0, 0),
		 * Double.parseDouble(campo1.getText())); opencv_core.addWeighted(temp2,
		 * Double.parseDouble(campo2.getText()), imt,
		 * Double.parseDouble(campo3.getText()), Double.parseDouble(campo4.getText()),
		 * temp3); temp4 = Biometria.cropImg(temp3);
		 */

		/*
		 * Rect rec = Biometria.getBigContour(imt); Utilitarios.showImage(new
		 * Mat(imt,rec)); opencv_imgproc.cvtColor(imt, imt,
		 * opencv_imgproc.COLOR_GRAY2BGR);
		 */
		// img.setImage(Utilitarios.convertMatToImage(temp4));

		try {
			ControllerBd.begin();

			List<File> imgs = new FileChooser().showOpenMultipleDialog(null);
			for (File file : imgs) {
				Mat temp = new Mat(opencv_imgcodecs.imread(file.getAbsolutePath(), opencv_imgcodecs.IMREAD_GRAYSCALE));
				System.out.println(file.getName());
				Acesso ass = new Acesso(null, file.getName(), 3);
				Mat temp2 = Biometria.processImagem(temp);
				ass.setImagemMat(temp2);
				ass.setRows(temp2.rows());
				ass.setCol(temp2.cols());
				ass.setImagemByte(ass.getImagemAsByteArr());
				Aplicacao.em.persist(ass);
			}

			ControllerBd.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
