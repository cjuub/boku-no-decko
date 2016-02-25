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
	private static final String BASE_API_URL =  "http://jisho.org/api/v1/search/words?";
	
	private ArrayList<Word> wordList;

	public WordDownloader() {
		wordList = new ArrayList<Word>();
	}

	public void downloadWords(String keywords) {
		String query = "keyword=" + keywords + "&page=";
		
		HttpURLConnection urlConnection = null;
		int pageCount = 1;
		boolean hasMorePages = true;
		try {
			while (hasMorePages) {
				URL url = new URL(BASE_API_URL + query + pageCount++);
				urlConnection = (HttpURLConnection) url.openConnection();
				
				BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
				String page = br.readLine();
				int entryCount = 0;
				boolean pageHasMoreEntries = true;
				while (pageHasMoreEntries) {
					pageHasMoreEntries = parsePageJson(page, entryCount++);
					
					if (!pageHasMoreEntries && entryCount == 1) {
						hasMorePages = false;
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
		JsonArray sensesArray = dataElement.getAsJsonObject().get("senses").getAsJsonArray();
		
		String kanji = japaneseElement.getAsJsonObject().get("word").getAsString();
		JsonElement readingElement = japaneseElement.getAsJsonObject().get("reading");
		if (readingElement == null) {
			return false;
		}
		
		String kana = readingElement.getAsString();
			
		ArrayList<String> english = new ArrayList<String>();
		for (int i = 0; i < sensesArray.size(); i++) {
			JsonArray englishArray = sensesArray.get(i).getAsJsonObject().get("english_definitions").getAsJsonArray();
			StringBuilder englishMeaning = new StringBuilder(); 
			for (int j = 0; j < englishArray.size(); j++) {
				englishMeaning.append(englishArray.get(j).getAsString());
				if (j != englishArray.size() - 1) {
					englishMeaning.append(", ");
				}
			}
			
			english.add(englishMeaning.toString());
		}
		
		wordList.add(new Word(kanji, kana, english));
		return true;
	}

	public ArrayList<Word> getWordList() {
		return wordList;
	}
}
