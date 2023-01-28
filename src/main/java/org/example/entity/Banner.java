package org.example.entity;

import java.util.ArrayList;
import java.util.List;

public class Banner {
    private long bannerId;
    private long advCampaignId;
    private double cost;
    private List<Country> showAdsCountries;

    public Banner() {
        showAdsCountries = new ArrayList<>();
    }

    public long getBannerId() {
        return bannerId;
    }

    public void setBannerId(long bannerId) {
        this.bannerId = bannerId;
    }

    public long getAdvCampaignId() {
        return advCampaignId;
    }

    public void setAdvCampaignId(long advCampaignId) {
        this.advCampaignId = advCampaignId;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public List<Country> getShowAdsCountries() {
        return showAdsCountries;
    }

    public void setShowAdsCountries(List<Country> showAdsCountries) {
        this.showAdsCountries = showAdsCountries;
    }
}
