package tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import utils.BrowserType;
import java.util.stream.Stream;
import static com.codeborne.selenide.Selenide.open;

@DisplayName("Проверка функционала главной страницы")
public class TestStartPage extends TestBase {

//    static Stream<BrowserType> browserCases() {
//        return Stream.of(BrowserType.CHROME, BrowserType.FIREFOX);
//    }
//
//    @ParameterizedTest
//    @MethodSource("browserCases")
//    @Tag("UI")
//    @DisplayName("Проверка промотекста для соискателя")
//    void checkTitleForApplicants(BrowserType browser) {
//        initDriver(browser);
//        open("/");
//        startPage.closeAuthPopup();
//        startPage.closeCookieBanner();
//        startPage.checkPromoTitle("Напишите телефон, чтобы работодатели могли предложить вам работу");
//        quitDriver();
//    }

    @Test
    @Tag("UI")
    @DisplayName("Проверка промотекста для соискателя")
    public void checkTitleForApplicants() {
        startPage.checkPromoTitle("Напишите телефон, чтобы работодатели могли предложить вам работу");
    }

    @Test
    @Tag("UI")
    @DisplayName("Вход")
    public void login() {
        headerPartPage.openAuth();
        authPage.loginByMailAndPassword(props);
        mainPage.checkHasVacancy();
    }

    @Test
    @Tag("UI")
    @DisplayName("Выход")
    public void logout() {
        headerPartPage.openAuth();
        authPage.loginByMailAndPassword(props);
        headerPartPage.logout();
        startPage.checkPromoTitle("Напишите телефон, чтобы работодатели могли предложить вам работу");
    }

    @Test
    @Tag("UI")
    @DisplayName("Проверка смены города")
    public void checkRegionSwitcher() {
        headerPartPage.changeRegion("Краснодар");
        startPage.checkWorkInCompanyTitle(
                "Поиск работы в Краснодаре",
                "Работа в компаниях Краснодара",
                "Работа по профессиям в Краснодаре"
        );
    }
}