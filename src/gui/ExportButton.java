package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import anki.CSVExporter;
import jisho.WordDownloader;

@SuppressWarnings("serial")
public class ExportButton extends JButton implements ActionListener {
	private WordDownloader wordDownloader;
	private CSVExporter exporter;
	
	public ExportButton(WordDownloader wordDownloader) {
		this.wordDownloader = wordDownloader;
		
		exporter = new CSVExporter();
		
		setText("Export CSV");
		addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		exporter.export(wordDownloader.getWordList(), "res.csv");
		System.out.println("Export finished");
	}
}
