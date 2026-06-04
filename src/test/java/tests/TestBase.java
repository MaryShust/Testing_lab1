package tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import pages.*;
import java.util.Properties;
import static com.codeborne.selenide.Selenide.sleep;

public class TestBase {
    protected StartPage startPage = new StartPage();
    protected MainPage mainPage = new MainPage();
    protected AuthPage authPage = new AuthPage();
    protected HeaderPartPage headerPartPage = new HeaderPartPage();
    protected StartEmployerPage startEmployerPage = new StartEmployerPage();
    protected ProfilePage profilePage = new ProfilePage();
    protected ResumePage resumePage = new ResumePage();
    protected Properties props = new Properties();

    public TestBase() {
        try {
            props.load(TestBase.class.getClassLoader().getResourceAsStream("credentials.properties"));
        } catch (Exception e) {
            throw new RuntimeException("Failed to load credentials.properties", e);
        }
    }

    @BeforeAll
    static void beforeAll() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());
        Configuration.baseUrl = "https://hh.ru";
        Configuration.browserSize = "1620x1080";
        Configuration.pageLoadTimeout = 50000;
        Configuration.headless = false;
        Configuration.browserPosition = "0x0";
//        Configuration.browser = System.getProperty("browser", "firefox");
        Configuration.browser = System.getProperty("browser", "chrome"); //
    }

    @BeforeEach
    void setupTests() {
        Selenide.clearBrowserCookies();
        startPage.openPage();
//        startPage.closeAuthPopup();
        startPage.closeCookieBanner();
        sleep(1000);
    }

    @AfterEach
    void afterEach() {
        Selenide.closeWebDriver();
    }
}