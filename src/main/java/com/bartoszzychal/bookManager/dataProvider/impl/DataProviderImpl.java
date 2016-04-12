package com.bartoszzychal.bookManager.dataProvider.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.json.HTTPTokener;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.bartoszzychal.bookManager.bookMapper.BookJSONMapper;
import com.bartoszzychal.bookManager.dataProvider.DataProvider;
import com.bartoszzychal.bookManager.dataProvider.data.BookVO;

public class DataProviderImpl implements DataProvider {
	public static final Logger LOG = Logger.getLogger(DataProvider.class);

	private final String FIND_BOOKS_URL = "http://localhost:9721/workshop/books-by";
	private final String TITLE = "?title=";
	private final String AUTHOR = "&authors=";
	private final String ADD_BOOK_URL = "http://localhost:9721/workshop/book?action=save";

	@Override
	public Collection<BookVO> findBooks(String title, String author) {
		title = setDefaultIfNULL(title);
		author = setDefaultIfNULL(author);
		LOG.warn("findBooks()");
		LOG.debug("Search by title: " + " and by author: " + author);
		Collection<BookVO> books = null;
		try {
			URL url = new URL(FIND_BOOKS_URL+TITLE+title+AUTHOR+author);
			LOG.warn("Created url: "+url.toString());
			JSONTokener tokener = new JSONTokener(new InputStreamReader(url.openStream(), "utf-8"));
			JSONArray jsonArray = new JSONArray(tokener);
			books = BookJSONMapper.mapJSONArray(jsonArray);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return books;
	}

	private String setDefaultIfNULL(String string) {
		if(string == null){
			string = "";
		}
		return string;
	}

	@Override
	public BookVO addNewBooks(BookVO bookVO) {
		JSONObject objectToSend = BookJSONMapper.map(bookVO);
		HttpURLConnection connection = null;
		BookVO responseBook = null;
		try {
			connection = preparePostConnecting(new URL(ADD_BOOK_URL));
			sendObject(objectToSend, connection);
			responseBook = checkResponse(connection); 
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			connection.disconnect();
		}
		return responseBook;
	}

	private HttpURLConnection preparePostConnecting(URL url) throws IOException, ProtocolException {
		HttpURLConnection connection;
		connection = (HttpURLConnection) url.openConnection();
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setRequestProperty("Accept", "application/json");
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);
		return connection;
	}


	private void sendObject(JSONObject objectToSend, HttpURLConnection connection) throws IOException {
		OutputStreamWriter output = new OutputStreamWriter(connection.getOutputStream());
		output.write(objectToSend.toString());
		output.flush();
	}

	private BookVO checkResponse(HttpURLConnection connection) throws IOException, UnsupportedEncodingException {
		StringBuilder sb = new StringBuilder();  
		int HttpResult = connection.getResponseCode(); 
		if (HttpResult == HttpURLConnection.HTTP_OK) {
		    BufferedReader br = new BufferedReader(
		            new InputStreamReader(connection.getInputStream(), "utf-8"));
		    String line = null;  
		    while ((line = br.readLine()) != null) {  
		        sb.append(line + "\n");  
		    }
		    br.close();
		    LOG.debug("SAVED BOOK" + sb.toString());  
		} else {
			LOG.debug(connection.getResponseMessage());  
		}
		return BookJSONMapper.map(sb.toString());
	}

}
