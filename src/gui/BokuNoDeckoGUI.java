package gui;

import java.util.ArrayList;

import jisho.Word;
import jisho.WordDownloader;

public class BokuNoDeckoGUI {
	public static void main(String[] args) {
		WordDownloader wordDownloader = new WordDownloader();
		wordDownloader.downloadWords("水");
		ArrayList<Word> wordList = wordDownloader.getWordList();
		
		System.out.println("Number of words found: " + wordList.size());
		
		for (Word w : wordList) {
			System.out.println(w.getKanji() + " " + w.getKana() + " " + w.getEnglish());
		}
	}
}
