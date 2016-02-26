package gui;

import java.util.ArrayList;

import anki.CSVExporter;
import jisho.Word;
import jisho.WordDownloader;

public class BokuNoDeckoGUI {
	public static void main(String[] args) {
		WordDownloader wordDownloader = new WordDownloader();
		wordDownloader.initializeWordList("%23common");
		ArrayList<Word> wordList = wordDownloader.getWordList();
		
		System.out.println("Number of words found: " + wordList.size());
		
		CSVExporter exporter = new CSVExporter(wordList);
		exporter.export("res.csv");
	}
}
