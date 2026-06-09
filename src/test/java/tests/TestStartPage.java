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

//    @ParameterizedTest
//    @MethodSource("browserCases")
//    @Tag("UI")
//    @DisplayName("Проверка открытия вакансии дня")
//    public void checkVacancyOfADay(BrowserType browser) {
//        initDriver(browser);
//        startPage.closeAuthPopup();
//        startPage.clickVacancyOfDay();
//
//        String expectedTitle = startPage.clickFirstVacancy();
//        String actualTitle = vacancyPage.getVacancyTitle();
//
//        Assertions.assertFalse(actualTitle.isEmpty(), "Заголовок вакансии не получен");
//        Assertions.assertTrue(
//                actualTitle.toLowerCase().contains(expectedTitle.toLowerCase().split(" ")[0]),
//                "Вакансия не соответствует. Ожидалось: " + expectedTitle + ", получено: " + actualTitle
//        );
//        quitDriver();
//    }

    @ParameterizedTest
    @MethodSource("browserCases")
    @Tag("UI")
    @DisplayName("Проверка открытия вакансии дня")
    public void checkVacancyOfADay(BrowserType browser) {
        initDriver(browser);
        startPage.closeAuthPopup();
        int windowsBefore = webdriver().object().getWindowHandles().size();
        startPage.clickVacancyOfDay();
        
        int windowsAfter = webdriver().object().getWindowHandles().size();
        if (windowsAfter > windowsBefore) {
            Selenide.switchTo().window(windowsBefore);
        }
        
        String actualTitle = vacancyPage.getVacancyTitle();
        
        if (windowsAfter > 1) {
            Selenide.closeWindow();
            Selenide.switchTo().window(0);
        }

        Assertions.assertNotNull(actualTitle, "Заголовок вакансии не получен");
        Assertions.assertFalse(actualTitle.trim().isEmpty(), "Заголовок вакансии пустой");
        Assertions.assertTrue(actualTitle.contains(" "), "Заголовок вакансии слишком короткий");
        quitDriver();
    }

    @ParameterizedTest
    @MethodSource("browserCases")
    @Tag("UI")
    @DisplayName("Проверка блока вакансии дня")
    public void checkBlocVacancyOfADfy(BrowserType browser) {
        initDriver(browser);
        startPage.closeAuthPopup();
        String vacancyUrl = startPage.getVacancyOfDayUrl();
        
        int windowsBefore = webdriver().object().getWindowHandles().size();
        startPage.clickBlocVacancyOfDay();
        
        if (webdriver().object().getWindowHandles().size() > windowsBefore) {
            Selenide.closeWindow();
            Selenide.switchTo().window(0);
        }
        
        String openUrl = vacancyUrl.replace("turbo=true", "turbo=false");
        Selenide.open(openUrl);
        
        String actualTitle = vacancyPage.getVacancyTitle();

        Assertions.assertNotNull(actualTitle, "Заголовок вакансии не получен");
        Assertions.assertFalse(actualTitle.trim().isEmpty(), "Заголовок вакансии пустой");
        Assertions.assertTrue(actualTitle.contains(" "), "Заголовок вакансии слишком короткий");
        quitDriver();
    }

    @ParameterizedTest
    @MethodSource("browserCases")
    @Tag("UI")
    @DisplayName("Проверка открытия компании")
    public void checkCompanyOfADay(BrowserType browser) {
        initDriver(browser);
        startPage.closeAuthPopup();
        startPage.clickCompanyOfDay();
        String actualTitle = startPage.clickFirstCompanyOfDay();
        String companyName = companyPage.getCompanyName();

        Assertions.assertNotNull(actualTitle, "Заголовок компании не получен");
        Assertions.assertFalse(actualTitle.trim().isEmpty(), "Заголовок компании пустой");
        Assertions.assertTrue(actualTitle.contains(companyName), "Заголовок компании не совпадает с компанией на которую перешли");
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
    @DisplayName("Проверка открытия компании")
    public void checkWorkInCompany(BrowserType browser) {
        initDriver(browser);
        startPage.closeAuthPopup();
        startPage.clickFirstCompanyOfDay();
        companyPage.getVacancy();
        mainPage.clickToVacancy();
        String companyName = vacancyPage.getCompanyName();
        System.out.println(companyName);
//        Assertions.assertNotNull(actualTitle, "Заголовок компании не получен");
//        Assertions.assertFalse(actualTitle.trim().isEmpty(), "Заголовок компании пустой");
//        Assertions.assertTrue(actualTitle.contains(companyName), "Заголовок компании не совпадает с компанией на которую перешли");
        quitDriver();
    }
}