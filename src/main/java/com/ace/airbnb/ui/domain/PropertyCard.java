package com.ace.airbnb.ui.domain;

public class PropertyCard {

    private String cardTitle;
    private String cardSubtitle;
    private String secondCardSubtitle;
    private String priceAvailability;
    private String score;


    public PropertyCard(String cardTitle, String cardSubtitle, String secondCardSubtitle, String priceAvailability, String score) {
        this.cardTitle = cardTitle;
        this.cardSubtitle = cardSubtitle;
        this.secondCardSubtitle = secondCardSubtitle;
        this.priceAvailability = priceAvailability;
        this.score = score;

    }

    public String getCardTitle() {
        return cardTitle;
    }

    public String getCardSubtitle() {
        return cardSubtitle;
    }

    public String getSecondCardSubtitle() {
        return secondCardSubtitle;
    }

    public String getPriceAvailability() {
        return priceAvailability;
    }

    public String getScore() {
        return score;
    }
}
