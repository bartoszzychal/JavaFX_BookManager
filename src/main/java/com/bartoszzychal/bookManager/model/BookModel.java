package com.bartoszzychal.bookManager.model;

import java.util.ArrayList;
import java.util.List;

import com.bartoszzychal.bookManager.dataProvider.data.BookVO;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;

public abstract class BookModel {
	private final StringProperty title = new SimpleStringProperty();
	private final StringProperty author = new SimpleStringProperty();
	private final ListProperty<BookVO> result = new SimpleListProperty<>(
			FXCollections.observableList(new ArrayList<>()));

	public StringProperty getTitleProperty() {
		return title;
	}

	public StringProperty getAuthorProperty() {
		return author;
	}

	public ListProperty<BookVO> getResultProperty() {
		return result;
	}

	public final String getTitle() {
		return title.get();
	}

	public final String getAuthor() {
		return author.get();
	}

	public final List<BookVO> getTableContent() {
		return result.get();
	}

	public final void setTitle(String value) {
		title.set(value);
	}

	public final void setAuthor(String value) {
		author.set(value);
	}

	public final void setTableContent(List<BookVO> value) {
		result.setAll(value);
	}
}
