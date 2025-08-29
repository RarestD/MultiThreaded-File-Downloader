package rare.prod.service;

import rare.prod.util.FileType;
import rare.prod.util.ProgressBar;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

public class FileDownloader {

    private final String fileURL;
    private String outputFile;
    private final int numThreads;


    public FileDownloader(String fileURL, String outputFile, int numThreads) {
        this.fileURL = fileURL;
        this.outputFile = outputFile;
        this.numThreads = numThreads;
    }

    public void download() throws IOException, InterruptedException, URISyntaxException {
        // ambil ukuran file
        HttpURLConnection connection = (HttpURLConnection)  new URI(fileURL).toURL().openConnection();
        int contentLength = connection.getContentLength();
        long chunkSize = contentLength / numThreads;
        List<AtomicLong> perThreadProgress = new ArrayList<>(numThreads);
        AtomicLong downloadedBytes = new AtomicLong(0);

        if (outputFile == null) {
            outputFile = ConfigManager.getTargetFolder() + "/" + FileType.getFilename(connection);
        }else {
            outputFile = ConfigManager.getTargetFolder() + "/"+ outputFile + FileType.detectFileExtension(fileURL);
        }

        try (RandomAccessFile raf = new RandomAccessFile(outputFile, "rw")){
            raf.setLength(contentLength);
        }

        for (int i = 0; i < numThreads; i++) {
            perThreadProgress.add(new AtomicLong());
        }

        try (ExecutorService executor = Executors.newFixedThreadPool(numThreads)) {
            for (int i = 0; i < numThreads; i++) {
                long startByte = i * chunkSize;
                long endByte = (i == numThreads - 1) ? contentLength : (startByte + chunkSize - 1);

                executor.submit(new DownloaderTask(fileURL, outputFile, startByte, endByte, downloadedBytes, perThreadProgress.get(i)));
            }

            StringBuilder builder;

            executor.shutdownNow();
            while (!executor.isTerminated()) {
                Thread.sleep(200);

                builder = new StringBuilder("File Size: " + contentLength + "\n").append("Main Progress\n");


                // Total bar
                String data = ProgressBar.renderBar(downloadedBytes.get(), contentLength, 50) + "\n";
                builder.append(data);

                // Thread bars
                for (int i = 0; i < numThreads; i++) {
                    long downloaded = perThreadProgress.get(i).get();
                    long threadSize = (i == numThreads - 1)
                            ? (contentLength - (chunkSize * i))
                            : chunkSize;

                    String thread = "Thread " + (i + 1) + ": " + ProgressBar.renderBar(downloaded, threadSize, 30);
                    builder.append(thread).append("\n");
                }

                System.out.print("\033[H\033[2J");
                System.out.flush();
                System.out.print(builder.toString());

            }
            System.out.println("\nDownload is completed");
        }
    }

}
