package org.example.service;

import org.example.dto.BannerPriority;
import org.example.dto.entity.Banner;
import org.example.dto.entity.Country;
import org.example.dto.BannerFilter;

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
                // по дефолту нет реализации distinctNyKey, поэтому это фильтр максимального значения текущей кампании
                .collect(Collectors.toMap(Banner::getAdvCampaignId, Function.identity(), campaignMaxCostFilter))
                .values().stream()
                .sorted(Comparator.comparing(Banner::getCost, Comparator.reverseOrder()))
                .limit(adsCount)
                .collect(Collectors.toList());
    }

    @Override
    public List<Banner> getBannersForShowing(List<Banner> allBanners, int adsCount, BannerFilter filter, BannerPriority priority) {
        List<Predicate<Banner>> filterList = getFiltersList(filter);
        Comparator<Banner> priorityComparator = getPriorityComparator(priority);
        BinaryOperator<Banner> campaignMaxCostFilter = getCampaignMaxCostFilter();

        return allBanners.stream()
                .filter(filterList.stream().reduce(i -> true, Predicate::and))
                .collect(Collectors.toMap(Banner::getAdvCampaignId, Function.identity(), campaignMaxCostFilter))
                .values().stream()
                .sorted(priorityComparator)
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

    // фильтры можно вынести в отдельные сервисы и проверять их отдельно. тогда тесты будут разнесены
    private List<Predicate<Banner>> getFiltersList(BannerFilter filter) {
        List<Predicate<Banner>> filterList = new ArrayList<>();
        if (filter.getCountry() != null) {
            filterList.add(getCountryFilter(filter.getCountry()));
        }

        if (filter.getAdvCampaignId() != null) {
            Predicate<Banner> predicate = b -> b.getAdvCampaignId() == filter.getAdvCampaignId();
            filterList.add(predicate);
        }

        if (filter.getBannerId() != null) {
            Predicate<Banner> predicate = b -> b.getBannerId() == filter.getBannerId();
            filterList.add(predicate);
        }

        if (filter.getMinCost() != null) {
            Predicate<Banner> predicate = b -> b.getCost() >= filter.getMinCost();
            filterList.add(predicate);
        }

        return filterList;
    }

    private Comparator<Banner> getPriorityComparator(BannerPriority priority) {
        // TODO у цепочки компараторов приоритет сортировки обратен преоритету фильтров
        // т.е. более важные фильтры должны быть в конце цепочки. Сейчас это не учитывается
        Comparator<Banner> comparatorChain = Comparator.comparing(Banner::getCost, Comparator.reverseOrder());
        if (priority.getBannerId() != null) {
            Comparator<Banner> comparator = createFieldComparator(priority.getBannerId(), Banner::getBannerId);
            comparatorChain.thenComparing(comparator);
            comparatorChain = comparator;
        }

        if (priority.getAdvCampaignId() != null) {
            Comparator<Banner> comparator = createFieldComparator(priority.getAdvCampaignId(), Banner::getAdvCampaignId);
            comparatorChain.thenComparing(comparator);
            comparatorChain = comparator;
        }

        return comparatorChain;
    }

    private <T, U extends Comparable<? super U>> Comparator<T> createFieldComparator(int priority, Function<T, U> keyExtractor) {
        Comparator<T> comparator = Comparator.comparing(keyExtractor);

        return priority > 0 ? comparator : comparator.reversed();
    }
}
