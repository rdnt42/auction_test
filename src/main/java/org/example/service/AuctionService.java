package org.example.service;

import org.example.dto.BannerPriority;
import org.example.dto.entity.Banner;
import org.example.dto.entity.Country;
import org.example.dto.BannerFilter;

import java.util.List;

public interface AuctionService {
    /**
     * @param allBanners list of all current banners
     * @param adsCount   limit ads count
     * @param country    country filter
     * @return filtered results by conditions
     */
    List<Banner> getBannersForShowing(List<Banner> allBanners, int adsCount, Country country);

    /**
     * @param allBanners list of all current banners
     * @param adsCount   limit ads count
     * @param filter     list for filtering
     * @param priority   list for prioritize
     * @return filtered and prioritized results by filters
     */
    List<Banner> getBannersForShowing(List<Banner> allBanners, int adsCount, BannerFilter filter, BannerPriority priority);

}
