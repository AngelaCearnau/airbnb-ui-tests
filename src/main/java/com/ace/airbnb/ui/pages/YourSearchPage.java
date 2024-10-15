package com.ace.airbnb.ui.pages;

import com.ace.airbnb.framework.Browser;
import com.ace.airbnb.framework.Element;
import com.ace.airbnb.framework.WebDriverConfig;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.time.Duration;
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

        new WebDriverWait(browser, Duration.ofSeconds(20)).until(
                webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));

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

    public String getPropertyInfoOnCardNoOfBedrooms(int index){
        String propWithIndex = String.valueOf(index);
        return browser.getText(PROPERTY_CARD_SECOND_SUBTITLE_BEDROOMS.xpathWithParam(propWithIndex));
    }

    public String getPropertyInfoOnCardSecondSubtitle(int index){
        String propWithIndex = String.valueOf(index);
        return browser.getText(PROPERTY_CARD_SECOND_SUBTITLE.xpathWithParam(propWithIndex));
    }

    public String getPropertyInfoOnCardName(int index){
        String propWithIndex = String.valueOf(index);
        return browser.getText(PROPERTY_CARD_INFO_NAME.xpathWithParam(propWithIndex));
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

    public void openMoreFilters(){
        browser.click(MORE_FILTERS);
    }

    public int waitForPropertiesListToBeDisplayed(){

        new WebDriverWait(browser, Duration.ofSeconds(20)).until(
                webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
        String noOfPlaces;
        do{
            noOfPlaces = getTextFromStaysPageHeading();
        } while (noOfPlaces.isEmpty());

        String[] numberOfPlaces = noOfPlaces.split(" ");
        int expectedNumberOfCards = Integer.parseInt(numberOfPlaces[0]);
        int totalNumberOfCards;
        int j=0;
        do{
            j++;
            //System.out.println("Counter" +  j);
            totalNumberOfCards  = getPropertiesOnFirstPage().size();
            if (totalNumberOfCards==expectedNumberOfCards){
                break;
            }
        } while (j<30);
        //int totalNumberOfCards  = yourSearchPage.getPropertiesOnFirstPage().size();
        System.out.println("totalNumberOfCards" + totalNumberOfCards);
        return totalNumberOfCards;
    }




}
