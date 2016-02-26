package gui;

import java.util.ArrayList;

import jisho.Word;
import jisho.WordDownloader;

public class BokuNoDeckoGUI {
	public static void main(String[] args) {
		WordDownloader wordDownloader = new WordDownloader();
		wordDownloader.initializeWordList("水");
		ArrayList<Word> wordList = wordDownloader.getWordList();
		
		System.out.println("Number of words found: " + wordList.size());
		
		for (Word w : wordList) {
			String english = "";
			for (String s : w.getEnglish()) {
				english += s + ";";
			}
			System.out.println(w.getKanji() + " " + w.getKana() + " " + english);
		}
	}
}
