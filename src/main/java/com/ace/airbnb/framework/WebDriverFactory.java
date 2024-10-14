package com.ace.airbnb.framework;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;



public class WebDriverFactory {

    private final boolean remoteDriver;
    private final URL remoteUrl;

    WebDriverFactory(boolean remoteDriver, URL remoteUrl) {
        this.remoteDriver = remoteDriver;
        this.remoteUrl = remoteUrl;
    }

    WebDriver webDriver(DesiredCapabilities desiredCapabilities, URI baseUrl) throws IOException {

        WebDriver baseDriver = remoteDriver ?
                remoteDriver(remoteUrl, desiredCapabilities) :
                localDriver(desiredCapabilities);

        return new BaseUrlDriver(baseDriver, baseUrl);
    }


    @Bean(destroyMethod = "quit")
    public WebDriver webDriver(
            @Value("${webdriver.remote:false}") boolean remoteDriver,
            @Value("${webdriver.remote.url:http://localhost:4444/wd/hub}") URL remoteUrl,
            DesiredCapabilities desiredCapabilities) throws Exception {
        return remoteDriver ?
                remoteDriver(remoteUrl, desiredCapabilities) :
                localDriver(desiredCapabilities);
    }

    private WebDriver localDriver(DesiredCapabilities desiredCapabilities) throws IOException {
        File file;
        switch (desiredCapabilities.getBrowserName()) {
            case "chrome":
                file = new File("C:/webdrivers/chromedriver.exe");
                System.setProperty("webdriver.chrome.driver", file.getAbsolutePath());
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.setCapability("browserName", desiredCapabilities.getBrowserName());
                return new ChromeDriver(chromeOptions);

            default:
                throw new IllegalStateException("unknown browser " + desiredCapabilities.getBrowserName());
        }
    }

    private WebDriver remoteDriver(URL remoteUrl, DesiredCapabilities desiredCapabilities) {
       RemoteWebDriver driver = new RemoteWebDriver(remoteUrl, desiredCapabilities);
       driver.setFileDetector(new LocalFileDetector());
       return new Augmenter().augment(driver);
    }
}
