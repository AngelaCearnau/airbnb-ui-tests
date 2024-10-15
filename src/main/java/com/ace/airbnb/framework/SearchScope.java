package com.ace.airbnb.framework;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

public interface SearchScope extends SearchContext {

    default Element findElement(Supplier<By> by) {
        return new Element(findElement(by.get()));
    }

    default Stream<Element> findElements(Supplier<By> by) {
        return findElements(by.get()).stream().map(Element::new);
    }

    default Element findElementByText(Supplier<By> by, String text) {
        return findElements(by).filter(p -> p.getText().equals(text)).findFirst().get();
    }

    default Optional<Element> optionalElement(Supplier<By> by) {
        try {
            return Optional.of(findElement(by));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    default boolean isPresent(Supplier<By> by) {
        return optionalElement(by).isPresent();
    }

    default ExpectedCondition<WebElement> isClickable(Supplier<By> by) {
        return ExpectedConditions.elementToBeClickable(by.get());
    }

    default ExpectedCondition<WebElement> isVisible(Supplier<By> by) {
        return ExpectedConditions.visibilityOfElementLocated(by.get());
    }

    default ExpectedCondition<Boolean> textToBePresentInElement(Supplier<By> by, String text) {
        return ExpectedConditions.textToBePresentInElementLocated(by.get(), text);
    }


    default String getText(Supplier<By> by) {
        return new Element(findElement(by.get())).getText();
    }

    default boolean textFromElementContains(Element element, String pattern) {
        return element.getText().contains(pattern);
    }
}
