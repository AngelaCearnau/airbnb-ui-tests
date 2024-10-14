package com.ace.airbnb.ui.pages;

import com.ace.airbnb.framework.Browser;
import com.ace.airbnb.framework.Element;
import com.ace.airbnb.framework.WebDriverConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.stream.Collectors;

import static com.ace.airbnb.locators.SelectorsYourSearchPage.*;

@ContextConfiguration(classes = {WebDriverConfig.class, Browser.class})
public class YourSearchPage {

    @Autowired
    private com.ace.airbnb.framework.Browser browser;

    //private Element heading;
    //private WebDriverWait wait;


    //private Browser browser;

    public YourSearchPage(Browser browser) {
        this.browser = browser;
        //wait = new WebDriverWait(browser, Duration.ofSeconds(Constants.TIMEOUT_DEFAULT));
    }


    public String getTextFromStaysPageHeading(){
        //System.out.println("text:" + browser.getText(STAYS_PAGE_HEADING));
        browser.awaitTextPresenceInElement(STAYS_PAGE_HEADING, "places in");
        //System.out.println("text:" + browser.getText(STAYS_PAGE_HEADING));
        //heading  = browser.await(STAYS_PAGE_HEADING);
        browser.awaitTextPresenceInElement(SEARCH_GUESTS, "guests");
        //System.out.println("text2:" + browser.getText(SEARCH_GUESTS));
        return browser.getText(STAYS_PAGE_HEADING);
    }


    public void selectProperty(int index){
        String propWithIndex = String.valueOf(index);
        browser.click(PROPERTY_CARD.xpathWithParam(propWithIndex));
    }


    public List<Element> getPropertiesOnFirstPage(){

        return browser.findElements(PROPERTIES_DISPLAYED).collect(Collectors.toList());
    }

    public String getLocationFromFilter(){
        return browser.getText(SEARCH_LOCATION);
    }

    public String getDateFromFilter(){
        return browser.getText(SEARCH_DATE);
    }

    public String getNoOfGuestFromFilter(){
        return browser.getText(SEARCH_GUESTS);
    }

}
