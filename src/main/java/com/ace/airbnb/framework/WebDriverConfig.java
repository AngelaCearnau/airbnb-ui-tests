package com.ace.airbnb.framework;

import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.AbstractMap;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class WebDriverConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebDriverConfig.class);

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public WebDriverFactory webDriverFactory(
            @Value("${webdriver.remote:false}") boolean remoteDriver,
            @Value("${webdriver.remote.url:http://computer}") URL remoteUrl) {
        return new WebDriverFactory(remoteDriver, remoteUrl);
    }

    @Bean
    public WebDriverCleaner webDriverCleaner() {
        return new WebDriverCleaner();
    }



    @Bean
    public DesiredCapabilities desiredCapabilities(@Value("${webdriver.capabilities.browserName:chrome}") String browserName) throws Exception {
        DesiredCapabilities capabilities = new DesiredCapabilities(browserName, "", Platform.ANY);

        populateCapabilities(capabilities);

        LOGGER.info("capabilities = {}", capabilities);

        return capabilities;
    }

    private void populateCapabilities(DesiredCapabilities capabilities) {
        String prefix = "webdriver.capabilities.";

        Map<String, String> map = System.getProperties().entrySet().stream()
                .map(e -> new AbstractMap.SimpleImmutableEntry<>(
                        String.valueOf(e.getKey()), String.valueOf(e.getValue())))
                .filter(e -> e.getKey().startsWith(prefix))
                .map(e -> new AbstractMap.SimpleImmutableEntry<>(
                        e.getKey().substring(prefix.length()), e.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        new MapFactory().create(map).forEach(capabilities::setCapability);
    }


    @Bean
    @Primary
    @Scope("prototype")
    public WebDriver webDriver(WebDriverCleaner webDriverCleaner,
                               @Qualifier("dirtyWebDriver") WebDriver driver) {
        return webDriverCleaner.cleanWebDriver(driver);
    }

    @Bean(destroyMethod = "quit")
    @Lazy
    public WebDriver dirtyWebDriver(WebDriverFactory webDriverFactory,
                                    DesiredCapabilities desiredCapabilities,
                                    URI baseUrl) throws IOException {
        return webDriverFactory.webDriver(desiredCapabilities, baseUrl);
    }

    @Primary
    @Bean
    //public URI baseUrl(@Value("${webdriver.baseUrl:http://auto}") URI value)
    public URI baseUrl(@Value("${webdriver.baseUrl:https://www.airbnb.com}") URI value)
            throws UnknownHostException {
        if (value.equals(URI.create("http://auto"))) {
            return URI.create("http://" + InetAddress.getLocalHost().getHostAddress() + ":8080");
        }
        return value;
    }



}
