package jisho;

import java.util.ArrayList;

public class Word {
	private String kanji;
	private String kana;
	private ArrayList<String> english;
	private ArrayList<String> partsOfSpeech;

	public Word(String kanji, String kana,  ArrayList<String> english, ArrayList<String> partsOfSpeech) {
		this.kanji = kanji;
		this.kana = kana;
		this.english = english;
		this.partsOfSpeech = partsOfSpeech;
	}

	public String getKanji() {
		return kanji;
	}

	public String getKana() {
		return kana;
	}

	public ArrayList<String> getEnglish() {
		return english;
	}

	public ArrayList<String> getPartsOfSpeech() {
		return partsOfSpeech;
	}
}
