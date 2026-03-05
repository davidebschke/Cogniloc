package kitool.frontend;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import kitool.backend.service.OllamaSetupService;

import java.util.Objects;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        OllamaSetupService setupService = new OllamaSetupService();

        if (!setupService.isOllamaInstalled()) {
            Alert dialog = new Alert(Alert.AlertType.INFORMATION);
            dialog.setTitle("initial setup");
            dialog.setHeaderText("Ollama is being installed...");
            dialog.setContentText("Please wait, this will only take a moment.");
            dialog.show();

            new Thread(() -> {
                try {
                    setupService.installOllama();
                    setupService.startOllama();
                    Platform.runLater(() -> {
                        dialog.close();
                        loadMainWindow(stage);
                    });
                } catch (Exception e) {
                    Platform.runLater(() -> {
                        dialog.close();
                        zeigeFehlerdialog(stage, e.getMessage());
                    });
                }
            }).start();

        } else {

            try {
                setupService.startOllama();
            } catch (Exception e) {
                zeigeFehlerdialog(stage, e.getMessage());
                return;
            }
            loadMainWindow(stage);
        }
    }

    private void loadMainWindow(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/MainView.fxml"));

            Scene scene = new Scene(loader.load(), 1100, 720);

            scene.getStylesheets().add(
                    Objects.requireNonNull(
                            getClass().getResource("/css/darkMode.css")
                    ).toExternalForm());

            stage.setTitle("MyKiTool");
            stage.setScene(scene);
            stage.setMinWidth(800);
            stage.setMinHeight(600);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void zeigeFehlerdialog(Stage stage, String message) {
        Alert error = new Alert(Alert.AlertType.ERROR);
        error.setTitle("Failure");
        error.setHeaderText("Ollama can not started");
        error.setContentText(message);
        error.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}