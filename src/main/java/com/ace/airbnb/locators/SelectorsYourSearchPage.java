package com.ace.airbnb.locators;

import org.openqa.selenium.By;

import java.util.function.Supplier;

import static com.ace.airbnb.framework.Constants.PLACEHOLDER;
import static org.openqa.selenium.By.xpath;


public enum SelectorsYourSearchPage implements Supplier<By> {



    // home page search
    YOUR_SEARCH_SECTION("//*[@data-testid='category-item--Your search--checked']"),
    STAYS_PAGE_HEADING("//*[@data-testid='stays-page-heading']"),
    //STAYS_PAGE_HEADING_TEXT("//span[text() = 'Over 1,000 places in Rome']"),
    STAYS_PAGE_HEADING_TEXT("//span[contains(text(),'places in Rome')]"),
    SEARCH_LOCATION("//*[@data-testid='little-search-location']/div"),
    SEARCH_GUESTS("//*[@data-testid='little-search-guests']/div"),
    SEARCH_DATE("//*[@data-testid='little-search-date']/div"),

    PROPERTIES_DISPLAYED("//div[@itemprop='itemListElement']/parent::div/parent::div/parent::div[@class='']"),

    //PROPERTY_CARD("//div[@itemprop='itemListElement']/meta[@content='" + "{placeholder}" + "']/parent::div//div[@data-testid='card-container']"),
    PROPERTY_CARD("(//div[@itemprop='itemListElement']/parent::div/parent::div/parent::div[@class='']//div[@data-testid='card-container'])[" + PLACEHOLDER + "]"),

    PROPERTY_CARD_SECOND_SUBTITLE(PROPERTY_CARD.get(),"//*[@data-testid='listing-card-subtitle'][2]"),
    PROPERTY_CARD_SECOND_SUBTITLE_BEDROOMS(PROPERTY_CARD_SECOND_SUBTITLE.get(),"/span[2]"),
    PROPERTY_CARD_INFO_NAME(PROPERTY_CARD.get(),"//*[@data-testid='listing-card-title']"),
    //PROPERTY_CARD(PROPERTIES_DISPLAYED.get(), "(//div[@itemprop='itemListElement']/parent::div/parent::div/parent::div[@class='']//div[@data-testid='card-container'])[" + PLACEHOLDER + "]"),
//'listing-card-title'
    //'listing-card-subtitle'


    //more filters
    MORE_FILTERS("//button[@data-testid='category-bar-filter-button']"),
    INCREASE_NUMBER_OF_BEDROOMS("//div[@id='stepper-filter-item-min_bedrooms']//button[2]"),
    SHOW_MORE("//button[@type='button']/span[text()= 'Show more']"),


    //amenities
    FEATURES_POOL("//span[text() = 'Pool']"),
    FILTERS_SHOW_PLACES_BUTTON("//*[@aria-label='Filters']//footer/div/a")

;

    private By by;


    //static String PARAM = "{placeholder}";


    SelectorsYourSearchPage(String id) {
        this.by = xpath(id);
    }

    SelectorsYourSearchPage(By id, String id2) {
        String newid = clearString(id) + id2;
        this.by = xpath(newid);
    }

    public Supplier<By> xpathWithParam(String param) {
        String newxpath = clearString().replace(PLACEHOLDER, param);
        Supplier<By> aNewXpath= ()-> xpath(newxpath);
        return aNewXpath;
    }

    @Override
    public By get() {
        return by;
    }

    @Override
    public String toString() {
        return by.toString();
    }


    private String clearString() {
        return by.toString().replace("By.xpath: ", "");
    }

    public String clearString(By id) {
        return id.toString().replace("By.xpath: ", "");
    }


}