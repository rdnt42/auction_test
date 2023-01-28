package org.example.service;

import org.example.dto.BannerFilter;
import org.example.dto.BannerPriority;
import org.example.dto.entity.Banner;
import org.example.dto.entity.Country;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class AuctionServiceImplTest {
    private final AuctionServiceImpl auctionService = new AuctionServiceImpl();

    @Test
    void getBannersForShowingEmpty() {
        List<Banner> bannersForShowing = auctionService.getBannersForShowing(Collections.emptyList(), 10, Country.RUSSIA);
        assertTrue(bannersForShowing.isEmpty());
    }

    @Test
    void getBannersForShowingEmptyCountries() {
        Banner banner = getBanner(1, 1);
        List<Banner> bannersForShowing = auctionService.getBannersForShowing(Collections.singletonList(banner), 10, Country.RUSSIA);

        assertEquals(1, bannersForShowing.size());
        assertEquals(1, bannersForShowing.get(0).getBannerId());
    }

    @Test
    void getBannersForShowingSelectedCountry() {
        Banner banner = getBannerWithCountry(1, 1, Country.RUSSIA);

        List<Banner> bannersForShowing = auctionService.getBannersForShowing(Collections.singletonList(banner), 10, Country.RUSSIA);

        assertEquals(1, bannersForShowing.size());
        assertEquals(1, bannersForShowing.get(0).getBannerId());
    }

    @Test
    void getBannersForShowingNotSelectedCountry() {
        Banner banner = getBannerWithCountry(1, 1, Country.ARMENIA);

        List<Banner> bannersForShowing = auctionService.getBannersForShowing(Collections.singletonList(banner), 10, Country.RUSSIA);

        assertTrue(bannersForShowing.isEmpty());
    }

    @Test
    void getBannersForShowingFilterCampaign() {
        List<Banner> duplicateBanners = getTwoCampaignDuplicateBanners(Country.RUSSIA);

        List<Banner> bannersForShowing = auctionService.getBannersForShowing(duplicateBanners, 10, Country.RUSSIA);

        assertEquals(1, bannersForShowing.size());
        assertTrue(bannersForShowing.get(0).getBannerId() == 1 || bannersForShowing.get(0).getBannerId() == 2);
    }

    @Test
    void getBannersForShowingMostExpensiveCampaign() {
        List<Banner> duplicateBanners = getTwoCampaignDuplicateBanners(Country.RUSSIA);
        duplicateBanners.get(1).setCost(100);
        duplicateBanners.get(1).setBannerId(2);

        List<Banner> bannersForShowing = auctionService.getBannersForShowing(duplicateBanners, 10, Country.RUSSIA);

        assertEquals(1, bannersForShowing.size());
        assertEquals(2, bannersForShowing.get(0).getBannerId());
    }

    @Test
    void getBannersForShowingLimit() {
        List<Banner> banners = getFiveDifferentBanners();
        List<Banner> bannersForShowing = auctionService.getBannersForShowing(banners, 2, Country.RUSSIA);

        assertEquals(2, bannersForShowing.size());
    }

    @Test
    void getBannersForShowingLimitHighPrice() {
        List<Banner> banners = getFiveDifferentBanners();
        banners.get(2).setCost(100);
        banners.get(4).setCost(10);

        List<Banner> bannersForShowing = auctionService.getBannersForShowing(banners, 2, Country.RUSSIA);

        assertEquals(2, bannersForShowing.size());
        assertEquals(100.0, bannersForShowing.get(0).getCost());
        assertEquals(10.0, bannersForShowing.get(1).getCost());

    }


    // для опциональных задач только несколько тестов
    @Test
    void getBannersForShowingFilterByMinCost() {
        BannerFilter filter = new BannerFilter();
        filter.setMinCost(50);
        List<Banner> banners = getFiveDifferentBanners();
        banners.get(2).setCost(100);
        banners.get(4).setCost(10);

        List<Banner> bannersForShowing = auctionService.getBannersForShowing(banners, 5, filter, new BannerPriority());

        assertEquals(1, bannersForShowing.size());
        assertEquals(100.0, bannersForShowing.get(0).getCost());
    }

    @Test
    void getBannersForShowingPriorityByBannerIdDown() {
        BannerPriority priority = new BannerPriority();
        priority.setBannerId(-1);
        List<Banner> banners = getFiveDifferentBanners();

        List<Banner> bannersForShowing = auctionService.getBannersForShowing(banners, 5, new BannerFilter(), priority);

        assertEquals(5, bannersForShowing.size());
        assertEquals(5, bannersForShowing.get(0).getBannerId());
    }

    @Test
    void getBannersForShowingPriorityByTwoDirectional() {
        BannerPriority priority = new BannerPriority();
        priority.setAdvCampaignId(1);
        priority.setBannerId(-1);
        List<Banner> banners = getFiveDifferentBanners();

        List<Banner> bannersForShowing = auctionService.getBannersForShowing(banners, 5, new BannerFilter(), priority);

        assertEquals(5, bannersForShowing.size());
        assertEquals(1, bannersForShowing.get(0).getAdvCampaignId());
    }

    private List<Banner> getTwoCampaignDuplicateBanners(Country country) {
        Banner banner1 = getBannerWithCountry(1, 1, country);
        Banner banner2 = getBannerWithCountry(2, 1, country);

        return Stream.of(banner1, banner2)
                .collect(Collectors.toList());
    }

    private Banner getBanner(long id, long campaignId) {
        Banner banner = new Banner();
        banner.setBannerId(id);
        banner.setAdvCampaignId(campaignId);

        return banner;
    }

    private Banner getBannerWithCountry(long id, long campaignId, Country country) {
        Banner banner = getBanner(id, campaignId);
        banner.setShowAdsCountries(Collections.singletonList(country));

        return banner;
    }

    private List<Banner> getFiveDifferentBanners() {
        List<Banner> banners = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Banner banner = getBanner(i, i);
            banners.add(banner);
        }

        return banners;
    }
}