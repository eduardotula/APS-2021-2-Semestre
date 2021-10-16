package com.source.control;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.IntBuffer;

import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.MatVector;
import org.bytedeco.opencv.opencv_face.EigenFaceRecognizer;
import org.bytedeco.opencv.opencv_face.FaceRecognizer;
import org.bytedeco.opencv.opencv_face.FisherFaceRecognizer;
import org.bytedeco.opencv.opencv_face.LBPHFaceRecognizer;

import javafx.stage.FileChooser;

public class jjj {

	public static void main(String[] args) {
        Mat testImage = opencv_imgcodecs.imread("C:/aaa.png", opencv_imgcodecs.IMREAD_GRAYSCALE);

        File root = new File("C:/imgs");

        FilenameFilter imgFilter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                name = name.toLowerCase();
                return name.endsWith(".jpg") || name.endsWith(".pgm") || name.endsWith(".png");
            }
        };

        File[] imageFiles = root.listFiles(imgFilter);

        MatVector images = new MatVector(imageFiles.length);

        Mat labels = new Mat(imageFiles.length, 1, opencv_core.CV_32SC1);
        IntBuffer labelsBuf = labels.createBuffer();

        int counter = 0;

        for (File image : imageFiles) {
            Mat img = opencv_imgcodecs.imread(image.getAbsolutePath(), opencv_imgcodecs.IMREAD_GRAYSCALE);

            int label = Integer.parseInt(image.getName().split("\\-")[0]);

            images.put(counter, img);

            labelsBuf.put(counter, label);

            counter++;
        }

        FaceRecognizer faceRecognizer = FisherFaceRecognizer.create();
         //FaceRecognizer faceRecognizer = EigenFaceRecognizer.create();
        //FaceRecognizer faceRecognizer = LBPHFaceRecognizer.create();

        System.out.println(images.size());
        System.out.println(labels.arrayHeight());
        faceRecognizer.train(images, labels);
        faceRecognizer.write("C:/imgs/pepega.yml");
        IntPointer label = new IntPointer(1);
        DoublePointer confidence = new DoublePointer(1);
        faceRecognizer = null;
        faceRecognizer = EigenFaceRecognizer.create();
        faceRecognizer.read("C:/imgs/pepega.yml");
        faceRecognizer.predict(testImage, label, confidence);
        int predictedLabel = label.get(0);
        System.out.println(confidence.get());
        System.out.println("Predicted label: " + predictedLabel);
    }
}
