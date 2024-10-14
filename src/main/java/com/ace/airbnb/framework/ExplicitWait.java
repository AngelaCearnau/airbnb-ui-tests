package com.ace.airbnb.framework;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.FluentWait;

import java.time.Duration;
import java.util.function.Function;
import java.util.function.Supplier;

public interface ExplicitWait extends SearchScope {

    default Element await(Supplier<By> by) {
        return await((SearchScope e) -> e.findElement(by), Constants.TIMEOUT_DEFAULT);
    }

    default void awaitClickable(Supplier<By> by) {
        await((SearchScope e) -> e.isClickable(by), Constants.TIMEOUT_DEFAULT);
    }

    default boolean awaitDisplayed(Supplier<By> by) {

        return await((SearchScope e) -> e.findElement(by).isDisplayed(), Constants.TIMEOUT_DEFAULT);
    }

    default void awaitVisible(Supplier<By> by) {
        await((SearchScope e) -> e.isVisible(by), Constants.TIMEOUT_DEFAULT);
    }

    default String getElementAttribute(Supplier<By> by, String value) {
        return await(by).getAttribute(value);
    }

    default <T> T await(Function<SearchScope, T> function, long timeout) {
        return new FluentWait<>(this)
                .withTimeout(Duration.ofSeconds(timeout))
                .pollingEvery(Duration.ofMillis(10))
                .ignoring(Exception.class)
                .until(
                        function
                );
    }

    default String getText(Supplier<By> by) {
        return await(by).getText();
    }

    default void click(Supplier<By> by) {
        await(by).click();
    }

    default boolean elementIsDisplayed(Supplier<By> by) {
        return await(by).isDisplayed();
    }

    default void awaitTextPresenceInElement(Supplier<By> by, String text) {
        await((SearchScope e) -> e.textToBePresentInElement(by, text), Constants.TIMEOUT_DEFAULT);
    }


}
