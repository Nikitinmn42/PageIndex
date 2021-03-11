package org.test.assignment.pageindex;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;


@Service
public class PageIndexService {

    private PageIndexRepository pageIndexRepository;

    @Autowired
    public void setPageIndexRepository(PageIndexRepository pageIndexRepository) {
        this.pageIndexRepository = pageIndexRepository;
    }

    @Transactional
    public void addPageIndex(PageIndex pageIndex) {
        PageIndex pageIndexFromDB = pageIndexRepository.findByPageUrl(pageIndex.getPageUrl());
        if (pageIndexFromDB != null) {
            pageIndexFromDB.setDictionary(pageIndex.getDictionary());
            pageIndexFromDB.setLastUpdate(LocalDateTime.now());
            pageIndexRepository.saveAndFlush(pageIndexFromDB);
        } else {
            pageIndexRepository.saveAndFlush(pageIndex);
        }
    }

    public PageIndex findIndex(String url) {
        return pageIndexRepository.findByPageUrl(url);
    }

    public void storeIndexIntoDB(PageIndex pageIndex) {
        addPageIndex(pageIndex);
    }

    public void outputIndexToConsole(PageIndex pageIndex) {
        pageIndex.getDictionaryEntries()
                .forEach(entry -> System.out.println(entry.getKey() + " - " + entry.getValue()));
    }

}
