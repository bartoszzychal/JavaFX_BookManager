package com.bartoszzychal.bookManager.bookMapper;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.bartoszzychal.bookManager.dataProvider.data.BookVO;

public class BookJSONMapper {
	
	public static final Logger LOG = Logger.getLogger(BookJSONMapper.class);
	
	public static Collection<BookVO> mapJSONArray(JSONArray array) {
		return StreamSupport.stream(array.spliterator(), false).map(BookJSONMapper::mapObjectToJSONObject)
				.map(BookJSONMapper::map).collect(Collectors.toList());

	}

	private static JSONObject mapObjectToJSONObject(Object object) {
		if(object != null){
			return (JSONObject) object;
		}
		return null;
	}
	
	public static BookVO map(JSONObject jsonObject) {
		if(jsonObject != null){
			BookVO bookVO = new BookVO(String.valueOf(jsonObject.getLong("id")), jsonObject.getString("title"), jsonObject.getString("authors"));
			LOG.debug("Parse object (BookVO):" +bookVO);
			return bookVO;
		}
		return null;
	}
	public static BookVO map(String json) {
		if(json != null){
			JSONObject jsonObject = new JSONObject(json);
			return BookJSONMapper.map(jsonObject);
		}
		return null;
	}
	
	public static JSONObject map(BookVO bookVO) {
		if(bookVO != null){
			Map<String, Object> book = new HashMap<>();
			book.put("title", bookVO.getTitle());
			book.put("authors", bookVO.getAuthor());
			JSONObject jsonObject = new JSONObject(book);
			LOG.debug("Parse object (JSON):" + jsonObject);
			return jsonObject;			
		}
		return null;
	}
	
	
}
