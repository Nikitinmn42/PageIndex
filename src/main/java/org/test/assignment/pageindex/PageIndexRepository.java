package org.test.assignment.pageindex;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PageIndexRepository extends JpaRepository<PageIndex, Long> {

    PageIndex findByPageUrl(String pageUrl);

}
