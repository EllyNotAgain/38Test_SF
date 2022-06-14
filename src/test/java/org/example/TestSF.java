package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;

public class TestSF {
    public static WebDriver driver;
    public static final String URL = "https://skillfactory.ru/";
    public static final ArrayList<String> TITLES_COURSES_MENU = new ArrayList<>();
    public static final String TITLE_EVENTS;
    public static final String TITLE_HOME;
    public static final String TITLE_CONTACTS;
    public static final String TITLE_CALL_ORDER;
    public static HomePage homePage;
    public static CataloguePage cataloguePage;
    public static ContactsPage contactsPage;

    static {
        TITLE_HOME = "Онлайн-школа SkillFactory — онлайн-обучение востребованным IT-профессиям";
        TITLE_EVENTS = "Бесплатные мероприятия и программы по Data Science, Big Data и другим направлениям - Школа по работе с данными Skillfactory";
        TITLE_CONTACTS = "Контакты школы по работе с данными Skillfactory";
        TITLES_COURSES_MENU.add("Все онлайн-курсы SkillFactory");
        TITLES_COURSES_MENU.add("Курсы по Data Science: лучшие онлайн-курсы по обучению с нуля");
        TITLES_COURSES_MENU.add("Курсы по аналитике данных с нуля: Data Analyst — подборка лучших онлайн-программ | ТОП от SkillFactory");
        TITLES_COURSES_MENU.add("Курсы Программирования: обучение программистов и разработчиков с нуля онлайн");
        TITLES_COURSES_MENU.add("Курсы веб-разработки с нуля: подборка лучших онлайн-курсов обучения web-разработчиков");
        TITLES_COURSES_MENU.add("Курсы по Backend-разработке: онлайн-обучение программированию в школе SkillFactory");
        TITLES_COURSES_MENU.add("Курсы Тестировщиков: онлайн-обучение QA-инженеров с нуля");
        TITLES_COURSES_MENU.add("Разработка мобильных приложений: онлайн-курсы обучения с нуля");
        TITLES_COURSES_MENU.add("Курсы по информационной безопасности с нуля: онлайн-обучение хакеров");
        TITLES_COURSES_MENU.add("Курсы по сетевой инфраструктуре: подборка лучших курсов обучения по управлению IT-инфраструктурой");
        TITLES_COURSES_MENU.add("Курсы по разработке игр: обучение разработчиков игр");
        TITLES_COURSES_MENU.add("Онлайн-курсы по Маркетингу: Обучение на маркетолога онлайн");
        TITLES_COURSES_MENU.add("Курсы дизайна с нуля: подборка курсов обучения дизайну онлайн");
        TITLES_COURSES_MENU.add("Курсы по менеджменту и управлению онлайн от SkillFactory и Product Live");
        TITLES_COURSES_MENU.add("Высшее образование онлайн: магистратура и ДПО");
        TITLES_COURSES_MENU.add("Курсы по созданию сайтов с нуля: онлайн-обучение разработчиков");
        TITLE_CALL_ORDER = "Заказать звонок";
    }

