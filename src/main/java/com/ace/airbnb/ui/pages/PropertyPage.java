package com.ace.airbnb.ui.pages;

import com.ace.airbnb.framework.Browser;
import com.ace.airbnb.framework.WebDriverConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.testng.Reporter;

import static com.ace.airbnb.locators.SelectorsPropertyPage.*;

@ContextConfiguration(classes = {WebDriverConfig.class, Browser.class})
public class PropertyPage {

    @Autowired
    private com.ace.airbnb.framework.Browser browser;


    public PropertyPage(Browser browser) {
        this.browser = browser;
    }

    public int getNumberOfGuests(){

        String guestsInfo = browser.getText(OVERVIEW_SECTION);
        String[] guestsInfoArray = guestsInfo.split(" ");

        return Integer.parseInt(guestsInfoArray[0]);
    }

    public String getPropertyName(){
        return browser.getText(PROPERTY_NAME);
    }

    public void dismissModalAboutTranslation(){
        try{
            browser.click(TRANSLATION_MODAL);
        } catch (Exception e){
            Reporter.log("Translation modal not displayed");
        }

    }

    public void clickShowAllAmenities(){
        browser.click(SHOW_AMENITIES);
    }

    public boolean checkPool(){
        return browser.awaitOptional(FACILITIES_SECTION_POOL);
    }
}
