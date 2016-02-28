package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import jisho.WordDownloader;

@SuppressWarnings("serial")
public class DownloadButton extends JButton implements ActionListener {
	private WordDownloader wordDownloader;
	private DownloadTagPanel downloadTagPanel;
	
	public DownloadButton(WordDownloader wordDownloader, DownloadTagPanel downloadTagPanel) {
		this.wordDownloader = wordDownloader;
		this.downloadTagPanel = downloadTagPanel;
		
		setText("Download");
		addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (downloadTagPanel.getDownloadTags().equals(DownloadTagPanel.NONE_TAG)) {
			System.out.println("ERROR: No tags selected");
			return;
		}
		
		wordDownloader.initializeWordList(downloadTagPanel.getDownloadTags());
		System.out.println("Number of words found: " +  wordDownloader.getWordList().size());
	}
}
