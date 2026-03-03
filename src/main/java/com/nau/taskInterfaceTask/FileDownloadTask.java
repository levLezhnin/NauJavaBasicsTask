package com.nau.taskInterfaceTask;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class FileDownloadTask implements Task, Closeable {

    private static final int TIMEOUT_MILLIS = 5000;
    private static final int BUFFER_SIZE = 1024;

    private final URL fileUrl;
    private final Path destinationPath;

    private final AtomicBoolean running = new AtomicBoolean(false);
    private final AtomicBoolean successfullyCompleted = new AtomicBoolean(false);

    private CompletableFuture<?> downloadFuture;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public FileDownloadTask(URL fileUrl, Path destinationPath) {
        this.fileUrl = fileUrl;
        this.destinationPath = destinationPath;
    }

    @Override
    public void start() {
        if (!running.compareAndSet(false, true)) {
            System.out.println("Скачивание уже выполняется");
            return;
        }

        successfullyCompleted.set(false);

        downloadFuture = CompletableFuture.runAsync(() -> {
            HttpURLConnection connection = null;

            try {
                try {
                    connection = (HttpURLConnection) fileUrl.openConnection();
                    connection.setConnectTimeout(TIMEOUT_MILLIS);
                    connection.setReadTimeout(TIMEOUT_MILLIS);
                    connection.connect();

                    if (!running.get()) {
                        return;
                    }

                    try (BufferedInputStream inputStream = new BufferedInputStream(connection.getInputStream());
                         OutputStream outputStream = Files.newOutputStream(destinationPath))
                    {

                        byte[] buffer = new byte[BUFFER_SIZE];
                        int bytesRead;

                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            if (!running.get() || Thread.currentThread().isInterrupted()) {
                                break;
                            }

                            outputStream.write(buffer, 0, bytesRead);
                        }

                        outputStream.flush();
                        successfullyCompleted.set(true);
                    }

                } finally {
                    if (connection != null) connection.disconnect();
                }
            } catch (IOException e) {
                if (running.get()) {
                    System.err.println("Ошибка при скачивании: " + e.getMessage());
                }
                successfullyCompleted.set(false);
            }
        }, executorService).whenComplete((result, throwable) -> {
                running.set(false);

                boolean completed = successfullyCompleted.get();

                if (!completed) {
                    System.out.println("Файл был загружен не полностью. Удаляю частично скачанный файл: " + destinationPath);
                    try {
                        Files.deleteIfExists(destinationPath);
                    } catch (IOException e) {
                        throw new RuntimeException("Не получилось удалить частично скачанный файл: " + destinationPath);
                    }
                } else {
                    System.out.println("Скачивание завершено успешно!");
                }
            });
    }

    @Override
    public void stop() {
        if (!running.get()) {
            System.out.println("Скачивание не выполняется");
            return;
        }

        running.set(false);

        if (downloadFuture != null) {
            downloadFuture.cancel(true);
        }

        System.out.println("Команда остановки отправлена");
    }

    public boolean awaitTermination() {
        try {
            downloadFuture.get();
            return true;
        } catch (CancellationException e) {
            System.out.println("Скачивание было отменено");
            return false;
        }
        catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Не получилось скачать файл: " + e);
        }
    }

    @Override
    public void close() {
        executorService.shutdownNow();
    }
}
