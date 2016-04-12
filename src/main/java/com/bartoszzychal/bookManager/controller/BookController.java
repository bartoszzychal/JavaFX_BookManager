package com.bartoszzychal.bookManager.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;

import com.bartoszzychal.bookManager.dataProvider.DataProvider;
import com.bartoszzychal.bookManager.dataProvider.data.BookVO;
import com.bartoszzychal.bookManager.model.BookModel;
import com.bartoszzychal.bookManager.model.BookAdd;
import com.bartoszzychal.bookManager.model.BookFind;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class BookController {

	private static final Logger LOG = Logger.getLogger(BookController.class);

	@FXML
	private TabPane tabs;

	@FXML
	private Tab findBooksTab;
	@FXML
	private TableView<BookVO> findTable;
	@FXML
	private TextField findTitleField;
	@FXML
	private TextField findAuthorField;
	@FXML
	private Button findButton;

	@FXML
	private TableColumn<BookVO, String> findId;
	@FXML
	private TableColumn<BookVO, String> findTitle;
	@FXML
	private TableColumn<BookVO, String> findAuthor;

	@FXML
	private Tab addNewBookTab;
	@FXML
	private TableView<BookVO> addTable;
	@FXML
	private TextField addTitleField;
	@FXML
	private TextField addAuthorField;
	@FXML
	private Button addButton;

	@FXML
	private TableColumn<BookVO, String> addId;
	@FXML
	private TableColumn<BookVO, String> addTitle;
	@FXML
	private TableColumn<BookVO, String> addAuthor;

	private final DataProvider dataProvider = DataProvider.INSTANCE;

	private final BookModel bookAddModel = new BookAdd();
	private final BookModel bookFindModel = new BookFind();

	@FXML
	private void initialize() {
		initializeAddTab();
		setAddProperty();

		initializeFindTab();
		setFindProperty();
		
		addButton.disableProperty().bind(addTitleField.textProperty().isEmpty());
		addButton.disableProperty().bind(addAuthorField.textProperty().isEmpty());
	}



	private void initializeAddTab() {
		addId.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().getId()));
		addTitle.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getTitle()));
		addAuthor.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getAuthor()));
	}
	private void setAddProperty() {
		addTitleField.textProperty().bindBidirectional(bookAddModel.getTitleProperty());
		addAuthorField.textProperty().bindBidirectional(bookAddModel.getAuthorProperty());
		addTable.itemsProperty().bind(bookAddModel.getResultProperty());
	}

	private void initializeFindTab() {
		findId.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().getId()));
		findTitle.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getTitle()));
		findAuthor.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getAuthor()));
		
	}
	private void setFindProperty() {
		findTitleField.textProperty().bindBidirectional(bookFindModel.getTitleProperty());
		findAuthorField.textProperty().bindBidirectional(bookFindModel.getAuthorProperty());
		findTable.itemsProperty().bind(bookFindModel.getResultProperty());
		
		findId.setComparator(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				Long id1 = Long.parseLong(o1);
				Long id2 = Long.parseLong(o2);
				return id1.compareTo(id2);
			}
		});
	}

	@FXML
	public void findButton(ActionEvent event) {

		new Thread(new Task<Collection<BookVO>>() {

			@Override
			protected Collection<BookVO> call() throws Exception {
				LOG.debug("FindButton: call() called");
				return dataProvider.findBooks(bookFindModel.getTitle(), bookFindModel.getAuthor());
			}

			@Override
			protected void succeeded() {
				LOG.debug("FindButton: succeeded() called");
				bookFindModel.setTableContent(new ArrayList<>(getValue()));
				findTitleField.clear();
				findAuthorField.clear();
			}
		}).start();
	}

	@FXML
	public void addButton(ActionEvent event) {

		new Thread(new Task<Collection<BookVO>>() {

			@Override
			protected Collection<BookVO> call() throws Exception {
				LOG.debug("AddButton: call() called");
				BookVO addedBook = dataProvider.addNewBooks(new BookVO(bookAddModel.getTitle(), bookAddModel.getAuthor()));
				ArrayList<BookVO> books = new ArrayList<>();
				books.add(addedBook);
				return books;
			}

			@Override
			protected void succeeded() {
				LOG.debug("AddButton: succeeded() called");
				List<BookVO> books = new ArrayList<>(getValue());
				bookAddModel.getTableContent().add(0, books.get(0));;
				addTitleField.clear();
				addAuthorField.clear();
			}
		}).start();
	}

}
