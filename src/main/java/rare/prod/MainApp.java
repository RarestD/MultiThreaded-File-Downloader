package rare.prod;

import rare.prod.service.ConfigManager;
import rare.prod.service.FileDownloader;
import rare.prod.util.Input;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Hello world!
 */
public class MainApp {
    public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException {
        while (true) {
            System.out.println("Thank you for using Rare MFD");
            System.out.println("MENU");
            System.out.println("1. Download");
            System.out.println("2. Set Download Path");
            System.out.println("x. Exit");
            String pleaseEnterYourChoice = Input.getInput("Please enter your choice");
            switch (pleaseEnterYourChoice.toLowerCase()) {
                case "1": viewDownload(); break;
                case "2": setPathView(); break;
                case "x": System.exit(0);

            }
        }
    }

    public static void viewDownload() throws IOException, URISyntaxException, InterruptedException {

        getPathViewFirst();

        String url = Input.getInput("Enter file URL");
        var fileName = Input.getInput("Enter file name to save as without .filetype (leave it blank to use default )");
        if (fileName.isBlank()) {
            fileName = null;
        }

        FileDownloader downloader = new FileDownloader(url, fileName, 8);
        downloader.download();
    }

    public static void getPathViewFirst(){
        String targetFolder = ConfigManager.getTargetFolder();

        if (targetFolder == null) {
            // First time: ask user
            targetFolder = Input.getInput("Enter target folder path").trim();
            ConfigManager.setTargetFolder(targetFolder);
            System.out.println("Saved target folder: " + targetFolder);
        } else {
            System.out.println("Using saved target folder: " + targetFolder);
        }
    }

    public static void setPathView(){
        String targetFolder = ConfigManager.getTargetFolder();

        targetFolder = Input.getInput("Enter target folder path").trim();
        ConfigManager.setTargetFolder(targetFolder);
        System.out.println("Saved target folder: " + targetFolder);
    }
}
