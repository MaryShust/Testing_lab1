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
}