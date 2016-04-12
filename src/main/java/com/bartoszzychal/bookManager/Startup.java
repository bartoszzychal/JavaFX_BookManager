package com.bartoszzychal.bookManager;

import java.util.Locale;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Startup extends Application{
			
	public static void main(String[] args) {
		Application.launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Book manager");
		
		String langCode = getParameters().getNamed().get("lang");
		if (langCode != null && !langCode.isEmpty()) {
			Locale.setDefault(Locale.forLanguageTag(langCode));
		}
		
		Parent root = FXMLLoader.load(getClass().getResource("/com/bartoszzychal/bookManager/view/bookManager.fxml"), //
				ResourceBundle.getBundle("com/bartoszzychal/bookManager/bundle/base"));
		
		Scene scene = new Scene(root);
		
		scene.getStylesheets().add(getClass().getResource("/com/bartoszzychal/bookManager/css/standard.css").toExternalForm());
		
		primaryStage.setScene(scene);
		
		primaryStage.show();
	}

}
