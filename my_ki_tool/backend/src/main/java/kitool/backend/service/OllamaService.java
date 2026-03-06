package kitool.backend.service;

import io.github.ollama4j.OllamaAPI;
import io.github.ollama4j.exceptions.OllamaBaseException;
import io.github.ollama4j.models.Model;
import io.github.ollama4j.models.OllamaResult;
import io.github.ollama4j.utils.OptionsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Locale;

public class OllamaService {

    private static final Logger log = LoggerFactory.getLogger(OllamaService.class);
    private final OllamaAPI ollamaAPI;
    private String currentModel = "llama3";

    public OllamaService() {
        this.ollamaAPI = new OllamaAPI("http://localhost:11434");
        this.ollamaAPI.setRequestTimeoutSeconds(300);
    }

    public static void changeLanguage(String choosenLanguage) {
        switch (choosenLanguage) {
                case "English":
                Locale.setDefault(new Locale("en", "US"));
                log.info("Language: English-US");
                break;
                case "Francais":
                Locale.setDefault(new Locale("fr", "FR"));
                log.info("Language: Francais");
                break;
                case "Deutsch":
                Locale.setDefault(new Locale("de", "DE"));
                log.info("Language: Deutsch");
                break;

        }
    }

    public boolean isOllamaRunning() {
        try {
            return ollamaAPI.ping();
        } catch (Exception _) {
            return false;
        }
    }

    public List<String> getAvailableModels() {
        try {
           return ollamaAPI.listModels()
                    .stream()
                    .map(Model::getName)
                    .toList();

        } catch (InterruptedException | OllamaBaseException | IOException | URISyntaxException e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public String chat(String userMessage) throws Exception {
        OllamaResult result = ollamaAPI.generate(
                currentModel,
                userMessage,
                false,
                new OptionsBuilder().build()
        );
        return result.getResponse();
    }

    public String getCurrentModel() {
        return currentModel;
    }

    public void setCurrentModel(String model) {
        this.currentModel = model;
    }
}
