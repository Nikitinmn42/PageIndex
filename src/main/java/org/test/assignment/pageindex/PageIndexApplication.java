package org.test.assignment.pageindex;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;


@SpringBootApplication
public class PageIndexApplication {

    private static PageProcessor pageProcessor;
    private static PageIndexService pageIndexService;

    public static void main(String[] args) {
        // Logging configuration
        try {
            new File("./log/").mkdirs();
            LogManager.getLogManager()
                    .readConfiguration(PageIndexApplication.class.getResourceAsStream("/logging.properties"));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        SpringApplication.run(PageIndexApplication.class, args);

        Scanner userInputScanner = new Scanner(System.in);
        String userInput;
        while (true) {
            System.out.print("Enter URL to analyze the page command (exit, find, help): ");
            userInput = userInputScanner.nextLine();
            switch (userInput) {
                case "exit":
                    System.exit(0);
                case "find":
                    System.out.print("Enter URL to find in stored index: ");
                    userInput = userInputScanner.nextLine();
                    PageIndex index = pageIndexService.findIndex(userInput);
                    if (index == null) {
                        System.out.println("Index for page: \"" + userInput + "\" not found.");
                    } else {
                        pageIndexService.outputIndexToConsole(index);
                    }
                    break;
                case "help":
                    System.out.println("Commands: \n" +
                            "exit - to exit the program. \n" +
                            "find - to find index for page already stored in database.");
                    break;
                default:
                    pageProcessor.setPageUrl(userInput);
                    pageProcessor.processPage();
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
            }
            System.out.println("\n");
        }
    }

    @Autowired
    public void setPageIndexService(PageIndexService pageIndexService) {
        PageIndexApplication.pageIndexService = pageIndexService;
    }

    @Autowired
    public void setPageProcessor(PageProcessor pageProcessor) {
        PageIndexApplication.pageProcessor = pageProcessor;
    }
}
