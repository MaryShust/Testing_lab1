package tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import pages.*;
import utils.BrowserType;
import java.util.stream.Stream;
import static com.codeborne.selenide.Selenide.sleep;

public class TestBase {
    protected StartPage startPage = new StartPage();
    protected MainPage mainPage = new MainPage();
    protected HeaderPartPage headerPartPage = new HeaderPartPage();
    protected StartEmployerPage startEmployerPage = new StartEmployerPage();
    protected ProfilePage profilePage = new ProfilePage();
    protected ResumePage resumePage = new ResumePage();
    protected VacancyPage vacancyPage = new VacancyPage();
    protected CompanyPage companyPage = new CompanyPage();

//    @BeforeAll
//    static void beforeAll() {
//        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());
//        Configuration.baseUrl = "https://hh.ru";
//        Configuration.browserSize = "1620x1080";
//        Configuration.pageLoadTimeout = 50000;
//        Configuration.headless = false;
//        Configuration.browserPosition = "0x0";
//    }

    protected void initDriver(BrowserType browserType) {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());
        Configuration.baseUrl = "https://hh.ru";
        Configuration.browserSize = "1620x1080";
        Configuration.pageLoadTimeout = 120000;
        Configuration.headless = false;
        Configuration.browserPosition = "0x0";

        if (browserType == BrowserType.FIREFOX) {
            Configuration.browser = "firefox";
        } else {
            Configuration.browser = "chrome";
        }
        Selenide.clearBrowserCookies();
        startPage.openPage();
        startPage.closeAuthPopup();
        startPage.closeCookieBanner();
        sleep(1000);
    }

    protected void quitDriver() {
        Selenide.closeWebDriver();
    }

    static Stream<BrowserType> browserCases() {
        return Stream.of(BrowserType.CHROME, BrowserType.FIREFOX);
    }
}