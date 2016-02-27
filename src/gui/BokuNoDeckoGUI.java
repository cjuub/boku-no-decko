package gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import jisho.WordDownloader;

@SuppressWarnings("serial")
public class BokuNoDeckoGUI extends JFrame {
	
	public BokuNoDeckoGUI(WordDownloader wordDownloader) {
		ButtonPanel buttonPanel = new ButtonPanel(wordDownloader);
		
		add(buttonPanel, BorderLayout.SOUTH);
		
		pack();
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public static void main(String[] args) {
		WordDownloader wordDownloader = new WordDownloader();
		new BokuNoDeckoGUI(wordDownloader);
	}
}
