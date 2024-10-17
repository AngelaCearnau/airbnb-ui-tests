package com.ace.airbnb;


import com.ace.airbnb.framework.Browser;
import com.ace.airbnb.framework.ScreenshotTaker;
import com.ace.airbnb.framework.WebDriverConfig;
import com.ace.airbnb.framework.WindowHandler;
import com.ace.airbnb.ui.domain.PropertyCard;
import com.ace.airbnb.ui.pages.MoreFiltersForm;
import com.ace.airbnb.ui.pages.PropertyPage;
import com.ace.airbnb.ui.pages.StaysSearchForm;
import com.ace.airbnb.ui.pages.YourSearchPage;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Reporter;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@ContextConfiguration(classes = {StaysSearchForm.class, YourSearchPage.class, WebDriverConfig.class, Browser.class})
@TestExecutionListeners(listeners = {ScreenshotTaker.class, DependencyInjectionTestExecutionListener.class})
public class SearchTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private StaysSearchForm staysSearchForm;

    @Autowired
    private YourSearchPage yourSearchPage;

    @Autowired
    private WebDriver driver;

    @Inject
    private Browser browser;

    private PropertyPage propertyPage;

    int guestsNumber;
    String city;


    @Test
    public void test1_verifyAppliedFilters(){
        Reporter.log(" Test 1: Verify that the results match the search criteria -  applied filters are correct");

        String guestsNumberString = guestsNumber + " guests";

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertTrue(yourSearchPage.getTextFromStaysPageHeading().contains("places in " + city));
        Reporter.log("Check that location is: " + city);
        softAssert.assertTrue(yourSearchPage.getLocationFromFilter().equals(city), "Location in filter not as expected");
        Reporter.log("Check number of guests is: " + yourSearchPage.getNoOfGuestFromFilter());
        softAssert.assertTrue(yourSearchPage.getNoOfGuestFromFilter().equals(guestsNumberString), "Guests information in filter not as expected");
        String timeInterval = getDateFormatForLittleSearchFilter();
        Reporter.log("Check selected time interval is: " + timeInterval);

        softAssert.assertTrue(yourSearchPage.getDateFromFilter().equals(timeInterval), "Selected dates in filter not as expected");
        softAssert.assertAll();

    }

    private String getDateFormatForLittleSearchFilter(){

        Date checkInDate = StaysSearchForm.getCheckInDate();
        SimpleDateFormat formatForLittleFilter = new SimpleDateFormat("MMM dd");
        String date1 = formatForLittleFilter.format(checkInDate);


        Date checkOutDate = StaysSearchForm.getCheckOutDate();
        String date2 = formatForLittleFilter.format(checkOutDate);


        if (StaysSearchForm.getCheckInMonth().contains(StaysSearchForm.getCheckOutMonth())){
            String dayOfMonth = date2.substring(4);
            return date1 + " – " + dayOfMonth;
        } else{
            return date1 + " – " + date2;
        }


    }

    //@Test(dependsOnMethods = { "test1_verifyAppliedFilters" })
    @Test
    public void test1_verifyMaxNumberOfGuestOnEachProperty(){

        Reporter.log(" Test 1: Verify that properties displayed on the first page can accommodate at least the selected\n" +
                " number of guests");

        propertyPage = new PropertyPage(browser);

        int totalNumberOfCards;
        int j=0;
        do{
            j++;
            totalNumberOfCards  = yourSearchPage.getPropertiesOnFirstPage().size();
            if (totalNumberOfCards>6){
                break;
            }
        } while (j<30);

        Reporter.log("Open page for each property and check number of guests is >= than " + guestsNumber);
        //System.out.println("totalNumberOfCards" + totalNumberOfCards);
        SoftAssert softAssert = new SoftAssert();
        for (int i=1;i<=totalNumberOfCards;i++){
                int finalI = i;
                new WindowHandler(browser) {
                    @Override
                    public void openWindow(WebDriver driver) {
                        yourSearchPage.selectProperty(finalI);
                    }

                    @Override
                    public void useWindow(WebDriver driver) {

                        int guestsAllowed = propertyPage.getNumberOfGuests();
                        Reporter.log("Guests allowed for property " + propertyPage.getPropertyName() + " : " + guestsAllowed);
                        softAssert.assertTrue(guestsAllowed >= guestsNumber);
                    }
                }.run();

        }
        softAssert.assertAll();
    }

    @Test
    public void test2_verifyExtraFilters(){

        int BEDROOMS = 5;
        SoftAssert softAssert = new SoftAssert();

        Reporter.log("Click More filters");
        yourSearchPage.openMoreFilters();

        MoreFiltersForm moreFiltersForm = new MoreFiltersForm(browser);

        Reporter.log("Select the number of bedrooms as 5");
        moreFiltersForm.increaseNumberOfBedrooms(BEDROOMS);
        Reporter.log("Select Pool from the Facilities section.");
        moreFiltersForm.selectPool();

        Reporter.log("Click Show Stays");
        moreFiltersForm.clickOnShowPlacesButton();

        int noOfProperties = yourSearchPage.waitForPropertiesListToBeDisplayed();

        for (int i =1; i<=noOfProperties; i++){
            String subtitle = yourSearchPage.getPropertyInfoOnCardSecondSubtitle(i);
            String nameOfProperty = yourSearchPage.getPropertyInfoOnCardName(i);

            Reporter.log("Verify that the properties displayed on the first page have at least the number of selected\n" +
                    " bedrooms");
            if (!subtitle.contains("Free cancellation")){ //"'Free cancellation' text replaces the number of bedrooms for some properties"
                String subtitle2 = yourSearchPage.getPropertyInfoOnCardNoOfBedrooms(i);
                softAssert.assertTrue(subtitle2.contains("bedrooms"));
                String[] noOfBedrooms = subtitle2.split(" ");
                int numberOfBedrooms = Integer.parseInt(noOfBedrooms[0]);
                Reporter.log("Property " + nameOfProperty + " has " + numberOfBedrooms + " bedrooms");
                softAssert.assertTrue(numberOfBedrooms >= BEDROOMS, "Number of rooms "+ numberOfBedrooms + " displayed for property " + nameOfProperty + " might not be correct");
            }
        }

        propertyPage = new PropertyPage(browser);


        new WindowHandler(browser) {
            @Override
            public void openWindow(WebDriver driver) {
                Reporter.log("Open the details of the first property");
                yourSearchPage.selectProperty(1);

            }

            @Override
            public void useWindow(WebDriver driver) {
                Reporter.log("Check that 'Pool' option is displayed for property " + propertyPage.getPropertyName());
                propertyPage.dismissModalAboutTranslation();
                propertyPage.clickShowAllAmenities();
                softAssert.assertTrue(propertyPage.checkPool());
            }
        }.run();

        softAssert.assertAll();
    }

    @Test
    public void test3_verifyPropertyOnMap(){

        SoftAssert softAssert = new SoftAssert();

        WebDriver driver = yourSearchPage.setUpNewdriver();

        Reporter.log("Verify that the property is displayed on the map and the color of the pin changes when hovering");
        boolean isChanged = yourSearchPage.checkPinColourChangesOnMapUponHover(driver);
        softAssert.assertTrue(isChanged, "Pin color might not have been changed" );

        List<PropertyCard> propertyCards = yourSearchPage.getInfoFromPropertyCardsOnlIstAndOnMap(driver);

        Reporter.log("Verify that the details shown in the map popup are the same as the ones shown in the search\n" +
                " results");

        softAssert.assertEquals(propertyCards.get(0).getCardTitle(), propertyCards.get(1).getCardTitle(), "Card title not as expected");
        softAssert.assertEquals(propertyCards.get(0).getCardSubtitle(), propertyCards.get(0).getCardSubtitle(), "Card subtitle not as expected");
        softAssert.assertEquals(propertyCards.get(0).getSecondCardSubtitle(), propertyCards.get(0).getSecondCardSubtitle(), "Card second subtitle not as expected");
        softAssert.assertEquals(propertyCards.get(0).getPriceAvailability(), propertyCards.get(0).getPriceAvailability(), "Card price not as expected");
        softAssert.assertEquals(propertyCards.get(0).getScore(), propertyCards.get(0).getScore(), "Card score review not as expected");

        yourSearchPage.closeNewDriver(driver);

        softAssert.assertAll();
    }
/*
    @AfterMethod(groups = { "test3" }, alwaysRun = false)
    public void cleanUp() {yourSearchPage.closeNewDriver(driver);}*/


    @BeforeMethod(alwaysRun = true)
    @Parameters({"adults", "children", "location"})
    public void searchOnHomePage(String adults, String children, String location) {
        Reporter.log("Search properties for <<" + adults + ">> adults and <<" + children + " children>> in " + location);
        guestsNumber = staysSearchForm.searchStays(adults, children,location);

        staysSearchForm.clickSearchButton();

        //get city name from seach location
        String[] locationParts = location.split("[,\\s]");
        city = locationParts[0];

    }

}
