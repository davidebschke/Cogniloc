package kitool.backend.service;

import java.io.*;
import java.nio.file.*;

public class OllamaSetupService {

    private static final String OLLAMA_EXE = "C:\\Users\\" +
            System.getProperty("user.name") +
            "\\AppData\\Local\\Programs\\Ollama\\ollama.exe";

    public boolean isOllamaInstalled() {
        return new File(OLLAMA_EXE).exists();
    }

    public void installOllama() throws Exception {
        Path tempInstaller = Files.createTempFile("OllamaSetup", ".exe");

        try (InputStream is = getClass()
                .getResourceAsStream("/ollama/OllamaSetup.exe")) {

            if (is == null) {
                throw new FileNotFoundException("OllamaSetup.exe nicht in Resources gefunden!");
            }

            Files.copy(is, tempInstaller, StandardCopyOption.REPLACE_EXISTING);
        }

        Process process = new ProcessBuilder(tempInstaller.toString())
                .redirectErrorStream(true)
                .start();

        int exitCode = process.waitFor();

        Files.deleteIfExists(tempInstaller);

        if (exitCode != 0) {
            throw new RuntimeException("Ollama Installation fehlgeschlagen mit Code: " + exitCode);
        }
    }

    public void startOllama() throws Exception {
        // Prüfen ob Ollama bereits läuft
        try {
            new java.net.Socket("localhost", 11434).close();
            return;
        } catch (Exception ignored) {}

        new ProcessBuilder("ollama", "serve")
                .redirectErrorStream(true)
                .start();

        for (int i = 0; i < 10; i++) {
            Thread.sleep(1000);
            try {
                new java.net.Socket("localhost", 11434).close();
                return;
            } catch (Exception ignored) {}
        }

        throw new RuntimeException("Ollama konnte nicht gestartet werden.");
    }
}
