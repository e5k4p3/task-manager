package service;

import service.interfaces.BasicLogger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TaskManagerLogger implements BasicLogger {
    private final File file;

    public TaskManagerLogger(File file) {
        this.file = file;
    }

    @Override
    public void addMessageToLog(String message) {
        try (Writer fileWriter = new FileWriter(file, true)) {
            fileWriter.write(LocalDateTime.now() + " " + message + "\n");
        } catch (IOException e) {
            System.out.println("Произошла ошибка во время добавления сообщения в log файл");
        }
    }

    @Override
    public void clearLogFile() {
        try (Writer fileWriter = new FileWriter(file)) {
            fileWriter.write("");
        } catch (IOException e) {
            System.out.println("Произошла ошибка во время очистки log файла");
        }
    }

    @Override
    public List<String> getLoggedMessages() {
        try {
            return Files.readAllLines(Paths.get(String.valueOf(file)));
        } catch (IOException e) {
            System.out.println("Произошла ошибка во время считывания log файла");
        }
        return new ArrayList<>();
    }
}
