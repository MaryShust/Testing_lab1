package tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import utils.BrowserType;

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
}