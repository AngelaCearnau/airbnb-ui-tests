package com.ace.airbnb.framework;

import org.openqa.selenium.WebDriver;

import java.time.Duration;
import java.util.Set;

class WebDriverCleaner {
    WebDriver cleanWebDriver(WebDriver driver) {

        WebDriver.Options manage = driver.manage();

        manage.deleteAllCookies();

        WebDriver.Timeouts timeouts = manage.timeouts();

        timeouts.implicitlyWait(Duration.ofSeconds(1));
        timeouts.scriptTimeout(Duration.ofSeconds(5));

        closeRedundantWindows(driver);

        return driver;
    }

    private void closeRedundantWindows(WebDriver driver) {
        Set<String> windowHandles = driver.getWindowHandles();
        if (windowHandles.size() > 1) {

            driver.switchTo().defaultContent();

            String topWindowHandle = driver.getWindowHandle();

            for (String windowHandle : windowHandles) {
                if (!windowHandle.equals(topWindowHandle)) {
                    driver.switchTo().window(windowHandle);
                    driver.close();
                }
            }
        }
    }
}
