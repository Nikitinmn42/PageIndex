package org.test.assignment.pageindex;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.io.File;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Stream;


@Data
@NoArgsConstructor
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


    public PageIndex(String pageUrl, File pageFile, Map<String, Long> dictionary, LocalDateTime lastUpdate) {
        this.pageUrl = pageUrl;
        this.pageFile = pageFile;
        this.dictionary = dictionary;
        this.lastUpdate = lastUpdate;
    }

    public Stream<Map.Entry<String, Long>> getDictionaryEntries() {
        return dictionary.entrySet().stream();
    }
}
