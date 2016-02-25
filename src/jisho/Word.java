package jisho;

public class Word {
	private String kanji;
	private String kana;
	private String english;

	public Word(String kanji, String kana, String english) {
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

	public String getEnglish() {
		return english;
	}
}
