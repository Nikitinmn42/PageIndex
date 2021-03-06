package org.test.assignment.pageindex;

import lombok.Data;
import lombok.Setter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.IDN;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;


@Data
@Component
public class PageProcessor {

    private final String pageFilePath = "./page.html";

    private String pageUrl;
    @Setter(onMethod_ = @Autowired)
    private PageIndexService pageIndexService;

    public void processPage() {
        if (isUrlCorrect()) {
            File pageFile = downloadFromUrl();
            if (pageFile == null) {
                System.out.println("Failed to download page.");
                return;
            }
            PageIndex pageIndex = countWords(pageFile);
            if (pageIndex == null) {
                System.out.println("Failed to process page.");
                return;
            }
            pageIndexService.outputIndexToConsole(pageIndex);
            pageIndexService.storeIndexIntoDB(pageIndex);
        } else {
            System.out.println("Wrong URL format: \"" + pageUrl +
                    "\"\nPlease enter URL in format \"https://www.example.com/\".");
        }
    }

    private boolean isUrlCorrect() {
        return pageUrl.matches("(?U)https?://[-\\w+&@#/%?=~|!:,.;()]*[-\\w+&@#/%=~|()]");
    }

    private File downloadFromUrl() {
        Path pagePath = Path.of(pageFilePath);
        File file = null;
        try (Scanner pageScanner = new Scanner(new URL(encodeUrl()).openStream()).useDelimiter("\n")) {
            file = writeFile(pagePath, pageScanner.tokens());
        } catch (IOException e) {
            Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
        }
        return file;
    }

    private String encodeUrl() {
        if (StandardCharsets.US_ASCII.newEncoder().canEncode(pageUrl)) {
            return pageUrl;
        }
        String[] urlSplit = pageUrl.split("(://)|/");
        return urlSplit[0] + "://" + IDN.toASCII(urlSplit[1]) + "/" +
                String.join("/", Arrays.copyOfRange(urlSplit, 2, urlSplit.length));
    }

    private File writeFile(Path path, Stream<String> stringStream) throws IOException {
        Files.deleteIfExists(path);
        stringStream.forEach(line -> {
            try {
                Files.writeString(path, line + "\n", CREATE, APPEND);
            } catch (IOException e) {
                Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
            }
        });
        return path.toFile();
    }

    private PageIndex countWords(File pageFile) {
        Map<String, Long> dictionary = null;
        try {
            Element body = Jsoup.parse(pageFile, "UTF-8").body();
            dictionary = body.getAllElements().stream()
                    .map(Element::ownText)
                    .flatMap(line -> Arrays.stream(line.toLowerCase().split("([\\s\\p{Punct}&&[^-]])+")))
                    .filter(word -> word.matches("(?U)\\w+(-\\w+)*"))
                    .collect(Collectors.groupingBy(word -> word, Collectors.counting()));
        } catch (IOException e) {
            Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
        }
        return dictionary == null ? null : new PageIndex(pageUrl, pageFile, dictionary, LocalDateTime.now());
    }
}
