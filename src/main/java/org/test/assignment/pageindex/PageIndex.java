package org.test.assignment.pageindex;

import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.io.File;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Stream;


@Entity
@Component
public class PageIndex {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column
    private String pageUrl;
    @Transient
    private File pageFile;
    @Column
    @ElementCollection(fetch = FetchType.EAGER)
    private Map<String, Long> dictionary;
    @Column
    private LocalDateTime lastUpdate;


    public PageIndex() {

    }

    public PageIndex(String pageUrl, File pageFile, Map<String, Long> dictionary, LocalDateTime lastUpdate) {
        this.pageUrl = pageUrl;
        this.pageFile = pageFile;
        this.dictionary = dictionary;
        this.lastUpdate = lastUpdate;
    }

    public Stream<Map.Entry<String, Long>> getDictionaryEntries() {
        return dictionary.entrySet().stream();
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public Map<String, Long> getDictionary() {
        return dictionary;
    }

    public void setDictionary(Map<String, Long> dictionary) {
        this.dictionary = dictionary;
    }
}
