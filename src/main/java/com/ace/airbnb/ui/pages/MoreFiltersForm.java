package com.ace.airbnb.ui.pages;


import com.ace.airbnb.framework.Browser;
import com.ace.airbnb.framework.WebDriverConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static com.ace.airbnb.locators.SelectorsYourSearchPage.*;

@ContextConfiguration(classes = {WebDriverConfig.class, Browser.class})
public class MoreFiltersForm {


    @Autowired
    private com.ace.airbnb.framework.Browser browser;

    public MoreFiltersForm(Browser browser) {
        this.browser = browser;
    }


    public void selectPool(){
        browser.click(SHOW_MORE);
        String showPlacesText = getTextFromShowPlacesButton();

        browser.awaitClickable(FEATURES_POOL);
        browser.click(FEATURES_POOL);
        waitUntilNumberOfResultsChanges(showPlacesText);

    }

    public void increaseNumberOfBedrooms(int index){

        String showPlacesText = getTextFromShowPlacesButton();
        //System.out.println(showPlacesText);

        for (int i=0; i<index; i++){
            browser.click(INCREASE_NUMBER_OF_BEDROOMS);
        }
        waitUntilNumberOfResultsChanges(showPlacesText);
    }

    public void waitUntilNumberOfResultsChanges(String text){

        /*String showPlacesText = getTextFromShowButton();
        System.out.println(showPlacesText);*/
        String newText;
        do{
            newText = getTextFromShowPlacesButton();
        } while (newText.equals(text) || newText.equals("loading"));
        System.out.println(newText);
    }


    public String getTextFromShowPlacesButton(){
        return browser.getText(FILTERS_SHOW_PLACES_BUTTON);
    }

    public void clickOnShowPlacesButton(){
        browser.click(FILTERS_SHOW_PLACES_BUTTON);
    }

}
