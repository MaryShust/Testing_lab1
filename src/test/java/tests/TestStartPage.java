package tests;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import utils.BrowserType;

import static com.codeborne.selenide.Selenide.sleep;
import static com.codeborne.selenide.Selenide.webdriver;

@DisplayName("Проверка функционала главной страницы")
public class TestStartPage extends TestBase {

    @ParameterizedTest
    @MethodSource("browserCases")
    @Tag("UI")
    @DisplayName("Проверка промотекста для соискателя")
    public void checkTitleForApplicants(BrowserType browser) {
        initDriver(browser);
        startPage.checkPromoTitle("Напишите телефон, чтобы работодатели могли предложить вам работу");
        quitDriver();
    }

    @ParameterizedTest
    @MethodSource("browserCases")
    @Tag("UI")
    @DisplayName("Вход")
    public void login(BrowserType browser) {
        initDriver(browser);
        headerPartPage.openAuth();
        authPage.loginByMailAndPassword(props);
        mainPage.checkHasVacancy();
        quitDriver();
    }

    @ParameterizedTest
    @MethodSource("browserCases")
    @Tag("UI")
    @DisplayName("Выход")
    public void logout(BrowserType browser) {
        initDriver(browser);
        headerPartPage.openAuth();
        authPage.loginByMailAndPassword(props);
        headerPartPage.logout();
        startPage.checkPromoTitle("Напишите телефон, чтобы работодатели могли предложить вам работу");
        quitDriver();
    }

    @ParameterizedTest
    @MethodSource("browserCases")
    @Tag("UI")
    @DisplayName("Проверка смены города")
    public void checkRegionSwitcher(BrowserType browser) {
        initDriver(browser);
        headerPartPage.changeRegion("Краснодар");
        startPage.checkWorkInCompanyTitle(
                "Поиск работы в Краснодаре",
                "Работа в компаниях Краснодара",
                "Работа по профессиям в Краснодаре"
        );
        quitDriver();
    }

    @ParameterizedTest
    @MethodSource("browserCases")
    @Tag("UI")
    @DisplayName("Проверка открытия вакансии дня")
    public void checkVacancyOfADay(BrowserType browser) {
        initDriver(browser);
        startPage.closeAuthPopup();
        startPage.clickVacancyOfDay();

        String expectedTitle = startPage.clickFirstVacancy();
        String actualTitle = vacancyPage.getVacancyTitle();

        Assertions.assertFalse(actualTitle.isEmpty(), "Заголовок вакансии не получен");
        Assertions.assertTrue(
                actualTitle.toLowerCase().contains(expectedTitle.toLowerCase().split(" ")[0]),
                "Вакансия не соответствует. Ожидалось: " + expectedTitle + ", получено: " + actualTitle
        );
        quitDriver();
    }

    @ParameterizedTest
    @MethodSource("browserCases")
    @Tag("UI")
    @DisplayName("Проверка открытия компании")
    public void checkCompanyOfADay(BrowserType browser) {
        initDriver(browser);
        startPage.closeAuthPopup();
        startPage.searchVacanciesByText("тестировщик");
        String expectedCompany = startPage.clickFirstCompany();
        
        sleep(3000);
        
        Assertions.assertNotNull(
                Selenide.webdriver().object(),
                "Веб-драйвер не инициализирован"
        );
        
        String pageUrl = Selenide.webdriver().object().getCurrentUrl();
        
        Assertions.assertTrue(
                pageUrl.contains("/employer/") || pageUrl.contains("/company/") || pageUrl.contains("/vacancy/"),
                "Не выполнен переход на страницу. URL: " + pageUrl
        );
        quitDriver();
    }

    @ParameterizedTest
    @MethodSource("browserCases")
    @Tag("UI")
    @DisplayName("Проверка работы в компании")
    public void checkWorkFromHome(BrowserType browser) {
        initDriver(browser);
        startPage.clickWorkFromHome();
        startPage.closeAuthPopup();
        String titleText = mainPage.checkWorkFromHome();

        Assertions.assertTrue(
                titleText.contains("Можно удалённо"),
                "Вакансия не соответствует."
        );
        quitDriver();
    }

    @ParameterizedTest
    @MethodSource("browserCases")
    @Tag("UI")
    @DisplayName("Проверка работы из дома")
    public void checkWorkInCompany(BrowserType browser) {
        initDriver(browser);
        startPage.clickWorkInCompany();

//        String titleText = mainPage.checkWorkFromHome();
//
//        Assertions.assertTrue(
//                titleText.contains("Можно удалённо"),
//                "Вакансия не соответствует."
//        );
        quitDriver();
    }
}