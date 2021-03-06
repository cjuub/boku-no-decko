package jisho;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class WordDownloader {
	private static final String CACHE_FOLDER = "cache";
	private static final String BASE_API_URL = "http://jisho.org/api/v1/search/words?";
	
	private ArrayList<Word> wordList;

	public WordDownloader() {
		wordList = new ArrayList<Word>();
	}
	
	public void initializeWordList(String keywords) {
		keywords = keywords.replace("#", "%23");
		
		if (!getCachedWords(keywords)) {
			downloadWords(keywords, 1);
		}
	}

	private boolean getCachedWords(String keywords) {
		String digest = getMD5(keywords);
		File cacheFile = new File(CACHE_FOLDER, digest + ".json");
		if (!cacheFile.exists()) {
			return false;
		}
		
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(cacheFile)));

			String page = null;
			int pageNbr = 1;
			int entryNbr = 0;
			while ((page = br.readLine()) != null) {
				pageNbr++;
				entryNbr = 0;
				while (parsePageJson(page, entryNbr++));
			}
			
			// Download more pages in case cache ended prematurely
			if (hasPage(keywords, pageNbr)) {
				downloadWords(keywords, pageNbr);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
		return true;
	}

	public boolean hasPage(String keywords, int pageNbr) {
		String query = "keyword=" + keywords + "&page=" + pageNbr;
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(BASE_API_URL + query);
			urlConnection = (HttpURLConnection) url.openConnection();
			BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
			String page = br.readLine();
			
			return parsePageJson(page, 0);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				urlConnection.getInputStream().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return false;
	}

	public void downloadWords(String keywords, int pageNbr) {
		String query = "keyword=" + keywords + "&page=";
		
		File cacheFolder = new File(CACHE_FOLDER);
		if (!cacheFolder.exists()) {
			cacheFolder.mkdirs();
		}
		
		String digest = getMD5(keywords);
		File cacheFile = new File(CACHE_FOLDER, digest + ".json");

		HttpURLConnection urlConnection = null;
		PrintWriter cachePrinter = null;
		
		boolean hasMorePages = true;
		
		try {
			cachePrinter = new PrintWriter(new FileOutputStream(cacheFile, true));

			while (hasMorePages) {
				URL url = new URL(BASE_API_URL + query + pageNbr++);
				urlConnection = (HttpURLConnection) url.openConnection();
				
				BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
				String page = br.readLine();
				
				cachePrinter.println(page);
				
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
				cachePrinter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private String getMD5(String keywords) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		}
		
		md.update(keywords.getBytes());
		byte[] digestBytes = md.digest();
		StringBuilder digest = new StringBuilder();
		
		for (byte b : digestBytes) {
			if ((b & 0xFF) < 0x10) {
				digest.append("0");
			}
			
			digest.append(Integer.toHexString(b & 0xFF));
		}
		
		return digest.toString();
	}
	
	private boolean parsePageJson(String page, int pageEntry) {
		JsonObject jsonObject = new JsonParser().parse(page).getAsJsonObject();
		JsonArray dataArray = jsonObject.get("data").getAsJsonArray();
		
		if (dataArray.size() <= pageEntry || dataArray.size() == 0) {
			return false;
		}
		
		JsonElement dataElement = dataArray.get(pageEntry);
		JsonElement japaneseElement = dataElement.getAsJsonObject().get("japanese").getAsJsonArray().get(0);
		JsonArray sensesArray = dataElement.getAsJsonObject().get("senses").getAsJsonArray();
		
		JsonElement kanjiElement = japaneseElement.getAsJsonObject().get("word");
		JsonElement readingElement = japaneseElement.getAsJsonObject().get("reading");
		if (readingElement == null) {
			return false;
		}
		
		String kana = readingElement.getAsString();
		String kanji = kanjiElement != null ? kanjiElement.getAsString() : kana;
			
		ArrayList<String> english = new ArrayList<String>();
		ArrayList<String> pos = new ArrayList<String>();
		for (int i = 0; i < sensesArray.size(); i++) {
			JsonArray englishArray = sensesArray.get(i).getAsJsonObject().get("english_definitions").getAsJsonArray();
			JsonArray posArray = sensesArray.get(i).getAsJsonObject().get("parts_of_speech").getAsJsonArray();
			
			StringBuilder posBuilder = new StringBuilder();
			for (int j = 0; j < posArray.size(); j++) {
				if (!posArray.get(j).isJsonNull()) {
					posBuilder.append(posArray.get(j).getAsString());
				}
			}
			
			if (posArray.size() == 0 && pos.size() == 0) {
				pos.add("None");
			} else if (posArray.size() == 0) {
				pos.add(pos.get(pos.size() - 1));
			} else {
				pos.add(posBuilder.toString());
			}
			
			StringBuilder englishMeaning = new StringBuilder(); 
			for (int j = 0; j < englishArray.size(); j++) {
				englishMeaning.append(englishArray.get(j).getAsString());
				if (j != englishArray.size() - 1) {
					englishMeaning.append(", ");
				}
			}
			
			english.add(englishMeaning.toString());
		}
		
		wordList.add(new Word(kanji, kana, english, pos));
		return true;
	}

	public ArrayList<Word> getWordList() {
		return wordList;
	}
}
