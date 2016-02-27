package anki;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import jisho.Word;

public class CSVExporter {
	private ArrayList<Word> wordList;
	private Set<String> ignoredPos;
	
	public CSVExporter(ArrayList<Word> wordList) {
		this.wordList = wordList;
		
		ignoredPos = new HashSet<>();
		ignoredPos.add("Wikipedia definition");
		ignoredPos.add("Place");
	}
	
	public void export(String filename) {
		PrintWriter csvPrinter = null;
		int cnt = 1;
		try {
			csvPrinter = new PrintWriter(new File(filename));
			for (Word w : wordList) {
				StringBuilder english = new StringBuilder();
				int nbrIgnored = 0;
				
				for (int i = 0; i < w.getEnglish().size(); i++) {
					if (isIgnoredPos(w.getPartsOfSpeech().get(i))) {
						nbrIgnored++;
						continue;
					}
					english.append(i + 1 - nbrIgnored + ". " + w.getEnglish().get(i) + "</br>");
				}
				
				csvPrinter.println(cnt++ + ";" + w.getKanji() + ";" + w.getKana() + ";" + english.toString());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			csvPrinter.close();
		}
	}
	
	private boolean isIgnoredPos(String pos) {
		return ignoredPos.contains(pos);
	}
}
