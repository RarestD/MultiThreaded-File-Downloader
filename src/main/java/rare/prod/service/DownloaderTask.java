package rare.prod.service;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class DownloaderTask implements Runnable {

    private final String fileURL;
    private final String outputFile;
    private final long startByte;
    private final long endByte;
    private final AtomicLong downloadedBytes;
    private final AtomicLong localDownloadedBytes;

    public DownloaderTask(String fileURL, String outputFile, long startByte, long endByte, AtomicLong downloadedBytes, AtomicLong perThreadProgress) {
        this.fileURL = fileURL;
        this.outputFile = outputFile;
        this.startByte = startByte;
        this.endByte = endByte;
        this.downloadedBytes = downloadedBytes;
        this.localDownloadedBytes = perThreadProgress;
    }


    @Override
    public void run() {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URI(fileURL).toURL().openConnection();
            connection.addRequestProperty("Range", "bytes=" + startByte + "-" + endByte);
            connection.connect();

            try (InputStream inputStream = connection.getInputStream();
                 RandomAccessFile raf = new RandomAccessFile(outputFile, "rw")){
                    raf.seek(startByte);
                    byte[] buffer = new byte[8192];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        raf.write(buffer, 0, bytesRead);

                        downloadedBytes.addAndGet(bytesRead);
                        localDownloadedBytes.addAndGet(bytesRead);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

}
