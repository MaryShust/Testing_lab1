package tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@DisplayName("Проверка функционала соискателя")
public class TestApplicant extends TestBase {

    @Test
    @Tag("UI")
    @DisplayName("Поиск вакансий соискателем по профессии")
    public void searchVacanciesByProfession() {
        String search = "Junior-тестировщик";
        startPage.searchVacanciesByText(search);
        startPage.closeAuthPopup();
        mainPage.checkSearchHeader(search);
    }

    @Test
    @Tag("UI")
    @DisplayName("Поиск вакансий по профессии не из пула захардкоженных профессий")
    public void searchVacanciesByAlternativeProfession() {
        String search = "Quality Assurance";
        startPage.searchVacanciesByText(search);
        startPage.closeAuthPopup();
        mainPage.checkSearchHeader(search);
    }

    @Test
    @Tag("UI")
    @DisplayName("Поиск вакансий соискателем по несуществующей профессии или компании")
    public void searchNotExistentVacanciesByProfession() {
        String search = "qwertyqwertyuiop";
        String expectedResult = "По запросу «qwertyqwertyuiop» ничего не найдено";
        startPage.searchVacanciesByText(search);
        startPage.closeAuthPopup();
        mainPage.checkSearchHeader(expectedResult);
    }

    @Test
    @Tag("UI")
    @DisplayName("Поиск вакансий соискателем по компании")
    public void searchVacanciesByCompany() {
        String search = "Сбербанк";
        startPage.searchVacanciesByText(search);
        startPage.closeAuthPopup();
        mainPage.checkSearchHeader(search);
    }

    @Test
    @Tag("UI")
    @DisplayName("Создание резюме")
    public void createResume() {
        String speciality = "Тестировщик";
        headerPartPage.openAuth();
        authPage.loginByMailAndPassword(props);
        headerPartPage.openProfile();
        profilePage.closeWhatsNew();
        profilePage.removeResume(speciality);

        headerPartPage.openCreateResume();
        resumePage.createResume(speciality);

        mainPage.checkSuccessPublicationResume();
    }

    @Test
    @Tag("UI")
    @DisplayName("Поиск резюме с применением фильтра")
    public void searchVacanciesWithFilter() {
        String search = "Тестировщик";
        int price = 100000000;
        String excluded = "Рабство";
        startPage.openFilter();
        startPage.setFilter(search, price, excluded);
        mainPage.checkSearchHeader(search);
    }

    @Test
    @Tag("UI")
    @DisplayName("Поиск резюме с применением фильтра противоречащего себе")
    public void searchVacanciesWithFailFilter() {
        String vacancy = "Тестировщик";
        String expected = "По запросу «" + vacancy + "» ничего не найдено";
        int price = 100000000;
        startPage.openFilter();
        startPage.setFilter(vacancy, price, vacancy);
        mainPage.checkSearchHeader(expected);
    }
}