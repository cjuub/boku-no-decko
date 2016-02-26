package jisho;

import java.util.ArrayList;

public class Word {
	private String kanji;
	private String kana;
	private ArrayList<String> english;

	public Word(String kanji, String kana,  ArrayList<String> english) {
		this.kanji = kanji;
		this.kana = kana;
		this.english = english;
	}

	public String getKanji() {
		return kanji;
	}

	public String getKana() {
		return kana;
	}

	public  ArrayList<String> getEnglish() {
		return english;
	}

}
