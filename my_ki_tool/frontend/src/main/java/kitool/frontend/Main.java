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
            // Ollama ist nicht installiert – Installationsdialog zeigen
            Alert dialog = new Alert(Alert.AlertType.INFORMATION);
            dialog.setTitle("Ersteinrichtung");
            dialog.setHeaderText("Ollama wird installiert...");
            dialog.setContentText("Bitte warten, dies dauert nur einen Moment.");
            dialog.show();

            new Thread(() -> {
                try {
                    setupService.installOllama();
                    setupService.startOllama();
                    Platform.runLater(() -> {
                        dialog.close();
                        ladeHauptfenster(stage);
                    });
                } catch (Exception e) {
                    Platform.runLater(() -> {
                        dialog.close();
                        zeigeFehlerdialog(stage, e.getMessage());
                    });
                }
            }).start();

        } else {
            // Ollama ist installiert – nur starten
            try {
                setupService.startOllama();
            } catch (Exception e) {
                zeigeFehlerdialog(stage, e.getMessage());
                return;
            }
            ladeHauptfenster(stage);
        }
    }

    private void ladeHauptfenster(Stage stage) {
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
        error.setTitle("Fehler");
        error.setHeaderText("Ollama konnte nicht gestartet werden");
        error.setContentText(message);
        error.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}