package com.msr.data;

import com.msr.model.Site;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * A sample JPA repository for querying and storing sites
 */
public interface SiteRepository extends JpaRepository<Site, Integer> {
	List<Site> findByState(String state);
}