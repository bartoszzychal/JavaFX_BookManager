package com.bartoszzychal.bookManager.dataProvider;

import java.util.Collection;

import com.bartoszzychal.bookManager.dataProvider.data.BookVO;
import com.bartoszzychal.bookManager.dataProvider.impl.DataProviderImpl;

public interface DataProvider {
	DataProvider INSTANCE = new DataProviderImpl();
	Collection<BookVO> findBooks(String title, String author);
	BookVO addNewBooks(BookVO books);
}
