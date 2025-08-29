package rare.prod.util;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Set;

public class FileType {

    private static Map<String, String> type = Map.ofEntries(
            Map.entry(".mkv", "video/x-matroska"),
            Map.entry(".mp4", "video/mp4"),
            Map.entry(".avi", "video/x-msvideo"),
            Map.entry(".mov", "video/quicktime"),
            Map.entry(".wmv", "video/x-ms-wmv"),
            Map.entry(".flv", "video/x-flv"),
            Map.entry(".webm", "video/webm"),
            Map.entry(".pdf", "application/pdf"),
            Map.entry(".zip", "application/zip"),
            Map.entry(".jpg", "image/jpeg"),
            Map.entry(".jpeg", "image/jpeg"),
            Map.entry(".png", "image/png")
            );

    public static String getFilename(HttpURLConnection connection){
        URL url = connection.getURL();

        // Method 1: Using Content-Disposition
        String contentDisposition = connection.getHeaderField("Content-Disposition");
        String serverFilename = null;
        if (contentDisposition != null && contentDisposition.contains("filename=")) {
            serverFilename = extractFilename(contentDisposition);
            System.out.println("Server filename: " + serverFilename);
        }

        // checking if the connection has Content-Disposition
        if (serverFilename != null) {
            // has Content-Disposition returning filename
            return serverFilename;
        }

        // Doesn't have a Content-Disposition try using method 2

        // Method 2: URL parsing (fallback)
        return extractFilenameFromURL(url.getPath());

    }

    public static String detectFileExtension(String filename) {
        // returning detected file Type by getFileExtension and covert into .file
        String fileExtension = getFileExtension(filename);
        if (type.containsKey(fileExtension)){
            return fileExtension;
        }
        String fileExtensionFromURL = getFileExtensionFromURL(fileExtension);
        return fileExtensionFromURL;
    }

    private static String extractFilename(String contentDisposition) {
        // getting a filename using Content-Disposition
        int index = contentDisposition.indexOf("filename=");
        if (index != -1) {
            return contentDisposition.substring(index + 9).replaceAll("\"", "");
        }
        return null;
    }

    private static String extractFilenameFromURL(String path) {
        // using fallback method or URL Parsing
        return path.substring(path.lastIndexOf('/') + 1);
    }

    private static String getFileExtension(String filename) {
        int lastDot = filename.lastIndexOf('.');
        return lastDot > 0 ? filename.substring(lastDot) : "";
    }

    private static String getFileTypeFromExtension(String extension) {
        switch (extension.toLowerCase()) {
            case ".mkv": return "video/x-matroska";
            case ".mp4": return "video/mp4";
            case ".avi": return "video/x-msvideo";
            case ".mov": return "video/quicktime";
            case ".wmv": return "video/x-ms-wmv";
            case ".flv": return "video/x-flv";
            case ".webm": return "video/webm";
            case ".pdf": return "application/pdf";
            case ".zip": return "application/zip";
            case ".jpg": return "image/jpeg";
            case ".jpeg": return "image/jpeg";
            case ".png": return "image/png";
            default: return ".dat";
        }
    }

    private static String getFileExtensionFromURL(String url) {
        switch (url.toLowerCase()) {
            case "video/x-matroska": return ".mkv";
            case "video/mp4": return ".mp4";
            case "video/x-msvideo": return ".avi";
            case "video/quicktime": return ".mov";
            case "video/x-ms-wmv": return ".wmv";
            case "video/x-flv": return ".flv";
            case "video/webm": return ".webm";
            case "application/pdf": return ".pdf";
            case "application/zip": return ".zip";
            case "image/jpg": return ".jpg";
            case "image/jpeg": return ".jpeg";
            case "image/png": return ".png";
        }
        return null;
    }

}
