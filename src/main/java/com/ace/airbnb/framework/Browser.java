package com.ace.airbnb.framework;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;

import java.util.function.Supplier;

public class Browser extends DelegatingWebDriver implements FormElements{


    public Browser(WebDriver driver) {
        super(driver);
    }

    public void doubleClick(Supplier<By> by) {
        new Actions(this)
            .doubleClick(await(by))
            .perform();
    }

    public void hoverOn(Supplier<By> by) {
        new Actions(this)
                .moveToElement(await(by))
                .perform();
    }

}