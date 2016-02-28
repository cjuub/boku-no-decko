package gui;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class DownloadTagPanel extends JPanel {
	public static final String NONE_TAG = "NONE";
	
	private static final String COMMON_TAG = "#common";
	
	private JCheckBox commonBox;
	
	public DownloadTagPanel() {
		commonBox = new JCheckBox(COMMON_TAG);
		
		add(commonBox);
	}
	
	public String getDownloadTags() {
		if (commonBox.isSelected()) {
			return COMMON_TAG;
		}
		
		return NONE_TAG;
	}
}
