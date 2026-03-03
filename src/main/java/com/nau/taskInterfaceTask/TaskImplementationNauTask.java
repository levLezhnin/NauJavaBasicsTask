package com.nau.taskInterfaceTask;

import com.nau.NauTask;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.time.Duration;

public class TaskImplementationNauTask implements NauTask {

    @Override
    public void solve() {
        String fileUrlStringPath = "https://books-online.in/upload/txt/tolstoy_voyna-i-mir__xoraa_436421.txt";

        File tempFile;
        URL fileUrl;
        Path filePath;

        try {
            tempFile = File.createTempFile("Война-и-мир", ".txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            fileUrl = URI.create(fileUrlStringPath).toURL();
            filePath = tempFile.toPath();
        } catch (MalformedURLException e) {
            throw new RuntimeException("Некорректный URL файла: " + fileUrlStringPath);
        }

        try (FileDownloadTask fileDownloadTask = new FileDownloadTask(fileUrl, filePath)) {

            performTaskWithInterruption(fileDownloadTask);
            System.out.printf("Заполнен ли файл %s : %s\n", tempFile.toPath(), tempFile.length() > 0);

            performTaskWithoutInterruption(fileDownloadTask);
            System.out.printf("Заполнен ли файл %s : %s\n", tempFile.toPath(), tempFile.length() > 0);

        }
        tempFile.deleteOnExit();
    }

    private void performTaskWithInterruption(FileDownloadTask fileDownloadTask) {
        Thread interruptor = new Thread(() -> {
            try {
                Thread.sleep(Duration.ofMillis(100));
                fileDownloadTask.stop();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        System.out.println("Выполнение с прерыванием");

        interruptor.start();
        startTaskAndAwaitResult(fileDownloadTask);
    }

    private void performTaskWithoutInterruption(FileDownloadTask fileDownloadTask) {
        System.out.println("Выполнение без прерывания");
        startTaskAndAwaitResult(fileDownloadTask);
    }

    private void startTaskAndAwaitResult(FileDownloadTask fileDownloadTask) {
        fileDownloadTask.start();
        fileDownloadTask.awaitTermination();
    }
}
