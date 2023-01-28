package org.example.service;

import org.example.entity.Banner;
import org.example.entity.Country;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class AuctionServiceImpl implements AuctionService {
    // TODO нужно учесть рандомизацию в тестах, сейчас тест просто проверяет один из результатов
    private final Random random = new Random();

    @Override
    public List<Banner> getBannersForShowing(List<Banner> allBanners, int adsCount, Country country) {
        Predicate<Banner> countryFilter = getCountryFilter(country);
        BinaryOperator<Banner> campaignMaxCostFilter = getCampaignMaxCostFilter();

        return allBanners.stream()
                .filter(countryFilter)
                // get max cost for unique campaignId
                .collect(Collectors.toMap(Banner::getAdvCampaignId, Function.identity(), campaignMaxCostFilter))
                .values().stream()
                .sorted(Comparator.comparing(Banner::getCost, Comparator.reverseOrder()))
                .limit(adsCount)
                .collect(Collectors.toList());
    }

    private Predicate<Banner> getCountryFilter(Country country) {
        return banner -> banner.getShowAdsCountries().isEmpty() || banner.getShowAdsCountries().contains(country);
    }

    private BinaryOperator<Banner> getCampaignMaxCostFilter() {
        return (a, b) -> {
            int compare = Comparator.comparing(Banner::getCost)
                    .compare(a, b);
            if (compare > 0) {
                return a;
            } else if (compare < 0) {
                return b;
            } else {
                return random.nextBoolean() ? a : b;
            }
        };
    }
}
