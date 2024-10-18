package com.ace.airbnb.ui.pages;

import com.ace.airbnb.framework.Browser;
import com.ace.airbnb.framework.Element;
import com.ace.airbnb.framework.WebDriverConfig;
import com.ace.airbnb.ui.domain.PropertyCard;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.testng.Reporter;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.ace.airbnb.locators.SelectorsYourSearchPage.*;

@ContextConfiguration(classes = {WebDriverConfig.class, Browser.class})
public class YourSearchPage {

    @Autowired
    private WebDriver driver;

    @Autowired
    private com.ace.airbnb.framework.Browser browser;


    public YourSearchPage(Browser browser) {
        this.browser = browser;
    }


    public String getTextFromStaysPageHeading(){

        new WebDriverWait(browser, Duration.ofSeconds(20)).until(
                webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));

        browser.awaitTextPresenceInElement(STAYS_PAGE_HEADING, "places in");
        browser.awaitTextPresenceInElement(SEARCH_GUESTS, "guests");

        return browser.getText(STAYS_PAGE_HEADING);
    }


    public void selectProperty(int index){
        String propWithIndex = String.valueOf(index);
        browser.click(PROPERTY_CARD.xpathWithParam(propWithIndex));
    }

    public String getPropertyInfo(int index){
        String propWithIndex = String.valueOf(index);
        return browser.getText(PROPERTY_CARD.xpathWithParam(propWithIndex));
    }

    public String getPropertyInfoOnCardNoOfBedrooms(int index){
        String propWithIndex = String.valueOf(index);
        return browser.getText(PROPERTY_CARD_SECOND_SUBTITLE_BEDROOMS.xpathWithParam(propWithIndex));
    }

    public String getPropertyInfoOnCardSecondSubtitle(int index){
        String propWithIndex = String.valueOf(index);
        return browser.getText(PROPERTY_CARD_SECOND_SUBTITLE.xpathWithParam(propWithIndex));
    }

    public String getPropertyInfoOnCardSubtitle(int index){
        String propWithIndex = String.valueOf(index);
        return browser.getText(PROPERTY_CARD_SUBTITLE.xpathWithParam(propWithIndex));
    }

    public String getPropertyInfoOnCardPrice(int index){
        String propWithIndex = String.valueOf(index);
        return browser.getText(PRICE_AVAILABILITY.xpathWithParam(propWithIndex));
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
            totalNumberOfCards  = getPropertiesOnFirstPage().size();
            if (totalNumberOfCards==expectedNumberOfCards){
                break;
            }
        } while (j<30);

        return totalNumberOfCards;
    }


    private PropertyCard getInfoOnPropertyCard(){
        String propWithIndex = String.valueOf(1);
        WebElement element =  driver.findElement(PROPERTY_CARD_INFO_NAME.xpathWithParam(propWithIndex).get());
        String name= element.getText();

        element =  driver.findElement(PROPERTY_CARD_SUBTITLE.xpathWithParam(propWithIndex).get());
        String subtitle=element.getText();

        element =  driver.findElement(PROPERTY_CARD_SECOND_SUBTITLE.xpathWithParam(propWithIndex).get());
        String secondSubtitle=element.getText();

        element =  driver.findElement(PRICE_AVAILABILITY.xpathWithParam(propWithIndex).get());

        String price=element.getText();

        element =  driver.findElement(PROPERTY_CARD_INFO_REVIEW_SCORE.xpathWithParam(propWithIndex).get());
        String score=element.getText();
        return new PropertyCard(name, subtitle, secondSubtitle, price, score);
    }

    private PropertyCard getInfoOnSelectedPropertyCardFromMap(){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.presenceOfElementLocated(CLICKED_ON_MAP_CARD.get()));

        WebElement element =  driver.findElement(CLICKED_ON_MAP_CARD.get());

        WebElement card_name =  element.findElement(CLICKED_ON_MAP_CARD_TITLE.get());
        String name= card_name.getText();

        WebElement card_subtitle =  element.findElement(CLICKED_ON_MAP_CARD_SUBTITLE.get());
        String subtitle= card_subtitle.getText();

        WebElement card_secondsubtitle =  element.findElement(CLICKED_ON_MAP_CARD_SECOND_SUBTITLE.get());
        String secondSubtitle= card_secondsubtitle.getText();

        WebElement card_displayedPrice =  element.findElement(CLICKED_ON_MAP_CARD_PRICING.get());
        String price= card_displayedPrice.getText();

        WebElement card_displayedScore =  element.findElement(CLICKED_ON_MAP_CARD_SCORE.get());
        String score = card_displayedScore.getText();

        return new PropertyCard(name, subtitle, secondSubtitle, price, score);
    }


    public WebDriver setUpNewdriver(){
        String url = browser.getCurrentUrl();
        driver = new ChromeDriver();
        driver.get(url);
        driver.manage().window().maximize();
        new WebDriverWait(driver, Duration.ofSeconds(20)).until(
                webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(4));
        return driver;

    }

    public boolean checkPinColourChangesOnMapUponHover(WebDriver driver) {

        //String WHITE = "(255, 255, 255, 1)";
        //String BLACK = "(34, 34, 34, 1)";

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.presenceOfElementLocated(PROPERTY_CARD_TEST.get()));



        List<WebElement> pins = driver.findElements(PIN_ON_MAP_STYLE.get());
        String pinColor = pins.get(1).getCssValue("background-color");
        Reporter.log("Get color of the first pin on page before hover: " + pinColor);


        //perform hover over first property in list
        new Actions(driver)
                .moveToElement(driver.findElement(PROPERTY_CARD_TEST.get()))
                .perform();

        Reporter.log("Performed hover upon first property in the list ");

        //get selected location from map
        WebElement element = driver.findElement(SELECTED_ON_MAP_BUTTON_STYLE.get());

        String selectedPinColor = element.getCssValue("background-color");
        Reporter.log("background color for the pin after hover: " + selectedPinColor);

        return !selectedPinColor.equals(pinColor);

    }


    public List<PropertyCard> getInfoFromPropertyCardsOnlIstAndOnMap(WebDriver driver){

        Reporter.log("Click on the identified property on the map");
        driver.findElement(SELECTED_ON_MAP_BUTTON.get()).click();
        PropertyCard propertyCardOnList = getInfoOnPropertyCard();
        List<PropertyCard> list = new ArrayList<>();
        list.add(propertyCardOnList);

        PropertyCard propertyCardOnMap =  getInfoOnSelectedPropertyCardFromMap();
        list.add(propertyCardOnMap);
        return list;
    }

}
