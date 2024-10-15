package com.ace.airbnb;


import com.ace.airbnb.framework.Browser;
import com.ace.airbnb.framework.ScreenshotTaker;
import com.ace.airbnb.framework.WebDriverConfig;
import com.ace.airbnb.framework.WindowHandler;
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
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.text.SimpleDateFormat;
import java.util.Date;

@ContextConfiguration(classes = {StaysSearchForm.class, WebDriverConfig.class, Browser.class})
@TestExecutionListeners(listeners = {ScreenshotTaker.class, DependencyInjectionTestExecutionListener.class})
public class SearchTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private StaysSearchForm staysSearchForm;


    @Autowired
    private Browser browser;

    private YourSearchPage yourSearchPage;
    private PropertyPage propertyPage;

    int guestsNumber;
    String city;


    //@Test
    public void test1_verifyAppliedFilters(){
        Reporter.log(" Test 1: Verify that the results match the search criteria -  applied filters are correct");

        String guestsNumberString = guestsNumber + " guests";


        yourSearchPage = new YourSearchPage(browser);


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
    //@Test
    public void test1_verifyMaxNumberOfGuestOnEachProperty(){

        Reporter.log(" Test 1: Verify that properties displayed on the first page can accommodate at least the selected\n" +
                " number of guests");
        yourSearchPage = new YourSearchPage(browser);
        propertyPage = new PropertyPage(browser);

       /* yourSearchPage.waitForPropertiesListToBeDisplayed();
        int totalNumberOfCards  = yourSearchPage.getPropertiesOnFirstPage().size();*/
        int totalNumberOfCards;
        int j=0;
        do{
            j++;
            //System.out.println("Counter" +  j);
            totalNumberOfCards  = yourSearchPage.getPropertiesOnFirstPage().size();
            if (totalNumberOfCards>6){
                break;
            }
        } while (j<30);
        //int totalNumberOfCards  = yourSearchPage.getPropertiesOnFirstPage().size();
        System.out.println("totalNumberOfCards" + totalNumberOfCards);
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
                        //System.out.println("In the new window: " + propertyPage.getNumberOfGuests());

                        //SoftAssert softAssert = new SoftAssert();
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
        yourSearchPage = new YourSearchPage(browser);

        yourSearchPage.openMoreFilters();

        MoreFiltersForm moreFiltersForm = new MoreFiltersForm(browser);

        moreFiltersForm.increaseNumberOfBedrooms(BEDROOMS);
        moreFiltersForm.selectPool();

        moreFiltersForm.clickOnShowPlacesButton();

        int noOfProperties = yourSearchPage.waitForPropertiesListToBeDisplayed();

        for (int i =1; i<=noOfProperties; i++){
            String subtitle = yourSearchPage.getPropertyInfoOnCardSecondSubtitle(i);
            String nameOfProperty = yourSearchPage.getPropertyInfoOnCardName(i);

            if (!subtitle.contains("Free cancellation")){
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
