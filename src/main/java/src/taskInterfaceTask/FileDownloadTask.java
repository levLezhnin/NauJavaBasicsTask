package src.taskInterfaceTask;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

interface Task
{
    /**
     * Запускает задачу
     */
    void start();

    /**
     * Останавливает задачу
     */
    void stop();
}

public class FileDownloadTask implements Task {

    private final URL fileUrl;
    private final Path destinationPath;
    private final AtomicBoolean running = new AtomicBoolean(false);
    private Future<?> downloadFuture;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public FileDownloadTask(URL fileUrl, Path destinationPath) {
        this.fileUrl = fileUrl;
        this.destinationPath = destinationPath;
    }

    @Override
    public void start() {
        if (running.get()) {
            System.out.println("Скачивание уже выполняется");
            return;
        }

        running.set(true);

        downloadFuture = executorService.submit(() -> {
            HttpURLConnection connection = null;
            BufferedInputStream inputStream = null;
            FileOutputStream outputStream = null;

            try {
                try {
                    connection = (HttpURLConnection) fileUrl.openConnection();
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);
                    connection.connect();

                    if (!running.get()) {
                        return;
                    }

                    inputStream = new BufferedInputStream(connection.getInputStream());
                    outputStream = new FileOutputStream(destinationPath.toFile());

                    byte[] buffer = new byte[1024];
                    int bytesRead;

                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        if (!running.get() || Thread.currentThread().isInterrupted()) {
                            break;
                        }

                        outputStream.write(buffer, 0, bytesRead);
                    }

                } finally {
                    try {
                        if (inputStream != null) inputStream.close();
                        if (outputStream != null) outputStream.close();
                        if (connection != null) connection.disconnect();
                    } catch (IOException e) {
                        System.err.println("Ошибка при закрытии ресурсов: " + e.getMessage());
                    }

                    if (!running.get()) {
                        Files.deleteIfExists(destinationPath);
                    } else {
                        running.set(false);
                        System.out.println("Скачивание завершено успешно!");
                    }
                }
            } catch (IOException e) {
                if (running.get()) {
                    System.err.println("Ошибка при скачивании: " + e.getMessage());
                }
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
}
