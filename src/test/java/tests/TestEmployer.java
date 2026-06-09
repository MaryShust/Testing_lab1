package tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import utils.BrowserType;

@DisplayName("Проверка функционала работодателя")
public class TestEmployer extends TestBase {

    @ParameterizedTest
    @MethodSource("browserCases")
    @Tag("UI")
    @DisplayName("Проверка промотекста для работодателя")
    public void checkTitleForEmployers(BrowserType browser) {
        initDriver(browser);
        headerPartPage.clickEmployerLink();
        startEmployerPage.checkPromoTitle();
        quitDriver();
    }

    @ParameterizedTest
    @MethodSource("browserCases")
    @Tag("UI")
    @DisplayName("Поиск резюме по профессии")
    public void searchVacanciesByProfession(BrowserType browser) {
        initDriver(browser);
        String search = "Тестировщик";
        headerPartPage.clickEmployerLink();
        startEmployerPage.searchResumeByText(search);
        String headerText = startEmployerPage.getSearchHeader();
        Assertions.assertTrue(headerText.contains("Подходящие кандидаты"), 
            "Заголовок должен содержать: Подходящие кандидаты");
        quitDriver();
    }

    @ParameterizedTest
    @MethodSource("browserCases")
    @Tag("UI")
    @DisplayName("Поиск резюме по профессии не из пула захардкоженных профессий")
    public void searchVacanciesByAlternativeProfession(BrowserType browser) {
        initDriver(browser);
        String search = "Quality Assurance";
        headerPartPage.clickEmployerLink();
        startEmployerPage.searchResumeByText(search);
        String headerText = startEmployerPage.getSearchHeader();
        Assertions.assertTrue(headerText.contains("Подходящие кандидаты"), 
            "Заголовок должен содержать: Подходящие кандидаты");
        quitDriver();
    }

    @ParameterizedTest
    @MethodSource("browserCases")
    @Tag("UI")
    @DisplayName("Поиск резюме по несуществующей профессии, но после поиска существующей")
    public void searchNotExistentVacanciesByProfessionAfterCorrectProfession(BrowserType browser) {
        initDriver(browser);
        String search1 = "Тестировщик";
        String search2 = "12345";
        headerPartPage.clickEmployerLink();
        startEmployerPage.searchResumeByText(search1);
        String header1 = startEmployerPage.getSearchHeader();
        Assertions.assertTrue(header1.contains("Подходящие кандидаты"), 
            "После первого поиска должен быть результат");

        startEmployerPage.searchResumeByText(search2);
        startEmployerPage.checkFailSearchHeader();
        quitDriver();
    }

    @ParameterizedTest
    @MethodSource("browserCases")
    @Tag("UI")
    @DisplayName("Поиск резюме по несуществующей профессии")
    public void searchNotExistentVacanciesByProfession(BrowserType browser) {
        initDriver(browser);
        String search = "Тестировщик12345";
        headerPartPage.clickEmployerLink();
        startEmployerPage.searchResumeByText(search);
        startEmployerPage.checkFailSearchHeader();
        quitDriver();
    }
}