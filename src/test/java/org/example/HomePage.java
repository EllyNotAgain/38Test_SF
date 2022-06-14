package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.util.ArrayList;
import java.util.List;

public record HomePage(WebDriver driver) {
    private static final String ALL_COURSES_BUTTON_CLASS = "tn-elem__3843658031563736040421";
    private static final String FREE_EVENTS_BUTTON_CLASS = "tn-elem__3843658031563737497747";
    private static final String MENU_COURSES_CLASS = "t978__tm-link";
    private static final String MENU_COURSES_ITEM_CLASS = "t978__link-inner";
    private static final String CONTACTS_XPATH = "//a[@href='/contacts']";

    public void allCoursesButton() {
        driver.findElement(By.className(ALL_COURSES_BUTTON_CLASS)).click();
    }

    public void freeEventsButton() {
        driver.findElement(By.className(FREE_EVENTS_BUTTON_CLASS)).click();
    }

    public ArrayList<String> menuCourses() {
        Actions action = new Actions(driver);
        List<WebElement> elements = driver.findElements(By.className(MENU_COURSES_ITEM_CLASS));
        ArrayList<String> titles = new ArrayList<>();
        for (WebElement el: elements) {
            action.moveToElement(driver.findElement(By.className(MENU_COURSES_CLASS))).build().perform();
            action.keyDown(Keys.CONTROL).keyDown(Keys.SHIFT).click(el).build().perform();
            ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
            driver.switchTo().window(tabs.get(1));
            titles.add(driver.getTitle());
            driver.close();
            driver.switchTo().window(tabs.get(0));
        }
        return titles;
    }

    public void contactsClick() {
        driver.findElement(By.xpath(CONTACTS_XPATH)).click();
    }


}
