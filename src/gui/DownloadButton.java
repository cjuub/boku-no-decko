package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import jisho.WordDownloader;

@SuppressWarnings("serial")
public class DownloadButton extends JButton implements ActionListener {
	private WordDownloader wordDownloader;
	
	public DownloadButton(WordDownloader wordDownloader) {
		this.wordDownloader = wordDownloader;
		setText("Download");
		addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		wordDownloader.initializeWordList("%23common");
		System.out.println("Number of words found: " +  wordDownloader.getWordList().size());
	}
}
