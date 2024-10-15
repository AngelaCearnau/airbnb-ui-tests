package com.ace.airbnb.ui.pages;

import com.ace.airbnb.framework.Browser;
import com.ace.airbnb.framework.WebDriverConfig;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.Date;

import static com.ace.airbnb.locators.SelectorsHomePage.*;
import static com.ace.airbnb.locators.SelectorsYourSearchPage.STAYS_PAGE_HEADING_TEXT;

@ContextConfiguration(classes = {WebDriverConfig.class, Browser.class})
public class StaysSearchForm {



    @Autowired
    private WebDriver driver;

    @Autowired
    private URI baseUrl;
    @Autowired
    private com.ace.airbnb.framework.Browser browser;

    static Date checkInDate;
    static Date checkOutDate;
    static String checkInMonth;
    static String checkOutMonth;

    public static Date getCheckInDate() {
        return checkInDate;
    }


    public static Date getCheckOutDate() {
        return checkOutDate;
    }

    public static String getCheckInMonth() {
        return checkInMonth;
    }

    public static String getCheckOutMonth() {
        return checkOutMonth;
    }

    public void openPage() {
        driver.get(baseUrl.toString());
        driver.manage().window().maximize();
    }

    public int searchStays(String adults, String children, String location){
        openPage();


        browser.await(EXPLORE_HEADER_WHERE).click();
        browser.await(EXPLORE_HEADER_WHERE_INPUT).sendKeys(location);


        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 7);
        checkInDate = calendar.getTime();


        SimpleDateFormat formatForCalendar = new SimpleDateFormat("MM/dd/yyyy");
        String date1 = formatForCalendar.format(checkInDate);

        checkInMonth = new SimpleDateFormat("MMM").format(calendar.getTime());
       // System.out.println("Date to select: " +date1);


        browser.click(EXPLORE_HEADER_CHECKIN);
        browser.await(EXPLORE_HEADER_DATES);
        browser.click(CALENDAR_SELECT_DATE.xpathWithParam(date1));


        calendar.add(Calendar.DATE, 7);
        checkOutDate = calendar.getTime();
        String date2 = formatForCalendar.format(checkOutDate);

        checkOutMonth = new SimpleDateFormat("MMM").format(calendar.getTime());
        //System.out.println("Date to checkout: " +date2);

        browser.click(CALENDAR_SELECT_DATE.xpathWithParam(date2));


        browser.await(EXPLORE_HEADER_GUESTS_INPUT).click();

        int guests=0;
        int numberOfAdults = Integer.parseInt(adults);
        for(int i=1;i<=numberOfAdults; i++){
            browser.click(EXPLORE_HEADER_GUESTS_INPUT_ADULTS_INCREASE);
            guests++;
        }

        int numberOfChildren = Integer.parseInt(children);
        for(int i=1;i<=numberOfChildren; i++){
            browser.click(EXPLORE_HEADER_GUESTS_INPUT_CHILDREN_INCREASE);
            guests++;
        }

        //System.out.println("Guests: " + guests);

        browser.click(EXPLORE_HEADER_WHERE);
        return guests;

    }

    public void clickSearchButton(){
        browser.click(EXPLORE_HEADER_WHERE_INPUT);
        browser.click(SEARCH_BUTTON);
        browser.await(STAYS_PAGE_HEADING_TEXT);
        new WebDriverWait(browser, Duration.ofSeconds(20)).until(
                webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
        ;

/*        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }*/

/*
        new WebDriverWait(browser, Duration.ofSeconds(20)).until(
                webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
        ;
        browser.awaitVisible(FIRST_CARD);*/
    }

}
