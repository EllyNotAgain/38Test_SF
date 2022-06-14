package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public record ContactsPage(WebDriver driver) {
    private static final String CALL_ME_BUTTON_CLASS = "tn-elem__1293038981568972946783";
    private static final String CALL_ORDER_TITLE_CLASS = "t702__title";
    private static final String SEND_BUTTON_XPATH = "//div[@class='t-form__submit']/button";
    private static final String INPUT_NAME_XPATH = "//*[@id='form130614803']//input[@name='name']";
    private static final String INPUT_PHONE_XPATH = "//*[@id='form130614803']//input[@name='tildaspec-phone-part[]']";
    private static final String CHECKBOX_XPATH = "//*[@id='form130614803']//div[@class='t-checkbox__indicator']";
    private static final String INPUT_ERROR = "//div[@class='t-form__errorbox-middle']//p[@style='display: block;']";

    public String callMeButtonClick() throws InterruptedException {
        driver.findElement(By.className(CALL_ME_BUTTON_CLASS)).click();
        Thread.sleep(2000);
        WebElement title = driver.findElement(By.className(CALL_ORDER_TITLE_CLASS));
        if (title.getSize().getHeight() != 0) {
            return title.getText();
        } else return "";
    }

    public void inputNameSet(String name) {
        driver.findElement(By.xpath(INPUT_NAME_XPATH)).sendKeys(name);
    }

    public void inputPhoneSet(String phone) {
        driver.findElement(By.xpath(INPUT_PHONE_XPATH)).sendKeys(phone);
    }

    public void checkboxSet(String permission) {
        String defaultValue = "yes";
        if (!permission.equals(defaultValue)) driver.findElement(By.xpath(CHECKBOX_XPATH)).click();
    }

    public void sendButtonClick() {
        driver.findElement(By.xpath(SEND_BUTTON_XPATH)).click();
    }

    public String textErrorGet() {
        String errorText = "";
        if (driver.findElement(By.xpath(INPUT_ERROR)).getSize().getHeight() != 0) {
            List<WebElement> errorsList = driver.findElements(By.xpath(INPUT_ERROR));
            for (WebElement error: errorsList) {
                errorText = errorText.concat(error.getText());
            }
        }
        return errorText;
    }

}
