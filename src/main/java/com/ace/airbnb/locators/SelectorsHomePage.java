package com.ace.airbnb.locators;

import org.openqa.selenium.By;

import java.util.function.Supplier;

import static com.ace.airbnb.framework.Constants.PLACEHOLDER;
import static org.openqa.selenium.By.xpath;


public enum SelectorsHomePage implements Supplier<By> {



    // home page search
    EXPLORE_HEADER("//*[@data-testid='QA_EXPLORE_HEADER']"),
    EXPLORE_HEADER_WHERE_INPUT(EXPLORE_HEADER.get(),"//*[@data-testid='structured-search-input-field-query']"),

    EXPLORE_HEADER_WHERE(EXPLORE_HEADER.get(),"//div[contains(text(),'Where')]"),

    EXPLORE_HEADER_CHECKIN("//*[@data-testid='structured-search-input-field-split-dates-0']"),
    EXPLORE_HEADER_DATES("//*[@data-testid='expanded-searchbar-dates-calendar-tab']"),

    CALENDAR_SELECT_DATE("//*[@data-testid='" + PLACEHOLDER + "']"),



    EXPLORE_HEADER_GUESTS_INPUT(EXPLORE_HEADER.get(),"//*[@data-testid='structured-search-input-field-guests-button']"),

    EXPLORE_HEADER_GUESTS(EXPLORE_HEADER.get(),"//div[contains(text(),'Add guests')]"),
    EXPLORE_HEADER_GUESTS_INPUT_ADULTS("//*[@data-testid='search-block-filter-stepper-row-adults']"),
    EXPLORE_HEADER_GUESTS_INPUT_ADULTS_INCREASE("//*[@data-testid='stepper-adults-increase-button']"),
    EXPLORE_HEADER_GUESTS_INPUT_CHILDREN_INCREASE("//*[@data-testid='stepper-children-increase-button']"),

    SEARCH_BUTTON("//*[@data-testid='structured-search-input-search-button']")



;

    private By by;


    SelectorsHomePage(String id) {
        this.by = xpath(id);
    }

    public Supplier<By> xpathWithParam(String param) {
        String newxpath = clearString().replace(PLACEHOLDER, param);
        Supplier<By> aNewXpath= ()-> xpath(newxpath);
        return aNewXpath;
    }

    SelectorsHomePage(By id, String id2) {
        String newid = clearString(id) + id2;
        this.by = xpath(newid);
    }

    @Override
    public By get() {
        return by;
    }

    @Override
    public String toString() {
        return by.toString();
    }


    public String clearString(By id) {
        return id.toString().replace("By.xpath: ", "");
    }

    private String clearString() {
        return by.toString().replace("By.xpath: ", "");
    }


}