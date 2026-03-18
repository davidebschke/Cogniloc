package kitool.backend.service;

import io.github.ollama4j.Ollama;
import io.github.ollama4j.exceptions.OllamaException;
import io.github.ollama4j.models.chat.OllamaChatMessageRole;
import io.github.ollama4j.models.chat.OllamaChatRequest;
import io.github.ollama4j.models.chat.OllamaChatResult;
import io.github.ollama4j.models.response.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

public class OllamaService {

    private static final Logger log = LoggerFactory.getLogger(OllamaService.class);
    private final Ollama ollamaAPI;
    private String currentModel = "llama3";

    public OllamaService() {
        this.ollamaAPI = new Ollama("http://localhost:11434");
        this.ollamaAPI.setRequestTimeoutSeconds(300);
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

        } catch (OllamaException e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public String chat(String userMessage) throws Exception {
        OllamaChatRequest request = OllamaChatRequest.builder()
                .withModel(currentModel)
                .withMessage(OllamaChatMessageRole.USER, userMessage)
                .build();

        OllamaChatResult result = ollamaAPI.chat(request,null);
        return result.getResponseModel().getMessage().getResponse();
    }

    public String getCurrentModel() {
        return currentModel;
    }

    public void setCurrentModel(String model) {
        this.currentModel = model;
    }
}
