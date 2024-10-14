package com.ace.airbnb.framework;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import java.util.List;

public class DelegatingSearchContext<T extends SearchContext>
    implements ExplicitWait {
    protected final T delegate;

    public DelegatingSearchContext(T delegate) {
        this.delegate = delegate;
    }

    @Deprecated
    @Override
    public List<WebElement> findElements(By by) {
        return delegate.findElements(by);
    }

    @Deprecated
    @Override
    public Element findElement(By by) {
        return new Element(delegate.findElement(by));
    }


}
