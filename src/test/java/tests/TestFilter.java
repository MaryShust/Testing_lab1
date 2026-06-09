package tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import utils.BrowserType;
import java.util.List;

@DisplayName("Проверка поиска с применением фильтров")
public class TestFilter extends TestBase{

    @ParameterizedTest
    @MethodSource("browserCases")
    @Tag("UI")
    @DisplayName("Применение фильтра - другие параметры (аккредитация компании)")
    public void searchVacanciesByOtherFilter(BrowserType browser) {
        initDriver(browser);
        String search = "тестировщик";
        startPage.searchVacanciesByText(search);
        startPage.closeAuthPopup();
        mainPage.clickOtherFilter();
        mainPage.clickToVacancy();

        String accreditationText = vacancyPage.getAccreditationText();
        Assertions.assertTrue(
            accreditationText.toLowerCase().contains("аккредитован"),
            "Компания не аккредитована. Текст: " + accreditationText
        );

        quitDriver();
    }

    @ParameterizedTest
    @MethodSource("browserCases")
    @Tag("UI")
    @DisplayName("Применение фильтра - формат работы (гибрид)")
    public void searchVacanciesByFormatJobFilter(BrowserType browser) {
        initDriver(browser);
        String search = "тестировщик";
        startPage.searchVacanciesByText(search);
        startPage.closeAuthPopup();
        mainPage.clickFormatJobFilter();
        mainPage.clickToVacancy();

        String formatJobText = vacancyPage.getFormatJob();
        Assertions.assertTrue(
            formatJobText.toLowerCase().contains("гибрид") ||
            formatJobText.toLowerCase().contains("удалённо") ||
            formatJobText.toLowerCase().contains("офис"),
            "Форма работы не соответствует фильтру. Текст: " + formatJobText
        );

        quitDriver();
    }

    @ParameterizedTest
    @MethodSource("browserCases")
    @Tag("UI")
    @DisplayName("Применение фильтра - опыт работы (От 1 года до 3 лет)")
    public void searchVacanciesByTimeFilter(BrowserType browser) {
        initDriver(browser);
        String search = "тестировщик";
        startPage.searchVacanciesByText(search);
        startPage.closeAuthPopup();
        mainPage.clickExperienceFilter();

        String experiencText = mainPage.checkExperienceFilter();
        Assertions.assertTrue(
                experiencText.toLowerCase().contains("опыт 1-3 года"),
                "Опыт работы не 1-3 года. Текст: " + experiencText
        );

        quitDriver();
    }

    @ParameterizedTest
    @MethodSource("browserCases")
    @Tag("UI")
    @DisplayName("Применение фильтра - тип занятости (подработка)")
    public void searchVacanciesByOpeningHoursFilter(BrowserType browser) {
        initDriver(browser);
        String search = "тестировщик";
        startPage.searchVacanciesByText(search);
        startPage.closeAuthPopup();
        mainPage.clickTypeOfEmploymentFilter();

        String employmentText = mainPage.checkTypeOfEmploymentFilter();
        Assertions.assertTrue(
                employmentText.toLowerCase().contains("подработка"),
                "Форма работы не подработка. Текст: " + employmentText
        );

        quitDriver();
    }

    @ParameterizedTest
    @MethodSource("browserCases")
    @Tag("UI")
    @DisplayName("Применение фильтра - тип занятости (подработка)")
    public void searchVacanciesByTypeOfEmploymentFilter(BrowserType browser) {
        initDriver(browser);
        String search = "тестировщик";
        startPage.searchVacanciesByText(search);
        startPage.closeAuthPopup();
        mainPage.clickTypeOfEmploymentFilter();


        String employmentText = mainPage.checkTypeOfEmploymentFilter();
        Assertions.assertTrue(
                employmentText.toLowerCase().contains("подработка"),
                "Форма работы не гибрид. Текст: " + employmentText
        );

        quitDriver();
    }

    @ParameterizedTest
    @MethodSource("browserCases")
    @Tag("UI")
    @DisplayName("Применение фильтра - опыт работы (От 1 года до 3 лет)")
    public void searchVacanciesByExperienceFilter(BrowserType browser) {
        initDriver(browser);
        String search = "тестировщик";
        startPage.searchVacanciesByText(search);
        startPage.closeAuthPopup();
        mainPage.clickExperienceFilter();

        String experiencText = mainPage.checkExperienceFilter();
        Assertions.assertTrue(
                experiencText.toLowerCase().contains("опыт 1-3 года"),
                "Опыт работы не 1-3 года. Текст: " + experiencText
        );

        quitDriver();
    }

    @ParameterizedTest
    @MethodSource("browserCases")
    @Tag("UI")
    @DisplayName("Применение фильтра - специализация (Инженер по качеству)")
    public void searchVacanciesBySpecializationFilter(BrowserType browser) {
        initDriver(browser);
        String search = "тестировщик";
        startPage.searchVacanciesByText(search);
        startPage.closeAuthPopup();
        mainPage.clickSpecializationFilter();
        List<String> specializationText = mainPage.getSpecializationsFilter();

        Assertions.assertTrue(
                specializationText.contains("инженер по качеству"),
                "Специализация не Инженер по качеству. Текст: " + specializationText
        );

        quitDriver();
    }
}