    @BeforeEach
    public void configTest() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        homePage = new HomePage(driver);
        cataloguePage = new CataloguePage(driver);
        contactsPage = new ContactsPage(driver);
        driver.get(URL);
    }

    @AfterEach
    public void finalizeResources() {
        if (driver != null)
            driver.quit();
    }

    @Test
    public void allCoursesButtonClick() {
        homePage.allCoursesButton();
        Assertions.assertEquals(TITLES_COURSES_MENU.get(0), driver.getTitle());
    }

    @Test
    public void freeEventsButtonClick() {
        homePage.freeEventsButton();
        Assertions.assertEquals(TITLE_EVENTS, driver.getTitle());
    }

    @Test
    public void menuCoursesClick() {
        ArrayList<String> actualTitles = homePage.menuCourses();
        Assertions.assertEquals(TITLES_COURSES_MENU, actualTitles);
    }

    @Test
    public void sfLogoClick() {
        homePage.allCoursesButton();
        cataloguePage.sfLogoClick();
        Assertions.assertEquals(TITLE_HOME, driver.getTitle());
    }

    @Test
    public void expandCoursesButtonClick(){
        homePage.allCoursesButton();
        String buttonTextFirst = cataloguePage.expandCoursesButtonText();
        cataloguePage.expandCoursesButtonClick();
        String buttonTextSecond = cataloguePage.expandCoursesButtonText();
        Assertions.assertNotEquals(buttonTextFirst, buttonTextSecond);
        cataloguePage.expandCoursesButtonClick();
        String buttonTextThird = cataloguePage.expandCoursesButtonText();
        Assertions.assertEquals(buttonTextFirst, buttonTextThird);
    }

    @Test
    public void expandSubjectsButtonClick() {
        homePage.allCoursesButton();
        String buttonTextFirst = cataloguePage.expandSubjectsButtonText();
        cataloguePage.expandSubjectsButtonClick();
        String buttonTextSecond = cataloguePage.expandSubjectsButtonText();
        Assertions.assertNotEquals(buttonTextFirst, buttonTextSecond);
        cataloguePage.expandSubjectsButtonClick();
        String buttonTextThird = cataloguePage.expandSubjectsButtonText();
        Assertions.assertEquals(buttonTextFirst, buttonTextThird);
    }

    @ParameterizedTest(name = "{index} DurationCheckbox {0} testing")
    @ValueSource(strings = {
            "0-12",
            "12-18",
            "18-24",
            "0-12,12-18",
            "12-18,18-24",
            "0-12,18-24",
            "0-12,12-18,18-24"
    })
    public void durationFilterRangeCheck(String duration) throws Exception {
        homePage.allCoursesButton();
        String durationFilter = cataloguePage.durationFilterGet(duration);
        Assertions.assertEquals(duration, durationFilter);
    }

    @ParameterizedTest(name = "{index} DurationCheckbox {0} testing")
    @ValueSource(strings = {
            "0-12",
            "12-18",
            "18-24",
            "0-12,12-18",
            "12-18,18-24",
            "0-12,18-24",
            "0-12,12-18,18-24"
    })
    public void durationFilterNumCheck(String duration) throws Exception {
        homePage.allCoursesButton();
        cataloguePage.durationFilterGet(duration);
        Assertions.assertEquals(cataloguePage.searchFilterCountGet(), cataloguePage.countCard());
    }

    @ParameterizedTest(name = "{index} DurationCheckbox {0} testing")
    @ValueSource(strings = {
            "0-12",
            "12-18",
            "18-24",
            "0-12,12-18",
            "12-18,18-24",
            "0-12,18-24",
            "0-12,12-18,18-24"
    })
    public void durationFilterCardCheck(String diapason) throws Exception {
        homePage.allCoursesButton();
        cataloguePage.durationFilterGet(diapason);
        String[] rangesString = diapason.split(",");
        ArrayList<Float[]> ranges = new ArrayList<>();
        for (String range: rangesString) {
            String[] limitsString = range.split("-");
            Float[] limits = {Float.parseFloat(limitsString[0]), Float.parseFloat(limitsString[1])};
            ranges.add(limits);
        }
        ArrayList<Float> durations = cataloguePage.durationsCard();
        for (float duration: durations) {
            boolean flag = false;
            for (Float[] range: ranges) {
                if (duration >= range[0] && duration <= range[1]) flag = true;
            }
            Assertions.assertTrue(flag, duration + " мес не входит в диапазон " + diapason);
        }
    }

    @ParameterizedTest(name = "{index} Price {0} - {1} testing")
    @CsvSource(value = {
            "-1000, 0",
            "fdfgd, gjhg",
            "'', ''",
            "999999999999999, 999999999999999",
            "100000, 150000"
    })
    public void priceFilterRangeCheck(String minPrice, String maxPrice) {
        homePage.allCoursesButton();
        cataloguePage.priceFilterSet(minPrice, maxPrice);
        int priceFilterMinValue = cataloguePage.priceFilterMinGet();
        int priceFilterMaxValue = cataloguePage.priceFilterMaxGet();
        Assertions.assertTrue(priceFilterMinValue > 0 && priceFilterMaxValue > 0 && priceFilterMinValue < priceFilterMaxValue,
                "min price = " + priceFilterMinValue + " , max price = " + priceFilterMaxValue);
    }

    @ParameterizedTest(name = "{index} Price {0} - {1} testing")
    @CsvSource(value = {
            "1800, 5000",
            "200000, 350000",
            "4999, 5000",
            "100000, 250000"
    })
    public void priceFilterNumCardCheck(String minPrice, String maxPrice) throws InterruptedException {
        homePage.allCoursesButton();
        cataloguePage.priceFilterSet(minPrice, maxPrice);
        Assertions.assertEquals(cataloguePage.searchFilterCountGet(), cataloguePage.countCard());
    }

    @ParameterizedTest(name = "{index} Price {0} - {1} testing")
    @CsvSource(value = {
            "1800, 5000",
            "200000, 350000",
            "100000, 250000"
    })
    public void priceFilterCardCheck(String minPrice, String maxPrice) throws InterruptedException {
        homePage.allCoursesButton();
        cataloguePage.priceFilterSet(minPrice, maxPrice);
        int priceFilterMinValue = cataloguePage.priceFilterMinGet();
        int priceFilterMaxValue = cataloguePage.priceFilterMaxGet();
        ArrayList<Integer> prices = cataloguePage.pricesCard();
        for (int price: prices) {
            Assertions.assertTrue(price >= priceFilterMinValue && price <= priceFilterMaxValue,
                    "Цена " + price + " не входит в диапазон " + priceFilterMinValue + " - " + priceFilterMaxValue);
        }
    }

    @ParameterizedTest(name = "{index} Search filter {0} testing")
    @ValueSource(strings = {"hyfjdzs", "java", "     "})
    public void searchFilterNameCheck(String searchString) throws InterruptedException {
        homePage.allCoursesButton();
        cataloguePage.searchFilterSet(searchString);
        Assertions.assertEquals(searchString, cataloguePage.searchFilterGet());
    }

    @ParameterizedTest(name = "{index} Search filter {0} testing")
    @ValueSource(strings = {"hyfjdzs", "java", "     "})
    public void searchFilterNumCheck(String searchString) throws InterruptedException {
        homePage.allCoursesButton();
        int countSearch = cataloguePage.searchFilterSet(searchString);
        int filter = cataloguePage.searchFilterCountGet();
        Assertions.assertEquals(filter, countSearch);
    }

    @Test
    public void ascendingSortPrice() throws InterruptedException {
        homePage.allCoursesButton();
        cataloguePage.sortCard("price:asc");
        ArrayList<Integer> sorted = cataloguePage.pricesCard();
        Collections.sort(sorted);
        Assertions.assertEquals(sorted, cataloguePage.pricesCard());
    }

    @Test
    public void descendingSortPrice() throws InterruptedException {
        homePage.allCoursesButton();
        cataloguePage.sortCard("price:desc");
        ArrayList<Integer> sorted = cataloguePage.pricesCard();
        sorted.sort(Collections.reverseOrder());
        Assertions.assertEquals(sorted, cataloguePage.pricesCard());
    }

    @Test
    public void ascendingSortCourseName() throws InterruptedException {
        homePage.allCoursesButton();
        cataloguePage.sortCard("title:asc");
        ArrayList<String> sorted = cataloguePage.nameCoursesCard();
        Collections.sort(sorted);
        Assertions.assertEquals(sorted, cataloguePage.nameCoursesCard());
    }

    @Test
    public void descendingSortCourseName() throws InterruptedException {
        homePage.allCoursesButton();
        cataloguePage.sortCard("title:desc");
        ArrayList<String> sorted = cataloguePage.nameCoursesCard();
        sorted.sort(Collections.reverseOrder());
        Assertions.assertEquals(sorted, cataloguePage.nameCoursesCard());
    }

    @Test
    public void contactsClick() {
        homePage.contactsClick();
        Assertions.assertEquals(TITLE_CONTACTS, driver.getTitle());
    }

    @Test
    public void callMeButtonClick() throws InterruptedException {
        homePage.contactsClick();
        String title = contactsPage.callMeButtonClick();
        Assertions.assertEquals(TITLE_CALL_ORDER, title);
    }

    @ParameterizedTest(name = "{index} Empty input testing {0} - {1} - {2}")
    @CsvSource(value = {
            "'', 9555555555, yes",
            "Гадя Петрович, '', yes",
            "Гадя Петрович, 9555555555, no"
    })
    public void emptyInputSend(String name, String phone, String permission) throws InterruptedException {
        homePage.contactsClick();
        contactsPage.callMeButtonClick();
        contactsPage.inputNameSet(name);
        contactsPage.inputPhoneSet(phone);
        contactsPage.checkboxSet(permission);
        contactsPage.sendButtonClick();
        String expectedError = "Пожалуйста, заполните все обязательные поля";
        Assertions.assertEquals(expectedError, contactsPage.textErrorGet());
    }

    @ParameterizedTest(name = "{index} Short phone number testing {0} - {1} - {2}")
    @CsvSource(value = {
            "Гадя Петрович, 8, yes",
            "Гадя Петрович, 999999999, yes",
    })
    public void shortPhoneNumberSend(String name, String phone, String permission) throws InterruptedException {
        homePage.contactsClick();
        contactsPage.callMeButtonClick();
        contactsPage.inputNameSet(name);
        contactsPage.inputPhoneSet(phone);
        contactsPage.checkboxSet(permission);
        contactsPage.sendButtonClick();
        String expectedError = "Слишком короткое значение";
        Assertions.assertEquals(expectedError, contactsPage.textErrorGet());
    }

}
