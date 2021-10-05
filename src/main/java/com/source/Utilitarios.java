/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.source;

import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;

import javafx.scene.effect.ImageInput;
import javafx.scene.image.Image;

/**
 *
 * @author Jones
 */
public class Utilitarios {
    
    public BufferedImage convertMatToImage(org.bytedeco.opencv.opencv_core.Mat grabbedImage) {
        int type = BufferedImage.TYPE_BYTE_GRAY;
        if (grabbedImage.channels() > 1) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }

        int bufferSize = grabbedImage.channels() * grabbedImage.cols() * grabbedImage.rows();
        byte[] bytes = new byte[bufferSize];
        BufferedImage imagem = new BufferedImage(grabbedImage.cols(), grabbedImage.rows(), type);
        byte[] targetPixels = ((DataBufferByte) imagem.getRaster().getDataBuffer()).getData();
        System.arraycopy(bytes, 0, targetPixels, 0, bytes.length);
        return imagem;
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
