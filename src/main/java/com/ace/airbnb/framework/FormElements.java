package com.ace.airbnb.framework;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

import java.util.function.Supplier;


import static java.util.concurrent.TimeUnit.SECONDS;

public interface FormElements extends ExplicitWait {

    default void setInputText(Supplier<By> by, Object value) {
        Retry retry = new Retry(5, 1, SECONDS);

        retry.attempt(
            () -> {
                Element element = await(by);
                element.sendKeys(value.toString());
            }
        );
    }

    default String getInputText(Supplier<By> by) {
        return await(by).getValue();
    }

    default Select getSelect(Supplier<By> by) {
        Element element = await(by);
        return new Select(element);
    }

    default void selectByVisibleText(Supplier<By> by, Object ... values) {
        for (Object v: values) {
            getSelect(by).selectByVisibleText(v.toString());
            try {
                if (!getSelect(by)
                        .getFirstSelectedOption()
                        .getText()
                        .equals(v.toString())) {
                    getSelect(by)
                            .getOptions()
                            .stream()
                            .filter(
                                    e -> e.getText().equals(v.toString()))
                            .findFirst()
                            .get()
                            .click();
                }
            } catch (Exception e) {
                //Don't need to handle it.
            }
        }
    }


}
