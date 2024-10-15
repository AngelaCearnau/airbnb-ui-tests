package com.ace.airbnb.locators;

import org.openqa.selenium.By;

import java.util.function.Supplier;

import static org.openqa.selenium.By.xpath;


public enum SelectorsPropertyPage implements Supplier<By> {



    // overview
    PROPERTY_NAME("//*[@data-section-id='TITLE_DEFAULT']//h1"),
    OVERVIEW_SECTION("//*[@data-plugin-in-point-id='OVERVIEW_DEFAULT_V2']//ol/li[1]"),

    TRANSLATION_MODAL("//div[@role='dialog']//button"),
    SHOW_AMENITIES("//div[@data-section-id='AMENITIES_DEFAULT']//button"),
    FACILITIES_SECTION("//div[@role='dialog']//h2[contains(text(),'facilities')]"),
    FACILITIES_SECTION_POOL("//div[@role='dialog']//h2[contains(text(),'facilities')]/parent::div/following-sibling::ul/li//div[contains(text(),'Pool')]"),



;

    private By by;


    SelectorsPropertyPage(String id) {
        this.by = xpath(id);
    }


    @Override
    public By get() {
        return by;
    }

    @Override
    public String toString() {
        return by.toString();
    }



}