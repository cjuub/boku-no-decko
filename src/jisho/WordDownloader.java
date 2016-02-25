package jisho;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class WordDownloader {
	private static final int RESULTS_PER_PAGE = 20;
	private static final String BASE_API_URL =  "http://jisho.org/api/v1/search/words?";
	
	private ArrayList<Word> wordList;

	public WordDownloader() {
		wordList = new ArrayList<Word>();
	}

	public void downloadWords(String keywords) {
		String query = "keyword=" + keywords + "&page=";
		
		HttpURLConnection urlConnection = null;
		boolean pageHasMoreData = true;
		int pageCount = 1;
		try {
			while (pageHasMoreData) {
				URL url = new URL(BASE_API_URL + query + pageCount++);
				urlConnection = (HttpURLConnection) url.openConnection();
				
				BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
				String page = br.readLine();
				for (int i = 0; i < RESULTS_PER_PAGE; i++) {
					pageHasMoreData = parsePageJson(page, i);
					
					if (!pageHasMoreData) {
						break;
					}
				}
				
				urlConnection.getInputStream().close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				urlConnection.getInputStream().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private boolean parsePageJson(String page, int index) {
		JsonObject jsonObject = new JsonParser().parse(page).getAsJsonObject();
		JsonArray dataArray = jsonObject.get("data").getAsJsonArray();
		
		if (dataArray.size() <= index || dataArray.size() == 0) {
			return false;
		}
		
		JsonElement dataElement = dataArray.get(index);
		JsonElement japaneseElement = dataElement.getAsJsonObject().get("japanese").getAsJsonArray().get(0);
		JsonElement sensesElement = dataElement.getAsJsonObject().get("senses").getAsJsonArray().get(0);
		JsonElement englishElement = sensesElement.getAsJsonObject().get("english_definitions").getAsJsonArray().get(0);
		
		String kanji = japaneseElement.getAsJsonObject().get("word").getAsString();
		String kana = japaneseElement.getAsJsonObject().get("reading").getAsString();
		String english = englishElement.getAsString();
		
		wordList.add(new Word(kanji, kana, english));
		return true;
	}

	public ArrayList<Word> getWordList() {
		return wordList;
	}
}
