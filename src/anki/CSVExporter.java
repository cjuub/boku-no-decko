package anki;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

import jisho.Word;

public class CSVExporter {
	private ArrayList<Word> wordList;
	
	public CSVExporter(ArrayList<Word> wordList) {
		this.wordList = wordList;
	}
	
	public void export(String filename) {
		PrintWriter csvPrinter = null;
		int cnt = 0;
		try {
			csvPrinter = new PrintWriter(new File(filename));
			for (Word w : wordList) {
				StringBuilder english = new StringBuilder();
				for (String s : w.getEnglish()) {
					english.append(s + ", ");
				}
				
				csvPrinter.println(cnt++ + ";" + w.getKanji() + ";" + w.getKana() + ";" + english.toString());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			csvPrinter.close();
		}
	}
}
