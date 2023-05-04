package pl.tiad.task2.frontend;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import static pl.tiad.task2.frontend.MainFormController.MAIN_FORM_RESOURCE;
import static pl.tiad.task2.frontend.MainFormController.MAIN_FORM_TITLE;

/**
 * =========================================================
 * Kamil Bednarek 236500, Radosław Bucki 236507
 * =========================================================
 */

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource(MAIN_FORM_RESOURCE));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle(MAIN_FORM_TITLE);
        scene.getStylesheets().add(String.valueOf(this.getClass().getResource("style.css")));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}