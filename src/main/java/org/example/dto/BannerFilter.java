package org.example.dto;

import org.example.dto.entity.Country;

public class BannerFilter {
    private Country country;
    private Double minCost;
    private Long advCampaignId;
    private Long bannerId;

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Double getMinCost() {
        return minCost;
    }

    public void setMinCost(double minCost) {
        this.minCost = minCost;
    }

    public Long getAdvCampaignId() {
        return advCampaignId;
    }

    public void setAdvCampaignId(long advCampaignId) {
        this.advCampaignId = advCampaignId;
    }

    public Long getBannerId() {
        return bannerId;
    }

    public void setBannerId(long bannerId) {
        this.bannerId = bannerId;
    }
}
