package gui;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import jisho.WordDownloader;

@SuppressWarnings("serial")
public class ButtonPanel extends JPanel {
	public ButtonPanel(WordDownloader wordDownloader) {
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		DownloadButton downloadButton = new DownloadButton(wordDownloader);
		ExportButton exportButton = new ExportButton(wordDownloader);
		
		add(downloadButton);
		add(exportButton);
	}
}
