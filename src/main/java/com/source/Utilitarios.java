/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.source;

import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.nio.ByteBuffer;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameConverter;
import org.bytedeco.javacv.JavaFXFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.opencv_core.RectVector;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;

import javafx.scene.effect.ImageInput;
import javafx.scene.image.Image;

/**
 *
 * @author Jones
 */
public class Utilitarios {
    
    public Image convertMatToImage(org.bytedeco.opencv.opencv_core.Mat grabbedImage) {
    	OpenCVFrameConverter.ToMat converter = new OpenCVFrameConverter.ToMat();
    	Frame fram = converter.convert(grabbedImage);
    	JavaFXFrameConverter fxConverter = new JavaFXFrameConverter();
    	Image img =  fxConverter.convert(fram);
    	return img;
    }
    
    public void mostraImagem(BufferedImage imagem) {
        ImageIcon icon = new ImageIcon(imagem);
        JFrame frame = new JFrame("YEP");
        frame.setLayout(new FlowLayout());
        frame.setSize(imagem.getWidth() + 50, imagem.getHeight() + 50);
        JLabel lbl = new JLabel();
        lbl.setIcon(icon);
        frame.add(lbl);
        frame.setVisible(true);
    }

    
}
