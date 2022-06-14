package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record CataloguePage(WebDriver driver) {
    private static final String SF_LOGO_CLASS = "tn-elem__1292058111595239686685";
    private static final String EXPAND_COURSES_BUTTON_XPATH = "//*[@id='rec372498708']//div[text()='Направление']/following-sibling::div/button/span";
    private static final String EXPAND_SUBJECTS_BUTTON_XPATH = "//*[@id='rec372498708']//div[text()='Тематика']/following-sibling::div/button/span";
    private static final String DURATION_CHECKBOX_0_12_XPATH = "//*[@id='rec372498708']//input[@name='от 0 до 12']/following-sibling::div";
    private static final String DURATION_CHECKBOX_12_18_XPATH = "//*[@id='rec372498708']//input[@name='от 12 до 18']/following-sibling::div";
    private static final String DURATION_CHECKBOX_18_24_XPATH = "//*[@id='rec372498708']//input[@name='от 18 до 24']/following-sibling::div";
    private static final String DURATION_CHOSEN_FILTER_XPATH = "//div[@data-field-val]";
    private static final String PRICE_MIN_FILTER_XPATH = "//*[@id='rec372498708']//input[@name='price:min']";
    private static final String PRICE_MAX_FILTER_XPATH = "//*[@id='rec372498708']//input[@name='price:max']";
    private static final String SEARCH_FILTER_XPATH = "//*[@id='rec372498708']//input[@name='query']";
    private static final String PRODUCT_CARD_XPATH = "//*[@id='rec372498708']//div[@data-product-uid]";
    private static final String LOAD_MORE_BUTTON_XPATH = "//td[text()='Загрузить еще']";
    private static final String SEARCH_PRODUCT_NUMBER_XPATH = "//*[@id='rec372498708']//span[@class='js-store-filters-prodsnumber']";
    private static final String SEARCH_PRODUCT_NAME_XPATH = "//*[@id='rec372498708']//div[contains(@class,'t-store__filter__chosen-val')]";
    private static final String CARD_PRICE_XPATH = "//*[@id='rec372498708']//div[@data-product-price-def]";
    private static final String CARD_DESCRIPTION_XPATH = "//*[@id='rec372498708']//div[contains(@class,'t-store__card__descr')]";
    private static final String FOOTER_ID = "rec304073632";
    private static final String SORT_DROPDOWN_XPATH = "//*[@id='rec372498708']//select[@name='sort']";
    private static final String CARD_NAME_XPATH = "//*[@id='rec372498708']//div[contains(@class,'t-store__card__title')]";

    public void sfLogoClick() {
        driver.findElement(By.className(SF_LOGO_CLASS)).click();
    }

    public String expandCoursesButtonText() {
        return driver.findElement(By.xpath(EXPAND_COURSES_BUTTON_XPATH)).getText();
    }

    public void expandCoursesButtonClick() {
        driver.findElement(By.xpath(EXPAND_COURSES_BUTTON_XPATH)).click();
    }

    public String expandSubjectsButtonText() {
        return driver.findElement(By.xpath(EXPAND_SUBJECTS_BUTTON_XPATH)).getText();
    }

    public void expandSubjectsButtonClick() {
        driver.findElement(By.xpath(EXPAND_SUBJECTS_BUTTON_XPATH)).click();
    }

    public String durationFilterGet(String duration) throws Exception {
        return switch (duration) {
            case "0-12" -> {
                driver.findElement(By.xpath(DURATION_CHECKBOX_0_12_XPATH)).click();
                yield durationFilter();
            }
            case "12-18" -> {
                driver.findElement(By.xpath(DURATION_CHECKBOX_12_18_XPATH)).click();
                yield durationFilter();
            }
            case "18-24" -> {
                driver.findElement(By.xpath(DURATION_CHECKBOX_18_24_XPATH)).click();
                yield durationFilter();
            }
            case "0-12,12-18" -> {
                driver.findElement(By.xpath(DURATION_CHECKBOX_0_12_XPATH)).click();
                driver.findElement(By.xpath(DURATION_CHECKBOX_12_18_XPATH)).click();
                yield durationFilter();
            }
            case "12-18,18-24" -> {
                driver.findElement(By.xpath(DURATION_CHECKBOX_12_18_XPATH)).click();
                driver.findElement(By.xpath(DURATION_CHECKBOX_18_24_XPATH)).click();
                yield durationFilter();
            }
            case "0-12,18-24" -> {
                driver.findElement(By.xpath(DURATION_CHECKBOX_0_12_XPATH)).click();
                driver.findElement(By.xpath(DURATION_CHECKBOX_18_24_XPATH)).click();
                yield durationFilter();
            }
            case "0-12,12-18,18-24" -> {
                driver.findElement(By.xpath(DURATION_CHECKBOX_0_12_XPATH)).click();
                driver.findElement(By.xpath(DURATION_CHECKBOX_12_18_XPATH)).click();
                driver.findElement(By.xpath(DURATION_CHECKBOX_18_24_XPATH)).click();
                yield durationFilter();
            }
            default -> throw new Exception("Duration check error");
        };
    }

    public String durationFilter() throws InterruptedException {
        Thread.sleep(3000);
        List<WebElement> elements = driver.findElements(By.xpath(DURATION_CHOSEN_FILTER_XPATH));
        ArrayList<String> filterList = new ArrayList<>();
        for (WebElement el: elements) {
            String elText = el.getText();
            filterList.add(elText.substring(3, elText.indexOf(" до ")) + "-" + elText.substring(elText.lastIndexOf(' ') + 1));
        }
        Collections.sort(filterList);
        String filter = filterList.get(0);
        for (int i = 1; i < filterList.size(); i++) {
            filter = filter.concat("," + filterList.get(i));
        }
        return filter;
    }

    public ArrayList<Float> durationsCard() throws InterruptedException {
        loadMoreButtonClick();
        ArrayList<Float> durations = new ArrayList<>();
        List<WebElement> descriptionList = driver.findElements(By.xpath(CARD_DESCRIPTION_XPATH));
        Actions action = new Actions(driver);
        action.moveToElement(driver.findElement(By.id(FOOTER_ID))).build().perform();
        Thread.sleep(5000);
        Pattern patternM = Pattern.compile("Длительность .{1,7}? мес", Pattern.DOTALL);
        Pattern patternW = Pattern.compile("Длительность .{1,7}? нед", Pattern.DOTALL);
        for (WebElement description: descriptionList) {
            Matcher matcher = patternM.matcher(description.getText());
            if (!matcher.find()) {
                matcher = patternW.matcher(description.getText());
            }
            matcher.reset();
            if (matcher.find()) {
                String matsherStr = matcher.group();
                String measure = matsherStr.substring(matsherStr.length() - 3);
                String dur = matsherStr.substring(0, matsherStr.length() - 4).trim();
                float duration = Float.parseFloat(dur.substring(dur.lastIndexOf(" ") + 1).replaceAll(",","."));
                if (measure.equals("мес")) durations.add(duration);
                else if (measure.equals("нед")) durations.add(duration / 4);
            } else {
                durations.add(-1f);
            }
            matcher.reset();
        }
        return durations;
    }

    public void priceFilterSet(String minPrice, String maxPrice) {
        driver.findElement(By.xpath(PRICE_MIN_FILTER_XPATH)).sendKeys(Keys.CONTROL+"a");
        driver.findElement(By.xpath(PRICE_MIN_FILTER_XPATH)).sendKeys(minPrice);
        driver.findElement(By.xpath(PRICE_MAX_FILTER_XPATH)).sendKeys(Keys.CONTROL+"a");
        driver.findElement(By.xpath(PRICE_MAX_FILTER_XPATH)).sendKeys(maxPrice+Keys.ENTER);
    }

    public int priceFilterMinGet() {
        return Integer.parseInt(driver.findElement(By.xpath(PRICE_MIN_FILTER_XPATH)).getAttribute("value").replaceAll(" ", ""));
    }

    public int priceFilterMaxGet() {
        return Integer.parseInt(driver.findElement(By.xpath(PRICE_MAX_FILTER_XPATH)).getAttribute("value").replaceAll(" ", ""));
    }

    public int countCard() throws InterruptedException {
        loadMoreButtonClick();
        Thread.sleep(3000);
        return driver.findElements(By.xpath(PRODUCT_CARD_XPATH)).size();
    }

    public ArrayList<Integer> pricesCard() throws InterruptedException {
        loadMoreButtonClick();
        pageDown();
        Thread.sleep(3000);
        ArrayList<Integer> prices = new ArrayList<>();
        List<WebElement> priceList = driver.findElements(By.xpath(CARD_PRICE_XPATH));
        for (WebElement price: priceList) {
            prices.add(Integer.parseInt(price.getAttribute("data-product-price-def")));
        }
        return prices;
    }

    public void loadMoreButtonClick() throws InterruptedException {
        Actions action = new Actions(driver);
        Thread.sleep(5000);
        while (driver.findElement(By.xpath(LOAD_MORE_BUTTON_XPATH)).getSize().getHeight() != 0) {
            action.moveToElement(driver.findElement(By.xpath(LOAD_MORE_BUTTON_XPATH))).click().build().perform();
            Thread.sleep(3000);
        }
    }

    public void pageDown() throws InterruptedException {
        Actions action = new Actions(driver);
        action.moveToElement(driver.findElement(By.id(FOOTER_ID))).build().perform();
        Thread.sleep(3000);
    }

    public int searchFilterSet(String searchString) throws InterruptedException {
        driver.findElement(By.xpath(SEARCH_FILTER_XPATH)).sendKeys(searchString+Keys.ENTER);
        Thread.sleep(3000);
        return countCard();
    }

    public int searchFilterCountGet() throws InterruptedException {
        Thread.sleep(3000);
        WebElement numValue = driver.findElement(By.xpath(SEARCH_PRODUCT_NUMBER_XPATH));
        if (numValue.getText().equals("")) {
            return 0;
        } else return Integer.parseInt(numValue.getText());
    }

    public String searchFilterGet() throws InterruptedException {
        Thread.sleep(3000);
        WebElement filter = driver.findElement(By.xpath(SEARCH_PRODUCT_NAME_XPATH));
        return filter.getAttribute("data-field-val");
    }

    public void sortCard(String order) {
        Select sort = new Select(driver.findElement(By.xpath(SORT_DROPDOWN_XPATH)));
        sort.selectByValue(order);
    }

    public ArrayList<String> nameCoursesCard() throws InterruptedException {
        loadMoreButtonClick();
        pageDown();
        List<WebElement> cardNames = driver.findElements(By.xpath(CARD_NAME_XPATH));
        ArrayList<String> names = new ArrayList<>();
        for (WebElement cardName: cardNames) {
            names.add(cardName.getText().toLowerCase());
        }
        return names;
    }


}
