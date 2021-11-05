package com.msr.service;

import com.msr.data.SiteRepository;
import com.msr.model.Site;
import com.msr.model.SiteUse;
import com.msr.model.UseType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SiteService {
	@Autowired
	private SiteRepository siteRepository;

	public Optional<Site> getSiteById(int id) {
		Optional<Site> site = siteRepository.findById(id);

		return site.map(this::aggregateSites);
	}

	public List<Site> getAllSites() {
		List<Site> sites = siteRepository.findAll();

		return sites.stream().map(this::aggregateSites).collect(Collectors.toList());
	}

	public List<Site> getSitesByState(String state) {
		List<Site> sites = siteRepository.findByState(state);
		return sites.stream().map(this::aggregateSites).collect(Collectors.toList());
	}

	private Site aggregateSites(Site ste) {
		Map<UseType, Integer> useTypeSizeMap = new HashMap<>();

		int totalSize = 0;
		int maxSize = 0;

		// calculate total size and find primary type
		for(SiteUse siteUse : ste.getSiteUses()) {
			totalSize += siteUse.getSizeSqft();

			UseType useType = siteUse.getUseType();
			int useTypeSize = useTypeSizeMap.getOrDefault(useType, 0) + siteUse.getSizeSqft();

			useTypeSizeMap.put(useType, useTypeSize);

			// set primary type
			if (useTypeSize > maxSize) {
				maxSize = useTypeSize;
				ste.setPrimaryType(useType);
			}
		}

		// set total size
		ste.setTotalSize(totalSize);

		return ste;
	}
}
