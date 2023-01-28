package org.example.service;

import org.example.entity.Banner;
import org.example.entity.Country;

import java.util.List;

public interface AuctionService {
    /**
     * @param allBanners list of all current banners
     * @param adsCount   limit ads count
     * @param country    country filter
     * @return filtered results by conditions
     */
    List<Banner> getBannersForShowing(List<Banner> allBanners, int adsCount, Country country);
}
