package com.view.models;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import javax.swing.SwingConstants;
import java.awt.Font;

public class SplashScreen extends JFrame{

	private static final long serialVersionUID = 1L;

	public SplashScreen() {
		super("Aguarde");
		setSize(256,117);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setResizable(false);
		JLabel lblNewLabel = new JLabel("Iniciando aplicação, Aguarde");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		getContentPane().add(lblNewLabel, BorderLayout.CENTER);
		setVisible(true);
	}
	
	public void close() {
		dispose();
	}
}
